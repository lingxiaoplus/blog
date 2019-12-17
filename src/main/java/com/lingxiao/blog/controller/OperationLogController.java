package com.lingxiao.blog.controller;

import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.bean.vo.OperationLogVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public ResponseEntity<PageResult<OperationLogVo>> getLogList(
                                                               @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                                               @RequestParam(value = "pageSize",defaultValue = "5")int pageSize,
                                                               @RequestParam(value = "logType",defaultValue = "0") int logType){
        PageResult<OperationLogVo> logList = operationLogService.getLogList(pageNum, pageSize, logType);
        return ResponseEntity.ok(logList);
    }

}
