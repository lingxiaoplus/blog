package com.lingxiao.blog.service.article.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.lingxiao.blog.bean.*;
import com.lingxiao.blog.bean.po.*;
import com.lingxiao.blog.bean.vo.HomePageVo;
import com.lingxiao.blog.enums.ArticleStatusEnum;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.mapper.*;
import com.lingxiao.blog.service.article.ArticleService;
import com.lingxiao.blog.service.article.LabelService;
import com.lingxiao.blog.service.system.DictionaryService;
import com.lingxiao.blog.service.system.ThemeService;
import com.lingxiao.blog.utils.BeanUtil;
import com.lingxiao.blog.utils.RedisUtil;
import com.lingxiao.blog.utils.SecurityUtil;
import com.lingxiao.blog.utils.UIDUtil;
import com.lingxiao.blog.bean.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleLabelMapper articleLabelMapper;
    @Autowired
    private LabelService labelService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private RedisUtil redisUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long addArticle(Article article) {
        if (null != article.getId()){
            //更新文章
            updateArticle(article);
            return article.getId();
        }

        long id = UIDUtil.nextId();
        article.setId(id);
        article.setCreateAt(new Date());
        article.setUpdateAt(article.getCreateAt());

        User user = SecurityUtil.getCurrentUser();
        article.setUserId(user.getUserId());
        article.setAuthor(user.getUsername());
        Category category = categoryMapper.selectByPrimaryKey(article.getCategoryId());
        article.setCategoryName(category.getName());

        int count = articleMapper.insertSelective(article);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.ARTICLE_INSERT_ERROR);
        }
        if (!CollectionUtils.isEmpty(article.getLabelIds())){
            addLabels(article.getId(),article.getLabelIds());
        }
        log.info("文章，{}", article);
        redisUtil.delRedis(RedisConstants.KEY_FRONT_STATTICS_ARTICLE_INCREASE);
        return id;
    }

    private void addLabels(long aId, List<Long> ids){
        List<ArticleLabel> labels = ids.stream().map(labelId -> {
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

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateArticle(Article article) {
        article.setCreateAt(null);
        article.setUserId(null);
        article.setUpdateAt(new Date());
        int count = articleMapper.updateByPrimaryKeySelective(article);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.ARTICLE_UPDATE_ERROR);
        }
        List<Long> labelIds = article.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            labelService.updateArticleLabelByArticleId(article.getId(),labelIds);
        }
    }


    @Override
    public ArticleVo getArticleContent(Long id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        if (article == null){
            throw new BlogException(ExceptionEnum.ARTICLE_SELECT_ERROR);
        }
        article.setWatchCount(article.getWatchCount()+1);
        articleMapper.updateByPrimaryKeySelective(article);
        return articleConvert(article);
    }


    @Override
    public PageResult<ArticleVo> getArticles(String keyword, @Nullable Integer status, int pageNum, int pageSize) {
        PageMethod.startPage(pageNum,pageSize,"create_at desc");
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        if (status != null){
            criteria.andEqualTo("status",status);
        }
        if (StringUtils.isNotBlank(keyword)){
            criteria.andLike("title","%"+keyword+"%");
        }
        List<Article> articles = articleMapper.selectByExample(example);
        PageInfo<Article> pageInfo = PageInfo.of(articles);
        List<ArticleVo> articleVoList = articleListConvert(pageInfo.getList());
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), articleVoList);
    }

    @Override
    public List<Article> getRankArticle(int size){
        return articleMapper.selectRankArticles(size);
    }

    @Override
    public List<ArticleVo> getTimeLineArticle(Date date){
        List<ArticleVo> result = redisUtil.getListByKey(String.format(RedisConstants.KEY_FRONT_ARTICLE_TIMELINE_YEAR,date.getTime()));
        if (!CollectionUtils.isEmpty(result)){
            return result;
        }
        List<Article> articles = articleMapper.selectYearArticles(date);
        result = articleListConvert(articles);
        redisUtil.rightPushAll(String.format(RedisConstants.KEY_FRONT_ARTICLE_TIMELINE_YEAR,date.getTime()),result, TimeUnit.DAYS.toMillis(1));
        return result;
    }

    @Cacheable(value = "banners")
    @Override
    public ResponseResult<HomePageVo> getHomePageBanner(int bannerSize){
        List<Article> articles = getRankArticle(bannerSize);
        List<ArticleVo> banners = articleListConvert(articles);
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

    private List<ArticleVo> articleListConvert(List<Article> articles){
        return articles.stream().map(item ->{
            ArticleVo articleVo = articleConvert(item);
            String content = stringFilter(item.getContent());
            articleVo.setLabels(labelService.getLabelByArticleId(item.getId()));
            //缩略字符串
            articleVo.setContent(content);
            return articleVo;
        }).collect(Collectors.toList());
    }

    private ArticleVo articleConvert(Article article){
        //设置状态
        Dictionary dictionary = new Dictionary();
        ArticleStatusEnum statusEnum = ArticleStatusEnum.get(article.getStatus());
        dictionary.setCode(String.valueOf(statusEnum.getCode()));
        dictionary.setName(statusEnum.getName());
        ArticleVo articleVo = BeanUtil.map(ArticleVo.class, article);
        articleVo.setStatus(dictionary);
        return articleVo;
    }

    private static String stringFilter (String str){
        String regEx="[`~!@#$%^&*()+=|{}\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘”“’]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String trim = m.replaceAll("").trim();
        return StringUtils.abbreviate(trim,200);
    }
}
