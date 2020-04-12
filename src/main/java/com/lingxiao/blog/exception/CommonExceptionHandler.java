package com.lingxiao.blog.exception;


import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.system.OperationLogService;
import com.lingxiao.blog.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {


    @Autowired
    private OperationLogService logService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpServletRequest request;

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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResult);
    }

    /**
     * 处理所有不可知异常
     * @param throwable
     * @return json
     */
    @ExceptionHandler(Throwable.class)  //捕获的异常类型
    @ResponseBody
    public ResponseEntity<ExceptionResult> handlerException(Throwable throwable){
        ExceptionResult apiResult = new ExceptionResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.getMessage());
        throwable.printStackTrace();

        try {
            OperationLog operationLog = new OperationLog();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() != null){
                User user = (User) authentication.getPrincipal();
                operationLog.setUsername(user.getUsername());
                operationLog.setNickname(user.getNickname());
            }
            operationLog.setOperationType(OperationType.EXCEPTION.getCode());
            operationLog.setOperationContent("unknow exception");
            operationLog.setUserIp(IPUtils.ipToNum(IPUtils.getIpAddress2(request)));
            operationLog.setCreateAt(new Date());
            operationLog.setBrowser(IPUtils.getBrowserName(request));
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            String result = writer.toString();
            operationLog.setExceptionInfo(result);
            printWriter.close();
            writer.close();
            logService.setOperationLog(operationLog);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResult);
    }
}
