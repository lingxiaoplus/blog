package com.lingxiao.blog.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ArticleVo {
    private String id;  //id使用字符串的方式返回给前端  因为js处理会存在精度丢失的问题
    private String title;
    private String author;
    private String categoryId;
    private List<String> tables;
    private String updateTime;
}
