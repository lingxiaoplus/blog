package com.lingxiao.blog.global.security;

import com.lingxiao.blog.global.security.filter.UrlMetadataSourceFilter;
import com.lingxiao.blog.global.security.handler.AuthFailHandler;
import com.lingxiao.blog.global.security.handler.AuthSuccessHandler;
import com.lingxiao.blog.global.security.handler.RestAccessDeniedHandler;
import com.lingxiao.blog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.util.Arrays;

/**
 * 用户权限相关配置
 */
//@Configuration
public class UrlAccessConfigure extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private UrlMetadataSourceFilter metadataSource;
    @Autowired
    private UrlAccessDecisionManager manager;
    @Autowired
    private RestAccessDeniedHandler handler;

    @Autowired
    private AuthSuccessHandler successHandler;
    @Autowired
    private AuthFailHandler failHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);


        http.authorizeRequests()
                .antMatchers("/images").permitAll()  //静态资源无需认证
                .antMatchers("/admin/**").hasAnyRole("ADMIN") //admin开头的请求，需要admin权限
                .antMatchers("/article/**").hasRole("USER");//需登陆才能访问的url

        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(manager);
                        return o;
                    }
                });


        http.authorizeRequests().anyRequest().authenticated()  //默认其它的请求都需要认证，这里一定要添加
                .and()
                .formLogin().disable() //禁用表单
                //允许跨域
                .cors().and().headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                    new Header("Access-control-Allow-Origin","*"),
                    new Header("Access-Control-Expose-Headers","Authorization"))))
                .and() //拦截OPTIONS请求，直接返回header
                //.addFilterAfter(new OptionRequestFilter(), CorsFilter.class)
                //添加登录filter
                .apply(new LoginConfigure<>()).loginHandler(successHandler,failHandler).and()
                .logout().permitAll()
                .and()
                .csrf().disable() //CRSF禁用，因为不使用session
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