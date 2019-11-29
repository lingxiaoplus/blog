package com.lingxiao.blog.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.global.security.AuthAccessDeniedHandler;
import com.lingxiao.blog.global.security.UrlAccessDecisionManager;
import com.lingxiao.blog.global.security.UrlFilterInvocationSecurityMetadataSource;
import com.lingxiao.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import java.io.PrintWriter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private UrlFilterInvocationSecurityMetadataSource metadataSource;
    @Autowired
    private UrlAccessDecisionManager manager;
    @Autowired
    private AuthAccessDeniedHandler handler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(manager);
                        return o;
                    }
                })
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("account")
                .passwordParameter("password")
                .permitAll()
                .failureHandler((request, response, e) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("{\"status\":\"error\",\"msg\":\"");
                    if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
                        buffer.append("用户名或密码输入错误，登录失败!");
                    } else if (e instanceof DisabledException) {
                        buffer.append("账户被禁用，登录失败，请联系管理员!");
                    } else {
                        buffer.append("登录失败!");
                    }
                    buffer.append("\"}");
                    writer.write(buffer.toString());
                    writer.flush();
                    writer.close();
                }).successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    ObjectMapper objectMapper = new ObjectMapper();
                    User user = (User) authentication.getDetails();
                    String s = "{\"status\":\"success\",\"msg\":" + objectMapper.writeValueAsString(user) + "}";
                    out.write(s);
                    out.flush();
                    out.close();
                })
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(handler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "/static/**");
    }

}
