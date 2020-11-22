package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.QuartzBean;
import com.lingxiao.blog.bean.form.PageQueryForm;
import com.lingxiao.blog.global.api.PageResult;

import java.util.List;

/**
 * @author Admin
 */
public interface JobService {
    /**
     * 获取任务列表
     * @param name
     * @return
     */
    PageResult<QuartzBean> listQuartzBean(PageQueryForm queryForm);

    /**
     * 添加任务
     * @param quartz
     */
    void addSchedule(QuartzBean quartz);
}
