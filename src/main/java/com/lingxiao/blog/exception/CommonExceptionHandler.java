package com.lingxiao.blog.exception;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.lingxiao.blog.enums.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Type;
import java.util.List;

@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ExceptionResult> handleException(BlogException e){
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResult> handleValidException(MethodArgumentNotValidException e){
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder stringBuilder = new StringBuilder();
        allErrors.forEach((error)->{
            stringBuilder.append(error.getDefaultMessage()).append(", ");
        });
        ExceptionResult exceptionResult = new ExceptionResult(ExceptionEnum.ILLEGA_ARGUMENT.getCode(),
                stringBuilder.toString());
        return ResponseEntity.status(200).body(exceptionResult);
    }

    /**
     * 处理所有不可知异常
     * @param e
     * @return json
     */
    @ExceptionHandler(Exception.class)  //捕获的异常类型
    @ResponseBody
    public ResponseEntity<ExceptionResult> handlerException(Exception e){
        ExceptionResult apiResult = new ExceptionResult(500, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResult);
    }
}
