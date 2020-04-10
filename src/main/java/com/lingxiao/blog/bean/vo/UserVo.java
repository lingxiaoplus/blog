package com.lingxiao.blog.bean.vo;

import com.lingxiao.blog.bean.Role;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserVo {
    private String userId;
    private String userIp;
    private String username;
    private String email;
    private String headPortrait;
    private Integer age;
    private String phoneNumber;
    private String nickname;
    private Integer status;
    private Date birthday;
    private List<Role> roles;

}
