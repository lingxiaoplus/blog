package com.lingxiao.blog.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "article_label")
public class ArticleLabel {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long articleId;
    private Long labelId;
}
