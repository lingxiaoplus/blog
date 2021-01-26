package com.lingxiao.blog.service.article;

import com.lingxiao.blog.bean.po.Article;
import com.lingxiao.blog.bean.vo.HomePageVo;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.bean.vo.ArticleVo;
import com.lingxiao.blog.global.api.ResponseResult;

import java.util.Date;
import java.util.List;

public interface ArticleService {
    /**
     * 添加文章到草稿箱IntoDraft
     */
    long addArticle(Article article);
    void updateArticle(Article article);

    List<Article> getRankArticle(int size);

    List<ArticleVo> getTimeLineArticle(Date date);

    ResponseResult<HomePageVo> getHomePageBanner(int bannerSize);

    void deleteArticle(Long id);

    /**
     * 根据id获取具体的文章内容
     * @param id
     */
    ArticleVo getArticleContent(Long id);

    /**
     * 分页获取文章
     * @param keyword  关键字
     * @param status  文章状态
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult<ArticleVo> getArticles(String keyword, int status,int pageNum, int pageSize);
}
