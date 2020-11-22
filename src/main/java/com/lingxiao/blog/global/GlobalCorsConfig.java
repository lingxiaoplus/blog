package com.lingxiao.blog.global;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//@Configuration
//@EnableConfigurationProperties(JwtProperties.class)
@Deprecated
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        //1.添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        //.1 允许的域，不要写*
        config.addAllowedOrigin("http://blog.lingxiaomz.top");
        config.addAllowedOrigin("http://www.lingxiaomz.top");
        config.addAllowedOrigin("http://api.lingxiaomz.top");
        config.addAllowedOrigin("https://blog.lingxiaomz.top");
        config.addAllowedOrigin("https://www.lingxiaomz.top");
        config.addAllowedOrigin("https://api.lingxiaomz.top");
        //.2是否发送cookie信息
        config.setAllowCredentials(true);
        //.3允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        //.4允许的头信息
        config.addAllowedHeader("*");
        //.5添加有效时长
        config.setMaxAge(3600L);

        //2.添加映射路径
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",config);

        //3.返回新的CorsFilter
        return new CorsFilter(configurationSource);
    }

    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/article/**","/menu/**","/category/**","/email/**","/system/**");
    }*/
}
