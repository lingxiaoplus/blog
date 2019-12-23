package com.lingxiao.blog.bean;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "comments")
@Data
public class Comment {
    @Id
    private Long id;
    private Long parentId;
    private Long userId;
    private Long articleId;
    private Long likeCount;
    private Date createAt;
    private String content;
    private Integer status; //评论状态 0：驳回 1：通过 2：删除 3：垃圾消息
}
