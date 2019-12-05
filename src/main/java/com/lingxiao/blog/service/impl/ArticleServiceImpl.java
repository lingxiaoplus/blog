package com.lingxiao.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.Article;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.ArticleMapper;
import com.lingxiao.blog.service.ArticleService;
import com.lingxiao.blog.utils.UIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void addArticle(Article article) {
        long id = UIDUtil.nextId();
        article.setId(id);
        article.setCreateAt(new Date());
        int count = articleMapper.insertSelective(article);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.CATEGORY_INSERT_ERROR);
        }
    }

    @Override
    public void updateArticle(Article article) {

    }

    @Override
    public PageResult<Article> getArticles(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Article> articles = articleMapper.selectAll();
        PageInfo<Article> pageInfo = PageInfo.of(articles);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }
}
