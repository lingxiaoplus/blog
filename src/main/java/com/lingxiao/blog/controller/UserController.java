package com.lingxiao.blog.controller;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.UserService;
import com.lingxiao.blog.utils.CookieUtils;
import com.lingxiao.blog.utils.IPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Api("用户管理接口")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册，注册成功返回token",notes = "注册")
    @ApiImplicitParam(name = "user",value = "user对象")
    @PostMapping(value = "/register")
    @OperationLogDetail(detail = "用户注册",operationType = OperationType.LOGIN)
    public ResponseEntity<String> register(@RequestBody @Valid User user,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        String token = userService.register(user,IPUtils.getIpAddress(request));
        CookieUtils.setCookie(request,response, ContentValue.LOGIN_TOKEN_NAME,token,ContentValue.COOKIE_MAXAGE);
        return ResponseEntity.ok(token);
    }

    @ApiOperation(value = "用户登录，登录成功返回token",notes = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginType",value = "登录类型(1: 用户名，2: 邮箱，3: 电话)")
    })
    @OperationLogDetail(detail = "用户登录",operationType = OperationType.LOGIN)
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(
            @RequestParam(name = "account") String account,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "loginType",defaultValue = "1") int loginType,
            HttpServletRequest request,
            HttpServletResponse response){
        String token = userService.login(account, password, loginType);
        CookieUtils.setCookie(request,response, ContentValue.LOGIN_TOKEN_NAME,token,ContentValue.COOKIE_MAXAGE);
        return ResponseEntity.ok(token);
    }

    @ApiOperation(value = "验证用户是否登录，返回用户信息",notes = "登录状态")
    @GetMapping(value = "/verify")
    @OperationLogDetail(detail = "验证用户是否登录",operationType = OperationType.LOGIN)
    public ResponseEntity<ResponseResult<User>> verify(
            @CookieValue(value = ContentValue.LOGIN_TOKEN_NAME,required = false) String cookieToken,
            @RequestParam(name = "token",required = false) String token,
            HttpServletRequest request,
            HttpServletResponse response
            ){
        if (!StringUtils.isBlank(cookieToken)){
            token = cookieToken;
        }
        User user = userService.verify(token);
        CookieUtils.setCookie(request,response, ContentValue.LOGIN_TOKEN_NAME,token,ContentValue.COOKIE_MAXAGE);
        ResponseResult<User> result = new ResponseResult<>(user);
        return ResponseEntity.ok(result);
    }
}
