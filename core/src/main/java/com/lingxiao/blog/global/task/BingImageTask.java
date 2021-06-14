package com.lingxiao.blog.global.task;

import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.SpringContextAware;
import com.lingxiao.blog.mapper.BingImageMapper;
import com.lingxiao.blog.service.file.FileService;
import com.lingxiao.blog.utils.DateUtil;
import com.lingxiao.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renml
 * @date 2020/11/19 16:58
 */
@Configuration
//@EnableScheduling
@Slf4j
public class BingImageTask implements Job {
    private BingImageMapper imageMapper;
    private FileService fileService;
    private RedisUtil redisUtil;

    private void configureTasks() {
        BingImageData bingImages = fileService.getBingImages(0);
        bingImages.getImages().forEach(image -> {
            BingImage bingImage = new BingImage();
            bingImage.setTitle(image.getCopyright());
            bingImage.setUrl(image.getUrl());
            bingImage.setUrlBase("https://cn.bing.com".concat(image.getUrlbase()));
            bingImage.setHashCode(image.getHsh());
            bingImage.setStartDate(DateUtil.getDateFromString(image.getStartdate()));
            bingImage.setCreateDate(new Date());
            imageMapper.insert(bingImage);
        });
        log.info("定时任务，插入数据");
    }
    private void initBeans(){
        redisUtil = (RedisUtil) SpringContextAware.getBean("redisUtil");
        fileService = (FileService) SpringContextAware.getBean("fileServiceImpl");
        imageMapper = (BingImageMapper) SpringContextAware.getBean("bingImageMapper");
    }

    private void startJsoupTask(){
        Integer currentPageObj = redisUtil.getValueByKey(RedisConstants.KEY_BACK_BINGIMAGE_TASK_CURRENTPAGE);
        fileService.getBingImageByJsoup(currentPageObj==null?1:currentPageObj+1);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("job execute: {}", jobExecutionContext.getJobDetail().getKey());
        initBeans();
        //configureTasks();
        startJsoupTask();
    }
}
