package com.lingxiao.blog.aspect;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.bean.UserInfo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.LoginInterceptor;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.OperationLogService;
import com.lingxiao.blog.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
        try {
            //方法执行完成后增加日志
            OperationLogDetail detail = getOperationLogDetail(joinPoint);
            log.debug("方法执行环绕后 ,{}",detail.detail());
            UserInfo userInfo = LoginInterceptor.getUserInfo();
            User user = userMapper.selectByPrimaryKey(userInfo.getId());
            OperationLog operationLog = new OperationLog();
            operationLog.setUsername(user.getUsername());
            operationLog.setNickname(user.getNickname());
            operationLog.setOperationType(detail.operationType().getCode());
            operationLog.setOperationContent(detail.detail());
            operationLog.setRunTakes(time);
            operationLog.setUserIp(IPUtils.ipToNum(IPUtils.getIpAddress(request)));
            operationLog.setBrowser(IPUtils.getBrowserName(request));
            operationLog.setCreateAt(new Date());
            logService.setOperationLog(operationLog);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    @AfterThrowing(value = "operationLog()", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable){
        OperationLogDetail detail = null;
        try {
            UserInfo userInfo = LoginInterceptor.getUserInfo();
            User user = userMapper.selectByPrimaryKey(userInfo.getId());
            detail = getOperationLogDetail(joinPoint);
            OperationLog operationLog = new OperationLog();
            operationLog.setUsername(user.getUsername());
            operationLog.setNickname(user.getNickname());
            operationLog.setOperationType(OperationType.EXCEPTION.getCode());
            operationLog.setOperationContent(detail.detail());
            operationLog.setUserIp(IPUtils.ipToNum(IPUtils.getIpAddress(request)));
            operationLog.setCreateAt(new Date());
            operationLog.setExceptionInfo(throwable.toString());
            operationLog.setBrowser(IPUtils.getBrowserName(request));
            logService.setOperationLog(operationLog);
            log.debug("方法执行异常。操作：{},异常：{}",detail.detail(),throwable);
        } catch (Exception e) {
            e.printStackTrace();
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
