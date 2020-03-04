package com.lingxiao.blog.service;

import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.utils.SystemUtil;

import java.util.Map;

public interface SystemInfoService {
    /**
     * 系统负载
     * @return
     */
    ResponseResult<Map<String, Object>> getSystemLoad();

    ResponseResult<SystemUtil.NetworkData> getNetworkState();
}
