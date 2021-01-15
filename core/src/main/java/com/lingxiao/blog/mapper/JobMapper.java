package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.QuartzBean;

import java.util.List;

public interface JobMapper{
    List<QuartzBean> listQuartzBean(String name);
}
