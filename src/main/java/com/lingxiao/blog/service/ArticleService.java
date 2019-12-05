package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.Article;
import com.lingxiao.blog.global.api.PageResult;

public interface ArticleService {
    /**
     * 添加文章到草稿箱IntoDraft
     */
    void addArticle(Article article);
    void updateArticle(Article article);
    PageResult<Article> getArticles(int pageNum, int pageSize);
    /**
     * 发布文章
     */
    //void publishArticle();
}
