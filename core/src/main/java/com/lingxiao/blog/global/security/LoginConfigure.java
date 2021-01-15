package com.lingxiao.blog.global.security;

import com.lingxiao.blog.global.security.filter.LoginAuthFilter;
import com.lingxiao.blog.global.security.handler.AuthFailHandler;
import com.lingxiao.blog.global.security.handler.AuthSuccessHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

public class LoginConfigure<T extends LoginConfigure<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T,B> {
    private LoginAuthFilter filter;
    public LoginConfigure() {
        this.filter = new LoginAuthFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        //super.configure(http);
        //设置Filter使用的AuthenticationManager,这里取公共的即可
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //不将认证后的context放入session
        filter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        LoginAuthFilter authenticationFilter = postProcess(filter);
        //指定Filter的位置 在logout之前
        http.addFilterBefore(authenticationFilter, LogoutFilter.class);
    }

    public LoginConfigure<T,B> loginHandler(AuthSuccessHandler successHandler, AuthFailHandler failHandler){
        filter.setAuthenticationSuccessHandler(successHandler);
        //设置失败的Handler
        filter.setAuthenticationFailureHandler(failHandler);
        return this;
    }

}
