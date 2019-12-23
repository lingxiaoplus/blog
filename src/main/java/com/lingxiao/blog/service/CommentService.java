package com.lingxiao.blog.service;


import com.lingxiao.blog.bean.Comment;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.global.api.PageResult;

public interface CommentService {
    PageResult<CommentVo> getComments(String keyword, int pageNum, int pageSize);
    int getCommentCount(long articleId);
}
