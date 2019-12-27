package com.lingxiao.blog.bean.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ArticleVo {
    private String id;  //id使用字符串的方式返回给前端  因为js处理会存在精度丢失的问题
    private String title;
    private String author;
    private String categoryId;
    private String categoryName;
    private List<String> lables;
    private String updateTime;
    private String headImage;
}
