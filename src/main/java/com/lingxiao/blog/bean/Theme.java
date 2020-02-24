package com.lingxiao.blog.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Table(name = "theme")
@Data
public class Theme {
    @Id
    //@NotEmpty(message = "用户id不能为空")
    private Long id;
    private String color;
    private String articleStyle;
    private String homeImage;
    private String slideImage;
    private String motto;
    private String footer;
    private String autoNight;
    private String seoKeyword;
}
