package com.lingxiao.blog.global;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author renml
 * @date 2021/6/14 2:59 下午
 */
@Component
public class SpringContextAware implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    @SuppressWarnings("static-access")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static String getMessage(String key){
        return applicationContext.getMessage(key, null, Locale.getDefault());
    }
}
