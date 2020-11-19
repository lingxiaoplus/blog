package com.lingxiao.blog.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Table(name = "menu")
@Data
public class Menu {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long parentId;
    @NotBlank(message = "菜单路径不能为空")
    private String url;
    //@NotBlank(message = "组件名字不能为空")
    private String component;
    @NotBlank(message = "菜单名字不能为空")
    private String name;
    private String icon;
    private Integer keepAlive;
    private Integer sortIndex;
    @DateTimeFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createAt;

    @Transient
    private List<Menu> children;
}
