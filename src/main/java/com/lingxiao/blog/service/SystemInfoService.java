package com.lingxiao.blog.service;

import com.lingxiao.blog.global.api.ResponseResult;

import java.util.Map;

public interface SystemInfoService {
    /**
     * 系统负载
     * @return
     */
    ResponseResult<Map<String, Object>> getSystemLoad();
}
