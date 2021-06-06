package com.lingxiao.blog;

import com.lingxiao.blog.listener.InitAdminListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lingxiao
 */
@SpringBootApplication
@EnableAsync
@MapperScan("com.lingxiao.blog.mapper")
@EnableCaching
@ServletComponentScan
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication
                .run(BlogApplication.class, args)
                .addApplicationListener(new InitAdminListener());

    }

}
