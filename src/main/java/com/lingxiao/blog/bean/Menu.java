package com.lingxiao.blog.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Table(name = "menu")
@Data
public class Menu {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long parentId;
    @NotBlank(message = "菜单路径不能为空")
    private String url;
    @NotBlank(message = "组件名字不能为空")
    private String component;
    @NotBlank(message = "菜单名字不能为空")
    private String name;
    private String icon;
    private Integer keepAlive;
}
