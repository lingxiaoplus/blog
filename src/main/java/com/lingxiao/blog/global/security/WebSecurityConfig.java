package com.lingxiao.blog.global.security;

import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.security.filter.OptionsRequestFilter;
import com.lingxiao.blog.global.security.filter.UrlMetadataSourceFilter;
import com.lingxiao.blog.global.security.handler.AuthFailHandler;
import com.lingxiao.blog.global.security.handler.AuthSuccessHandler;
import com.lingxiao.blog.global.security.handler.RestAccessDeniedHandler;
import com.lingxiao.blog.global.security.handler.TokenClearLogoutHandler;
import com.lingxiao.blog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

//@EnableGlobalMethodSecurity(prePostEnabled = true) //开启注解 判断用户对某个控制层的方法是否具有访问权限
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthSuccessHandler successHandler;

    @Autowired
    private AuthFailHandler failHandler;

    @Autowired
    private RestAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private TokenClearLogoutHandler tokenClearLogoutHandler;
    @Autowired
    private OptionsRequestFilter optionsRequestFilter;
    @Autowired
    private UrlMetadataSourceFilter metadataSource;
    @Autowired
    private UrlAccessDecisionManager manager;
    @Autowired
    private UserService userService;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**",
                "/index.html","/img/**","/fonts/**","/favicon.ico","/verifyCode",
                "/image/**","/user/register","/user/email/**","/user/verify/**","/front/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(manager);
                        return o;
                    }
                })
                .anyRequest().authenticated()  //默认其它的请求都需要认证，这里一定要添加
                .and()
                .cors()  //支持跨域
                .and()
                .csrf().disable()  //CRSF禁用，因为不使用session
                .sessionManagement().disable()  //禁用session
                .formLogin().disable() //禁用form登录
                //添加header设置，支持跨域和ajax请求
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                new Header("Access-control-Allow-Origin","*"),
                new Header("Access-Control-Expose-Headers", ContentValue.LOGIN_TOKEN_NAME))))
                .and() //拦截OPTIONS请求，直接返回header
                .addFilterAfter(optionsRequestFilter, CorsFilter.class)
                //添加登录filter
                .apply(new LoginConfigure<>()).loginHandler(successHandler,failHandler)
                .and()
                .apply(new JwtRequestConfigure<>()).and()
                //添加token的filter
                //.apply(new JwtLoginConfigurer<>()).tokenValidSuccessHandler(successHandler).permissiveRequestUrls("/logout")
                //使用默认的logoutFilter
                .logout()
//              .logoutUrl("/logout")   //默认就是"/logout"
                .addLogoutHandler(tokenClearLogoutHandler)  //logout时清除token
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()) //logout成功后返回200
                .and()
                .sessionManagement()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
        auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean("daoAuthenticationProvider")
    protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
        //这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userService);
        return daoProvider;
    }
}
