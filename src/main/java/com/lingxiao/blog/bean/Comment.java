package com.lingxiao.blog.bean;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "comments")
@Data
public class Comment {
    @Id
    private Long id;
    private Long parentId;
    @NotNull(message = "未指定哪个用户的评论")
    private Long userId;
    @NotNull(message = "未指定是哪篇文章的评论")
    private Long articleId;
    private Long likeCount;
    private Date createAt;
    @NotBlank(message = "评论不能为空")
    private String content;
    private Integer status; //评论状态 -1: 带审核 0：驳回 1：通过 2：删除 3：垃圾消息
}
