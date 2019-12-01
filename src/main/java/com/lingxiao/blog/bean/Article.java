package com.lingxiao.blog.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "article")
@Data
public class Article {
    @Id
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private Long watchCount;
    private Long commentCount;
    private Date createAt;
    private Date updateAt;
    private Long likeCount;
    private Integer status;
}

