package com.lingxiao.blog.service;


import com.lingxiao.blog.bean.Comment;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.global.api.PageResult;

public interface CommentService {
    PageResult<CommentVo> getComments(String keyword, int pageNum, int pageSize);

    PageResult<CommentVo> getCommentsByArticleId(int pageNum, int pageSize,long id);
    int getCommentCount(long articleId);
    /**
     * 添加评论
     * @param comment  评论内容
     */
    void addComment(Comment comment);
}
