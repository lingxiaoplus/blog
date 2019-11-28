package com.lingxiao.blog.global;

import lombok.Data;

@Data
public class ResponseResult<T> {
    private int code = 200;
    private String message = "ok";
    private T data;

    public ResponseResult(T data){
        this.data = data;
    }
}
