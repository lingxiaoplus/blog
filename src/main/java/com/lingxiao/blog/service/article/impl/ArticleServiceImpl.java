package com.lingxiao.blog.service.article.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.BlogApplication;
import com.lingxiao.blog.bean.*;
import com.lingxiao.blog.bean.vo.HomePageVo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.mapper.*;
import com.lingxiao.blog.service.article.ArticleService;
import com.lingxiao.blog.service.article.LabelService;
import com.lingxiao.blog.service.system.ThemeService;
import com.lingxiao.blog.utils.UIDUtil;
import com.lingxiao.blog.bean.vo.ArticleDetailVo;
import com.lingxiao.blog.bean.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleLabelMapper articleLabelMapper;
    @Autowired
    private LabelService labelService;
    @Autowired
    private ThemeService themeService;

    @Transactional
    @Override
    public void addArticle(Article article) {
        if (null != article.getId()){
            //更新文章
            updateArticle(article);
            return;
        }
        //UserInfo userInfo = LoginInterceptor.getUserInfo();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        long id = UIDUtil.nextId();
        article.setId(id);
        article.setCreateAt(new Date());
        article.setUpdateAt(article.getCreateAt());
        article.setUserId(user.getUserId());
        int count = articleMapper.insertSelective(article);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.CATEGORY_INSERT_ERROR);
        }
        if (!CollectionUtils.isEmpty(article.getLabelIds())){
            addLabels(article.getId(),article.getLabelIds());
        }
        log.info("文章，{}", article);
    }

    private void addLabels(long aId, List<Long> ids){
        List<ArticleLabel> labels = ids.stream().map((labelId) -> {
            ArticleLabel articleLabel = new ArticleLabel();
            articleLabel.setArticleId(aId);
            articleLabel.setLabelId(labelId);
            return articleLabel;
        }).collect(Collectors.toList());
        int labelsCount = articleLabelMapper.insertList(labels);
        if (labelsCount != labels.size()){
            throw new BlogException(ExceptionEnum.ARTICLE_LABEL_INSERT_ERROR);
        }
    }

    @Transactional(noRollbackFor = {Exception.class})
    @Override
    public void updateArticle(Article article) {
        if (null == articleMapper.selectByPrimaryKey(article.getId())){
            throw new BlogException(ExceptionEnum.ARTICLE_SELECT_ERROR);
        }
        article.setCreateAt(null);
        article.setUserId(null);
        article.setUpdateAt(new Date());
        int count = articleMapper.updateByPrimaryKeySelective(article);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.ARTICLE_UPDATE_ERROR);
        }
        //List<Label> originalLabels = labelService.getLabelByArticleId(article.getId());
        List<Long> labelIds = article.getLabelIds();
        labelService.updateArticleLabelByArticleId(article.getId(),labelIds);
    }


    @Override
    public ArticleDetailVo getArticleContent(Long id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        if (article == null){
            throw new BlogException(ExceptionEnum.ARTICLE_SELECT_ERROR);
        }
        article.setWatchCount(article.getWatchCount()+1);
        ArticleDetailVo articleDetailVo = transformArticle(article);
        articleMapper.updateByPrimaryKeySelective(article);
        return articleDetailVo;
    }

    private ArticleDetailVo transformArticle(Article article){
        ArticleDetailVo articleDetail = new ArticleDetailVo();
        articleDetail.setId(String.valueOf(article.getId()));
        articleDetail.setUserId(String.valueOf(article.getUserId()));
        articleDetail.setCategoryId(String.valueOf(article.getCategoryId()));

        articleDetail.setTitle(article.getTitle());
        articleDetail.setContent(article.getContent());
        articleDetail.setHeadImage(article.getHeadImage());

        DateTime createTime = new DateTime(article.getCreateAt());
        String createString = createTime.toString("yyyy-MM-dd HH:mm:ss");
        articleDetail.setCreateAt(createString);
        DateTime updateTime = new DateTime(article.getUpdateAt());
        String updateString = updateTime.toString("yyyy-MM-dd HH:mm:ss");
        articleDetail.setUpdateAt(updateString);

        articleDetail.setCommentCount(article.getCommentCount());
        articleDetail.setLikeCount(article.getLikeCount());
        articleDetail.setWatchCount(article.getWatchCount());

        List<Label> labels = labelService.getLabelByArticleId(article.getId());
        articleDetail.setLabels(labels);

        return articleDetail;
    }

    @Override
    public PageResult<ArticleVo> getArticles(String keyword,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize,"create_at desc");

        Example example = new Example(Article.class);
        example.createCriteria()
                .andNotEqualTo("status",ContentValue.ARTICLE_STATUS_DELETED)
                .andLike("title","%"+keyword+"%");
        //example.orderBy("createAt").desc();
        List<Article> articles = articleMapper.selectByExample(example);

        PageInfo<Article> pageInfo = PageInfo.of(articles);


        List<ArticleVo> articleVoList = articleVoConvert(pageInfo.getList());
        return new PageResult<ArticleVo>(pageInfo.getTotal(),pageInfo.getPages(),articleVoList);
    }

    @Override
    public List<Article> getRankArticle(int size){
        Example example = new Example(Article.class);
        example.setOrderByClause("watch_count desc");
        List<Article> articles = articleMapper.selectByExampleAndRowBounds(example,new RowBounds(0,size));
        return articles;
    }

    @Cacheable(value = "banners")
    @Override
    public ResponseResult<HomePageVo> getHomePageBanner(int bannerSize){
        List<Article> articles = getRankArticle(bannerSize);
        List<ArticleVo> banners = articleVoConvert(articles);
        Hitokoto hitokoto = themeService.getHitokoto();
        HomePageVo homePageVo = new HomePageVo();
        homePageVo.setBanners(banners);
        homePageVo.setHitokoto(hitokoto);
        ResponseResult<HomePageVo> result = new ResponseResult<>();
        result.setData(homePageVo);
        return result;
    }

    @Override
    public void deleteArticle(Long id) {
        int count = articleMapper.updateArticleStatus(id, ContentValue.ARTICLE_STATUS_DELETED);
        if (count != 1){
            throw new BlogException(ExceptionEnum.ARTICLE_DELETE_ERROR);
        }
    }

    private List<ArticleVo> articleVoConvert(List<Article> articles){
        List<ArticleVo> articleVoList = articles
                .stream()
                .map((item) -> {
                    ArticleVo articleVo = new ArticleVo();
                    articleVo.setId(String.valueOf(item.getId()));
                    articleVo.setTitle(item.getTitle());
                    DateTime dateTime = new DateTime(item.getUpdateAt());
                    String dateString = dateTime.toString("yyyy-MM-dd");
                    articleVo.setUpdateTime(dateString);
                    String createString = new DateTime(item.getCreateAt()).toString("yyyy-MM-dd");
                    articleVo.setCreateTime(createString);
                    User user = userMapper.selectByPrimaryKey(item.getUserId());
                    articleVo.setAuthor(user.getUsername());
                    articleVo.setHeadImage(item.getHeadImage());

                    articleVo.setCategoryId(String.valueOf(item.getCategoryId()));
                    Category category = categoryMapper.selectByPrimaryKey(item.getCategoryId());
                    articleVo.setCategoryName(category.getName());
                    articleVo.setWatchCount(item.getWatchCount());
                    if (item.getContent().length() > 100){
                        articleVo.setContent(StringUtils.substring(item.getContent(),0,100));
                    }else {
                        articleVo.setContent(item.getContent());
                    }
                    List<Label> labels = labelService.getLabelByArticleId(item.getId());
                    articleVo.setLabels(labels);

                    return articleVo;
                })
                .collect(Collectors.toList());
        return articleVoList;
    }
}
