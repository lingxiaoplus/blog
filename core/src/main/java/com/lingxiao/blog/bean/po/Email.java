package com.lingxiao.blog.bean.po;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@Table(name = "email")
public class Email {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "邮箱不能为空")
    private String authCode;
    private Integer enabled;
    private String host;
    private String protocol;
}
