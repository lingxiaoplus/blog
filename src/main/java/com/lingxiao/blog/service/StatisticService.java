package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.statistics.WeekData;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;

import java.util.List;
import java.util.Map;

public interface StatisticService {
    ResponseResult<Map<String, Object>> getArticleWeekIncreased();
}
