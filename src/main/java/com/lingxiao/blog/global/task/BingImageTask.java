package com.lingxiao.blog.global.task;

import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.mapper.BingImageMapper;
import com.lingxiao.blog.service.file.FileService;
import com.lingxiao.blog.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;

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
    @Autowired
    private BingImageMapper imageMapper;
    @Autowired
    private FileService fileService;

    //@Scheduled(cron = "0 0 0 1/1 * ?")
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
        /*List<BingImage> collect = bingImages.getImages().stream().map(image -> {
            BingImage bingImage = new BingImage();
            bingImage.setTitle(image.getCopyright());
            bingImage.setUrl(image.getUrl());
            bingImage.setUrlBase("https://cn.bing.com".concat(image.getUrlbase()));
            bingImage.setHashCode(image.getHsh());
            bingImage.setStartDate(DateUtil.getDateFromString(image.getStartdate()));
            bingImage.setCreateDate(new Date());
            return bingImage;
        }).collect(Collectors.toList());
        int row = imageMapper.insertList(collect);
        if (row != collect.size()){
            log.info("定时任务，插入部分数据");
        }else {
            log.info("定时任务，插入全部数据");
        }*/
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("job execute: {}", jobExecutionContext.getJobDetail().getKey());
        configureTasks();
    }
}
