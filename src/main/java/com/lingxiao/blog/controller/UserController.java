package com.lingxiao.blog.controller;

import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.service.UserService;
import com.lingxiao.blog.utils.IPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Api("用户管理接口")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册，注册成功返回token",notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",value = "user对象"),
            @ApiImplicitParam(name = "loginType",value = "登录类型(1: 用户名，2: 邮箱，3: 电话)")
    })
    @RequestMapping(name = "/",method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody @Valid User user,

                                           HttpServletRequest request){
        String token = userService.register(user,IPUtils.getIpAddress(request));
        return ResponseEntity.ok(token);
    }

    @RequestMapping(name = "/",method = RequestMethod.POST)
    public ResponseEntity<String> login(
            @RequestParam(name = "account") String account,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "loginType",defaultValue = "1") int loginType){
        return ResponseEntity.ok().build();
    }
}
