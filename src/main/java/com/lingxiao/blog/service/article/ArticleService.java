package com.lingxiao.blog.service.article;

import com.lingxiao.blog.bean.Article;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.bean.vo.ArticleDetailVo;
import com.lingxiao.blog.bean.vo.ArticleVo;

import java.util.List;

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

    PageResult<ArticleVo> getArticles(String keyword,int pageNum, int pageSize);
    /**
     * 发布文章
     */
    //void publishArticle();
}
