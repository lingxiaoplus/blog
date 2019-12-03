package com.lingxiao.blog.global.security.handler;

import com.google.gson.Gson;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.ResponseResult;
import com.lingxiao.blog.global.security.bean.UserDetailsImpl;
import com.lingxiao.blog.service.UserService;
import com.lingxiao.blog.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录成功之后会调用  在这个类里面做jwt
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String generateToken = userService.authEntication(userDetails.getUser());
        //设置cookie 移动端无法设置cookie
        CookieUtils.setCookie(request,response, ContentValue.LOGIN_TOKEN_NAME,generateToken,ContentValue.COOKIE_MAXAGE);
        response.setHeader(ContentValue.LOGIN_TOKEN_NAME,generateToken);

        //登录成功将用户信息返回
        PrintWriter out = response.getWriter();
        ResponseResult result = new ResponseResult(userDetails.getUser());
        String userJson = new Gson().toJson(result, ResponseResult.class);
        out.write(userJson);
        out.flush();
        out.close();
    }
}
