package com.lingxiao.blog.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="user_role")
@Data
public class UserRole {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long userId;
    private Long roleId;
}
