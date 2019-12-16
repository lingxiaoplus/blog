package com.lingxiao.blog.exception;

import com.lingxiao.blog.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlogException extends RuntimeException{
    private ExceptionEnum exceptionEnum;

    @Override
    public String toString() {
        return "BlogException{" +
                "exceptionEnum=" + exceptionEnum.getMsg() +
                '}';
    }
}
