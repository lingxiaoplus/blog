package com.lingxiao.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.Article;
import com.lingxiao.blog.bean.Category;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.bean.UserInfo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.LoginInterceptor;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.ArticleMapper;
import com.lingxiao.blog.mapper.CategoryMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.ArticleService;
import com.lingxiao.blog.utils.UIDUtil;
import com.lingxiao.blog.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void addArticle(Article article) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        long id = UIDUtil.nextId();
        article.setId(id);
        article.setCreateAt(new Date());
        article.setUpdateAt(article.getCreateAt());
        article.setUserId(userInfo.getId());
        int count = articleMapper.insertSelective(article);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.CATEGORY_INSERT_ERROR);
        }
        log.info("文章，{}",article);
    }

    @Override
    public void updateArticle(Article article) {
        article.setUpdateAt(new Date());
    }


    @Override
    public Article getArticleContent(Long id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        if (article == null){
            throw new BlogException(ExceptionEnum.ARTICLE_SELECT_ERROR);
        }
        return article;
    }

    @Override
    public PageResult<ArticleVo> getArticles(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Article> articles = articleMapper.selectAll();
        PageInfo<Article> pageInfo = PageInfo.of(articles);


        List<ArticleVo> articleVoList = pageInfo.getList()
                .stream()
                .filter((item)-> ContentValue.ARTICLE_STATUS_DELETED != item.getStatus())
                .map((item) -> {
                    ArticleVo articleVo = new ArticleVo();
                    articleVo.setId(item.getId());
                    articleVo.setTitle(item.getTitle());
                    DateTime dateTime = new DateTime(item.getUpdateAt());
                    String dateString = dateTime.toString("yyyy-MM-dd");
                    articleVo.setUpdateTime(dateString);
                    User user = userMapper.selectByPrimaryKey(item.getUserId());
                    articleVo.setAuthor(user.getUsername());
                    Category category = categoryMapper.selectByPrimaryKey(item.getCategoryId());
                    articleVo.setCategoryName(category.getName());
                    return articleVo;
                })
                .collect(Collectors.toList());
        return new PageResult<ArticleVo>(pageInfo.getTotal(),pageInfo.getPages(),articleVoList);
    }

    @Override
    public void deleteArticle(Long id) {
        int count = articleMapper.updateArticleStatus(id, ContentValue.ARTICLE_STATUS_DELETED);
        if (count != 1){
            throw new BlogException(ExceptionEnum.ARTICLE_DELETE_ERROR);
        }
    }
}
