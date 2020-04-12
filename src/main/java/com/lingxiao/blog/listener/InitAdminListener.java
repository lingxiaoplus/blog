package com.lingxiao.blog.listener;

import com.lingxiao.blog.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitAdminListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("spring启动监听");
        userService.initAdmin();
    }
}
