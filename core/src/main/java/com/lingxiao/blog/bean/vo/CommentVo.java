package com.lingxiao.blog.bean.vo;

import lombok.Data;
import java.util.List;

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
    private ArticleDetailVo article;  //文章
    private List<CommentVo> replies;  //这条评论的回复

}
