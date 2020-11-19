package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.Article;
import com.lingxiao.blog.bean.statistics.WeekData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface ArticleMapper extends Mapper<Article> {
    @Update("update articles set status=#{status} where id=#{id}")
    int updateArticleStatus(@Param("id") long id, @Param("status") int status);

    @Select("select * from `articles` where YEAR(create_at)=YEAR(#{date}) order by create_at desc")
    List<Article> selectYearArticles(@Param("date") Date date);

    List<WeekData> weekIncreased();
}
