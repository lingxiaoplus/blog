package com.lingxiao.blog.exception;

import com.lingxiao.blog.enums.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ExceptionResult> handleException(BlogException e){
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResult> handleValidException(MethodArgumentNotValidException e){
        ExceptionResult exceptionResult = new ExceptionResult(ExceptionEnum.ILLEGA_ARGUMENT.getCode(),
                "");
        return ResponseEntity.status(200).body(exceptionResult);
    }
}
