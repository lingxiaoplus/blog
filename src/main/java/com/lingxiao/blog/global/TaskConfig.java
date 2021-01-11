package com.lingxiao.blog.global;

import org.quartz.Scheduler;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义一个异步执行线程池
 */
@Configuration
public class TaskConfig {
    @Bean("taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(10);
        threadPoolExecutor.setMaxPoolSize(20);
        threadPoolExecutor.setQueueCapacity(100);
        threadPoolExecutor.setKeepAliveSeconds(60);  //允许线程的空闲时间60秒：当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        // 线程池名的前缀：设置好了之后可以方便定位处理任务所在的线程池
        threadPoolExecutor.setThreadNamePrefix("taskExecutor-");
         /*
        线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，
        当线程池没有处理能力的时候，该策略会直接在execute方法的调用线程中运行被拒绝的任务；
        如果执行程序已关闭，则会丢弃该任务
         */
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        threadPoolExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        threadPoolExecutor.setAwaitTerminationSeconds(600);
        return threadPoolExecutor;
    }

    @Bean(name = "SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setAutoStartup(true);
        //延时5秒启动
        factory.setStartupDelay(5);
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        //这个东西如果不配置的话，quartz有自己默认的配置文件
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * 通过SchedulerFactoryBean获取Scheduler
     * @return
     * @throws IOException
     */
    @Bean(name = "Scheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }
}
