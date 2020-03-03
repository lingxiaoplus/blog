package com.lingxiao.blog.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "operation_log")
@Data
public class OperationLog {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String username;
    private String nickname;
    private Long userIp;
    private Long runTakes;
    private Integer operationType;
    private String operationContent;
    private Date createAt;
    private String exceptionInfo;
    private String browser;
}
