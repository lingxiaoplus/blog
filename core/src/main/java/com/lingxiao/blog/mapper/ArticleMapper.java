package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.Article;
import com.lingxiao.blog.bean.statistics.WeekData;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface ArticleMapper extends Mapper<Article> {
    int updateArticleStatus(@Param("id") long id, @Param("status") int status);
    List<Article> selectYearArticles(@Param("date") Date date);
    List<WeekData> weekIncreased();

    List<Article> selectArticles();
}
