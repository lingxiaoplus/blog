package com.lingxiao.blog.global.security.handler;

import com.google.gson.JsonObject;
import com.lingxiao.blog.global.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 校验用户登录失败
 * 自定义403响应的内容
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",400);
        jsonObject.addProperty("message","登录失败，用户名或密码错误，请重试");
        out.write(jsonObject.toString());
        out.flush();
        out.close();
    }
}
