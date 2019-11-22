package com.lingxiao.blog.controller;

import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(name = "/",method = RequestMethod.POST)
    public ResponseEntity<Void> register(@RequestBody @Valid User user, HttpServletRequest request){
        userService.register(user,request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }
}
