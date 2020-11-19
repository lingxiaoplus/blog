package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.Comment;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface CommentMapper extends Mapper<Comment> , IdListMapper<Comment,Long> {

}
