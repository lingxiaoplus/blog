package com.lingxiao.blog.controller;

import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.SystemInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api("获取系统信息")
@RestController
@RequestMapping("/system")
public class SystemInfoController {
    @Autowired
    private SystemInfoService systemInfoService;

    @ApiOperation(value = "获取系统负载")
    @GetMapping("/load")
    public ResponseEntity<ResponseResult<Map<String, Object>>> getSystemLoad(){
        return ResponseEntity.ok(systemInfoService.getSystemLoad());
    }
}
