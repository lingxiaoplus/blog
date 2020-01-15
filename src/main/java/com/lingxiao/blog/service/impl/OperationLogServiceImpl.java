package com.lingxiao.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.bean.vo.OperationLogVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.LogMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.OperationLogService;
import com.lingxiao.blog.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<OperationLogVo> getLogList(int pageNum, int pageSize,int operationType){
        PageHelper.startPage(pageNum,pageSize);
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationType(operationType);
        //List<OperationLog> logList = logMapper.select(operationLog);
        List<OperationLog> logList;
        if(operationType < 0){
            //查除了 登录日志的其他所有
            Example example = new Example(OperationLog.class);
            example.createCriteria().andNotEqualTo("operationType",OperationType.LOGIN.getCode());
            logList = logMapper.selectByExample(example);
        }else {
            logList = logMapper.select(operationLog);
        }

        PageInfo<OperationLog> pageInfo = PageInfo.of(logList);
        List<OperationLogVo> logListVo = pageInfo.getList().stream().map((item)->{
            OperationLogVo logVo = new OperationLogVo();
            DateTime dateTime = new DateTime(item.getCreateAt());
            String dateString = dateTime.toString("yyyy-MM-dd");
            logVo.setCreateAt(dateString);
            logVo.setId(item.getId());
            logVo.setUsername(item.getUsername());
            logVo.setNickname(item.getNickname());
            logVo.setUserIp(IPUtils.numToIP(item.getUserIp()));
            logVo.setRunTakes(item.getRunTakes());
            logVo.setOperationContent(item.getOperationContent());
            logVo.setOperationType(ContentValue.LOG_LOGIN == operationType?"登录日志":"操作日志");
            return logVo;
        }).collect(Collectors.toList());



        return new PageResult<OperationLogVo>(pageInfo.getTotal(),pageInfo.getPages(),logListVo);
    }

    @Override
    public void setOperationLog(OperationLog operationLog){
        int count = logMapper.insertSelective(operationLog);
        if (count != 1){
            log.error("记录日志失败,{}", operationLog);
        }
        log.error("记录日志成功,{}", operationLog);
    }
}
