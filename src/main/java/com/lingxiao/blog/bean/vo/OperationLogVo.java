package com.lingxiao.blog.bean.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OperationLogVo {
    private Long id;
    private String username;
    private String nickname;
    private String userIp;
    private Long runTakes;
    private String operationType;
    private String operationContent;
    private String createAt;
}
