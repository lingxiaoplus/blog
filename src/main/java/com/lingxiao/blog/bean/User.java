package com.lingxiao.blog.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingxiao.blog.global.ContentValue;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "user")
@Data
public class User implements UserDetails {
    @Id
    private Long userId;
    private Long userIp;
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @JsonIgnore
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

    @JsonIgnore
    private String salt; //盐值

    private Date birthday;
    @JsonIgnore
    private Date createAt;
    @JsonIgnore
    private Date updateAt;

    @Transient
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }
        return authorities;
    }

    // 帐户是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 帐户是否被冻结
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //是否启用
        return this.status == ContentValue.USERTYPE_ENABLE;
    }
}
