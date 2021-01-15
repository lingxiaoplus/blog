package com.lingxiao.blog.controller.system;

import com.lingxiao.blog.bean.QuartzBean;
import com.lingxiao.blog.bean.form.PageQueryForm;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.system.JobService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/job")
@Slf4j
@Api("定时任务管理")
public class JobController {
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;
    @Autowired
    private JobService jobService;

    /**
     * 新增任务
     *
     * @param quartz
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Void> save(@RequestBody @Valid QuartzBean quartz){
        jobService.addSchedule(quartz);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取任务列表
     *
     * @param queryForm
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<PageResult<QuartzBean>> list(PageQueryForm queryForm) {
        return ResponseEntity.ok(jobService.listQuartzBean(queryForm));
    }

    /**
     * 立即执行
     *
     * @param quartz
     * @return
     */
    @PostMapping("/do")
    public ResponseEntity<Void> trigger(@RequestBody @Valid QuartzBean quartz) throws Exception {
        log.info("立即执行");
        JobKey key = new JobKey(quartz.getJobName(), quartz.getJobGroup());
        scheduler.triggerJob(key);
        return ResponseEntity.ok().build();
    }

    /**
     * 暂停任务
     *
     * @param quartz
     * @return
     */
    @PostMapping("/pause")
    public ResponseEntity<Void> pause(@RequestBody @Valid QuartzBean quartz) throws Exception {
        log.info("停止任务");
        JobKey key = new JobKey(quartz.getJobName(), quartz.getJobGroup());
        scheduler.pauseJob(key);
        return ResponseEntity.ok().build();
    }

    /**
     * 从暂停中恢复过来
     *
     * @param quartz
     * @return
     */
    @PostMapping("/recover")
    public ResponseEntity<Void> resume(@RequestBody @Valid QuartzBean quartz) throws Exception {
        log.info("恢复任务");
        JobKey key = new JobKey(quartz.getJobName(), quartz.getJobGroup());
        scheduler.resumeJob(key);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除任务
     *
     * @param quartz
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity<Void> remove(@RequestBody @Valid QuartzBean quartz) throws Exception {
        log.info("删除任务");
        TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), quartz.getJobGroup());
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(quartz.getJobName(), quartz.getJobGroup()));
        log.info("removeJob: {}", JobKey.jobKey(quartz.getJobName()));
        return ResponseEntity.ok().build();
    }
}