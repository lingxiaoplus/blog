package com.lingxiao.blog.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Table(name = "user")
@Data
public class User {
    @Id
    private Long userId;
    private Long userIp;
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotEmpty(message = "邮箱不能为空")
    private String email;
    private String headPortrait;
    private Integer age;
    @NotEmpty(message = "电话号码不能为空")
    private String phoneNumber;
    @NotEmpty(message = "昵称不能为空")
    private String nickname;
    private Integer status;

    private Date birthday;
    private Date createAt;
    private Date updateAt;

}
