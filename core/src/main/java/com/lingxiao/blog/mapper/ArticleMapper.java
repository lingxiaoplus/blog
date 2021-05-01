package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.Article;
import com.lingxiao.blog.bean.statistics.WeekData;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface ArticleMapper extends Mapper<Article> {
    /**
     * 更新文章状态
     * @param id 文章id
     * @param status 状态
     * @return
     */
    int updateArticleStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 根据年份获取文章
     * @param date 年份
     * @return
     */
    List<Article> selectYearArticles(@Param("date") Date date);

    /**
     * 获取每周文章的数量
     * @return
     */
    List<WeekData> weekIncreased();

    /**
     * 获取所有文章
     * @param keyword 标题关键字
     * @param status 状态
     * @return
     */
    List<Article> selectArticles(@Param("keyword") String keyword, @Param("status")int status);

    List<Article> selectByCondition(Article article);

    /**
     * 获取点击量最高的文章
     * @param number 获取数量
     * @return
     */
    List<Article> selectRankArticles(int number);

}
