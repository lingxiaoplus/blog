package com.lingxiao.blog.global.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult<T> implements Serializable {
    private int code = 200;
    private String message = "ok";
    private T data;

    public ResponseResult(T data){
        this.data = data;
    }

    public ResponseResult() {
    }
}
