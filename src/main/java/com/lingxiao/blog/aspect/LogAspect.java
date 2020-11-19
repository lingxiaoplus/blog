package com.lingxiao.blog.aspect;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.po.OperationLog;
import com.lingxiao.blog.bean.po.User;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.system.OperationLogService;
import com.lingxiao.blog.service.user.UserService;
import com.lingxiao.blog.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private OperationLogService logService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    /**
     * 切入点为注解的方式
     */
    @Pointcut("@annotation(com.lingxiao.blog.annotation.OperationLogDetail)")
    public void operationLog(){}

    @Before("operationLog()")
    public void doBefore(JoinPoint joinPoint){
        OperationLogDetail annotation = getOperationLogDetail(joinPoint);
        log.debug("方法执行前,{}",annotation.detail());
    }



    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        long time = System.currentTimeMillis();
        res =  joinPoint.proceed();
        time = System.currentTimeMillis() - time;
        OperationLog operationLog = new OperationLog();
        try {
            //方法执行完成后增加日志
            OperationLogDetail detail = getOperationLogDetail(joinPoint);
            log.debug("方法执行环绕后 ,{}",detail.detail());
            operationLog.setOperationType(detail.operationType().getCode());
            operationLog.setOperationContent(detail.detail());
            operationLog.setRunTakes(time);
            operationLog.setUserIp(IPUtils.ipToNum(IPUtils.getIpAddress(request)));
            operationLog.setBrowser(IPUtils.getBrowserName(request));
            operationLog.setCreateAt(new Date());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //anonymousUser
            if (authentication != null && !ContentValue.ANONYMOUSUSER.equals(authentication.getPrincipal())){
                User user = (User) authentication.getPrincipal();
                if(user != null){
                    operationLog.setUsername(user.getUsername());
                    operationLog.setNickname(user.getNickname());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            logService.setOperationLog(operationLog);
        }
        return res;
    }

    @AfterThrowing(value = "operationLog()", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable){
        OperationLogDetail detail = null;
        OperationLog operationLog = new OperationLog();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!ContentValue.ANONYMOUSUSER.equals(authentication.getPrincipal())){
                User user = (User) authentication.getPrincipal();
                operationLog.setUsername(user.getUsername());
                operationLog.setNickname(user.getNickname());
            }
            detail = getOperationLogDetail(joinPoint);
            operationLog.setOperationType(OperationType.EXCEPTION.getCode());
            operationLog.setOperationContent(throwable.toString());
            operationLog.setUserIp(IPUtils.ipToNum(IPUtils.getIpAddress(request)));
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
            log.debug("方法执行异常。操作：{},异常：{}",detail.detail(),throwable);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            logService.setOperationLog(operationLog);
        }
    }

    @After("operationLog()")
    public void doAfter(JoinPoint joinPoint){
        OperationLogDetail annotation = getOperationLogDetail(joinPoint);
        log.debug("方法执行前,{}",annotation.detail());
    }

    private OperationLogDetail getOperationLogDetail(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLogDetail annotation = signature.getMethod().getAnnotation(OperationLogDetail.class);
        return annotation;
    }

}
