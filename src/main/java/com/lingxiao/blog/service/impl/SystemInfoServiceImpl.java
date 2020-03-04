package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.SystemInfoService;
import com.lingxiao.blog.utils.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemInfoServiceImpl implements SystemInfoService {

    @Autowired
    private SystemUtil systemUtil;

    @Override
    public ResponseResult<Map<String, Object>> getSystemLoad() {
        Map<String, Object> map = new HashMap<>();
        map.put("osName",systemUtil.getOsName());
        map.put("osArch",systemUtil.getOsArch());
        SystemUtil.MemoryInfo memInfo = systemUtil.getMemInfo();
        map.put("memoryInfo",memInfo);
        SystemUtil.JvmInfo jvmInfo = systemUtil.getJvmInfo();
        map.put("jvmInfo",jvmInfo);
        try {
            SystemUtil.CpuInfo cpuInfo = systemUtil.getCpuInfo();
            map.put("cpuInfo",cpuInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResponseResult result = new ResponseResult<Map<String, Object>>(map);
        return result;
    }
}
