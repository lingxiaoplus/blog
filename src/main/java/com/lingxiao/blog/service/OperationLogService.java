package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.bean.vo.OperationLogVo;
import com.lingxiao.blog.global.api.PageResult;

public interface OperationLogService {
    PageResult<OperationLogVo> getLogList(int pageNum, int pageSize, int operationType, String keyword);
    void setOperationLog(OperationLog operationLog);
}
