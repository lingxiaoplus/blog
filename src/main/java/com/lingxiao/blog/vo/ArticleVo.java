package com.lingxiao.blog.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ArticleVo {
    private Long id;
    private String title;
    private String author;
    private String categoryName;
    private List<String> tables;
    private String updateTime;
}
