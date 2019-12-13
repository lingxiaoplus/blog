package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.Article;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.vo.ArticleDetailVo;
import com.lingxiao.blog.vo.ArticleVo;

public interface ArticleService {
    /**
     * 添加文章到草稿箱IntoDraft
     */
    void addArticle(Article article);
    void updateArticle(Article article);
    void deleteArticle(Long id);

    /**
     * 根据id获取具体的文章内容
     * @param id
     */
    ArticleDetailVo getArticleContent(Long id);

    PageResult<ArticleVo> getArticles(int pageNum, int pageSize);
    /**
     * 发布文章
     */
    //void publishArticle();
}
