package com.lingxiao.blog.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.QuartzBean;
import com.lingxiao.blog.bean.form.PageQueryForm;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.JobMapper;
import com.lingxiao.blog.service.system.JobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Admin
 */
@Service
@Slf4j
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;

    @Override
    public PageResult<QuartzBean> listQuartzBean(PageQueryForm queryForm) {
        PageHelper.startPage(queryForm.getPageNum(),queryForm.getPageSize());
        List<QuartzBean> quartzBeans = jobMapper.listQuartzBean(queryForm.getCondition());
        PageInfo<QuartzBean> pageInfo = PageInfo.of(quartzBeans);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public void addSchedule(QuartzBean quartz) {
        log.info("新增任务, {}",quartz.getJobName());
        if (StringUtils.isNoneBlank(quartz.getOldJobGroup())) {
            JobKey key = new JobKey(quartz.getOldJobName(), quartz.getOldJobGroup());
            try {
                scheduler.deleteJob(key);
            } catch (SchedulerException e) {
                log.info("删除原来的任务失败");
                e.printStackTrace();
                throw new BlogException(ExceptionEnum.JOB_DELETE_ERROR);
            }
        }

        JobDetail job = null;
        try {
            //获取.class
            Class cls = Class.forName(quartz.getJobClassName());
            cls.newInstance();
            //创建jobdetail
            job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),
                    quartz.getJobGroup())
                    //设置参数
                    //.usingJobData("aa", "ceshi")
                    //描述
                    .withDescription(quartz.getDescription())
                    .build();
        } catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
            throw new BlogException(ExceptionEnum.JOB_ADD_ERROR_CLASS_NOT_FOUND);
        }
        // 使用cron表达式
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + quartz.getJobName(), quartz.getJobGroup())
                .startNow()
                .withSchedule(cronScheduleBuilder)
                .build();
        try {
            //交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new BlogException(ExceptionEnum.JOB_ADD_ERROR_JOB_START);
        }
    }

}
