package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.global.api.PageResult;

public interface OperationLogService {
    PageResult<OperationLog> getLogList(int pageNum, int pageSize);
    void setOperationLog(OperationLog operationLog);

}
