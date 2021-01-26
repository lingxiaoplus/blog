package com.lingxiao.blog.service.user;


import com.lingxiao.blog.bean.po.Comment;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.global.api.PageResult;
import java.util.List;
/**
 * @author lingxiao
 */
public interface CommentService {
    PageResult<CommentVo> getComments(String keyword, int pageNum, int pageSize);

    PageResult<CommentVo> getCommentsByArticleId(int pageNum, int pageSize,long id);
    int getCommentCount(long articleId);
    /**
     * 添加评论
     * @param comment  评论内容
     */
    void addComment(Comment comment);


    void deleteComments(List<Long> ids);

    void setCommentStatus(List<Long> ids, Integer status);
}
