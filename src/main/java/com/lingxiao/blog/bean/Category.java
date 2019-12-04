package com.lingxiao.blog.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@Table(name = "category")
public class Category {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long parentId;  //父分类（如果是root就是0）
    @NotBlank(message = "分类名字不能为空")
    private String name;
    private String description;

}
