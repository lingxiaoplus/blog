package com.lingxiao.blog.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.po.IpRegion;
import com.lingxiao.blog.bean.po.OperationLog;
import com.lingxiao.blog.bean.vo.OperationLogVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.IP2RegionMapper;
import com.lingxiao.blog.mapper.LogMapper;
import com.lingxiao.blog.service.system.IP2RegionService;
import com.lingxiao.blog.service.system.OperationLogService;
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
    private IP2RegionService ip2RegionService;

    @Override
    public PageResult<OperationLogVo> getLogList(int pageNum, int pageSize,int operationType, String keyword){
        PageHelper.startPage(pageNum,pageSize);
        List<OperationLog> logList;
        //查除了 登录日志的其他所有
        Example example = new Example(OperationLog.class);
        example.setOrderByClause("create_at desc");
        if(operationType < OperationType.LOGIN.getCode()){
            example
                    .createCriteria()
                    .andNotEqualTo("operationType", OperationType.LOGIN.getCode())
                    .andLike("username",keyword+"%");
            logList = logMapper.selectByExample(example);
        }else {
            example.createCriteria().andEqualTo("operationType",operationType);
            logList = logMapper.selectByExample(example);
        }

        PageInfo<OperationLog> pageInfo = PageInfo.of(logList);
        List<OperationLogVo> logListVo = pageInfo.getList().stream().map(item->{
            OperationLogVo logVo = new OperationLogVo();
            DateTime dateTime = new DateTime(item.getCreateAt());
            String dateString = dateTime.toString("yyyy-MM-dd");
            logVo.setCreateAt(dateString);
            logVo.setId(item.getId());
            logVo.setUsername(item.getUsername());
            logVo.setNickname(item.getNickname());
            logVo.setRunTakes(item.getRunTakes());
            logVo.setOperationContent(item.getOperationContent());
            logVo.setOperationType(ContentValue.LOG_LOGIN == operationType?"登录日志":"操作日志");
            logVo.setBrowser(item.getBrowser());
            logVo.setExceptionInfo(item.getExceptionInfo());
            //没法批量查询，只能单个查
            IpRegion ipRegion = ip2RegionService.selectRegionByIp(item.getUserIp());
            if (ipRegion != null) {
                String address = ipRegion.getCountry() + " " +
                        ipRegion.getProvince() + " " + ipRegion.getCity() + " " + ipRegion.getOperator();
                logVo.setUserIp(address);
            }
            return logVo;
        }).collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),logListVo);
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
