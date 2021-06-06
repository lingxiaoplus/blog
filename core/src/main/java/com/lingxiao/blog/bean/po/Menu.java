package com.lingxiao.blog.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lingxiao.blog.bean.BaseModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author  renml
 * @date  2021-06-05
 */
@Table(name = "menu")
@Data
public class Menu extends BaseModel {
    //@Id

    //private Long id;
    private Long parentId;
    @NotBlank(message = "菜单路径不能为空")
    private String url;
    private String component;
    @NotBlank(message = "菜单名字不能为空")
    private String name;
    private String icon;
    private Integer keepAlive;
    private Integer sortIndex;

    @Transient
    private List<Menu> children;

    @KeySql(useGeneratedKeys = true)
    @Override
    public Long getId() {
        return super.getId();
    }
}
