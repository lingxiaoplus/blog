package com.lingxiao.blog.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuartzBean {

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 任务描述
     */
    private String description;
    /**
     * 执行类
     */
    @NotBlank(message = "执行类名不能为空")
    private String jobClassName;
    /**
     * 执行时间
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;
    private String triggerName;
    /**
     * 任务状态
     */
    @NotBlank(message = "任务状态不能为空")
    private String triggerState;

    /**
     * 任务名称 用于修改
     */
    private String oldJobName;
    /**
     * 任务分组 用于修改
     */
    private String oldJobGroup;
}