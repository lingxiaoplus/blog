package com.lingxiao.blog.bean.po;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Table(name = "role")
@Data
public class Role {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @NotBlank(message = "角色名不能为空")
    private String roleName;
    @NotBlank(message = "角色类型不能为空")
    private String roleTag;
    private String roleDescription;
    private Integer roleLevel;

    @Transient
    private List<Long> menuList;

}
