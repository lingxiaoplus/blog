package com.lingxiao.blog.bean.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingxiao.blog.bean.po.Role;
import com.lingxiao.blog.global.ContentValue;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Admin
 */
@Table(name = "user")
@Data
public class User implements UserDetails {
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

    private String phoneNumber;
    @NotEmpty(message = "昵称不能为空")
    private String nickname;
    private Integer status;

    /**
     * 盐值
     */
    @JsonIgnore
    private String salt;

    private Date birthday;
    @JsonIgnore
    private Date createAt;
    @JsonIgnore
    private Date updateAt;

    @Transient
    private List<Role> roles;
    @Transient
    private String uId;
    @Transient
    private String uIp;
    @Transient
    private String verifyCode;


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (CollectionUtils.isEmpty(roles)) return authorities;
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleTag()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        //用户是否过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //用户密码是否过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == ContentValue.USERTYPE_ENABLE;
    }
}
