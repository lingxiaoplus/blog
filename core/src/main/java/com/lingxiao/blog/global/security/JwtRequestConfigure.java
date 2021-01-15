package com.lingxiao.blog.global.security;

import com.lingxiao.blog.global.security.filter.JwtRequestAuthFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;

public class JwtRequestConfigure<T extends JwtRequestConfigure<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T,B> {

    private JwtRequestAuthFilter authFilter;

    public JwtRequestConfigure() {
        this.authFilter = new JwtRequestAuthFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        //将filter放到logoutFilter之前
        JwtRequestAuthFilter filter = postProcess(authFilter);
        http.addFilterBefore(filter, LogoutFilter.class);
    }
    //设置匿名用户可访问url
    /*public JwtRequestConfigure<T, B> permissiveRequestUrls(String ... urls){
        authFilter.setPermissiveUrl(urls);
        return this;
    }*/

}
