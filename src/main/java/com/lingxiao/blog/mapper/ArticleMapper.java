package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface ArticleMapper extends Mapper<Article> {
    @Update("update articles set status=#{status} where id=#{id}")
    int updateArticleStatus(@Param("id") long id, @Param("status") int status);
}
