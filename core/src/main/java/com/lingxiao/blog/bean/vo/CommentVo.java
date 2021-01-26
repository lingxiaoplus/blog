package com.lingxiao.blog.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * @author lingxiao
 */
@Data
public class CommentVo {
    private String id;
    private String parentId;
    private String createAt;
    private String content;
    private Integer status;

    private String username;
    private String email;
    private String website;

    //private User member;  //这条评论的所属者
    //文章
    private ArticleVo article;
    //这条评论的回复
    private List<CommentVo> replies;

}
