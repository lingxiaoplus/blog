package com.lingxiao.blog.bean.vo;

import lombok.Data;

@Data
public class CommentVo {
    private String id;
    private String parentId;
    private String userId;
    private String articleId;
    private String likeCount;
    private String createAt;
    private String content;
    private String username;
    private String userIP;
    private String userEmail;
    private String articleTitle;
    private Integer status;

}
