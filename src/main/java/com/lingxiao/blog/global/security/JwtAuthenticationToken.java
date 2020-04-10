package com.lingxiao.blog.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 3981518947978158945L;

    private UserDetails principal;
    private String credentials;

    public JwtAuthenticationToken(UserDetails principal) {
        super(Collections.emptyList());
        this.principal = principal;
    }

    public JwtAuthenticationToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
    }

    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }
}
