package com.lingxiao.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.bean.UserInfo;
import com.lingxiao.blog.global.LoginInterceptor;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.LogMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.OperationLogService;
import com.lingxiao.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<OperationLog> getLogList(int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<OperationLog> logList = logMapper.selectAll();
        PageInfo<OperationLog> pageInfo = PageInfo.of(logList);
        return new PageResult<OperationLog>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public void setOperationLog(OperationLog operationLog){

        logMapper.insertSelective(operationLog);
        int count = logMapper.insertSelective(operationLog);
        if (count != 1){
            log.error("记录日志失败,{}",operationLog);
        }
    }
}
