package com.lingxiao.blog.global.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

public class LoginConfigure<T extends LoginConfigure<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T,B> {
    @Autowired
    private AuthenticationFilter filter;
    @Autowired
    private LoginFailureHandler failureHandler;

    @Override
    public void configure(B http) throws Exception {
        //super.configure(http);
        //设置Filter使用的AuthenticationManager,这里取公共的即可
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置失败的Handler
        filter.setAuthenticationFailureHandler(failureHandler);
        //不将认证后的context放入session
        filter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        AuthenticationFilter authenticationFilter = postProcess(filter);
        //指定Filter的位置
        //http.addFilterAfter(authenticationFilter,);
    }

    public LoginConfigure<T,B> loginSuccessHandler(LoginSuccessHandler successHandler){
        filter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }

}
