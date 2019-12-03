package com.lingxiao.blog.global.security.bean;

import com.lingxiao.blog.bean.Role;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.global.ContentValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义用户身份信息
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 3981518947978158945L;
    /**
     * 用户信息
     */
    private User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return user.getStatus() == ContentValue.USERTYPE_ENABLE;
    }
}
