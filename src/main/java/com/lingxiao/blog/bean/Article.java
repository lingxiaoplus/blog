package com.lingxiao.blog.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(name = "article")
@Data
public class Article {
    @Id
    private Long id;
    private Long userId;
    @NotBlank(message = "分类id不能为空")
    private Long categoryId;
    @NotBlank(message = "文章标题不能为空")
    private String title;
    @NotBlank(message = "文章内容不能为空")
    private String content;

    private String headImage;
    private Long watchCount;
    private Long commentCount;
    private Date createAt;
    private Date updateAt;
    private Long likeCount;
    private Integer status;
}

