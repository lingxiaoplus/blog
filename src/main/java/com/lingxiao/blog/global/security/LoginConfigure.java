package com.lingxiao.blog.global.security;

import com.lingxiao.blog.global.security.filter.AuthenticationFilter;
import com.lingxiao.blog.global.security.handler.LoginFailureHandler;
import com.lingxiao.blog.global.security.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

/**
 * 用户登录相关配置
 * @param <T>
 * @param <B>
 */
public class LoginConfigure<T extends LoginConfigure<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T,B> {

    private AuthenticationFilter filter;

    public LoginConfigure() {
        this.filter = new AuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        //super.configure(http);
        //设置Filter使用的AuthenticationManager,这里取公共的即可
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置失败的Handler
        filter.setAuthenticationFailureHandler(new LoginFailureHandler());
        //不将认证后的context放入session
        filter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        AuthenticationFilter authenticationFilter = postProcess(filter);
        //指定Filter的位置 在logout之后
        http.addFilterAfter(authenticationFilter, LogoutFilter.class);
    }

    public LoginConfigure<T,B> loginSuccessHandler(LoginSuccessHandler successHandler){
        filter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }

}
