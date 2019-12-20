package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.ArticleLabel;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface ArticleLabelMapper extends Mapper<ArticleLabel>, IdListMapper<ArticleLabel,Long>, InsertListMapper<ArticleLabel> {

}
