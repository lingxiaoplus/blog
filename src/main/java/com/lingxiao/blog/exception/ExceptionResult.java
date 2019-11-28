package com.lingxiao.blog.exception;

import com.lingxiao.blog.enums.ExceptionEnum;
import lombok.Data;

@Data
public class ExceptionResult {
    private int code;
    private String message;
    private Long timestamp;


    public ExceptionResult(int code,String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public ExceptionResult(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
