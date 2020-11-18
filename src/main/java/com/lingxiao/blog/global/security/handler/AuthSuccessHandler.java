package com.lingxiao.blog.global.security.handler;

import com.google.gson.Gson;
import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.bean.vo.UserVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.user.UserService;
import com.lingxiao.blog.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @OperationLogDetail(detail = "用户登录",operationType = OperationType.LOGIN)
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        String generateToken = userService.authEntication(user);
        //设置cookie 移动端无法设置cookie
        CookieUtils.setCookie(request,response, ContentValue.LOGIN_TOKEN_NAME,generateToken,ContentValue.COOKIE_MAXAGE);
        response.setHeader(ContentValue.LOGIN_TOKEN_NAME,generateToken);

        //登录成功将用户信息返回
        PrintWriter out = response.getWriter();
        ResponseResult<UserVo> result = new ResponseResult<>(userService.getUserVo(user));
        String userJson = new Gson().toJson(result, ResponseResult.class);
        out.write(userJson);
        out.flush();
        out.close();
    }
}
