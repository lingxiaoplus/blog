package com.lingxiao.blog.global.security.handler;

import com.google.gson.Gson;
import com.lingxiao.blog.exception.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse resp, AccessDeniedException e) throws IOException, ServletException {
      log.error("权限校验失败，用户没有该权限");
        ExceptionResult respBean = new ExceptionResult(HttpStatus.UNAUTHORIZED.value(), "抱歉，您没有访问权限");
        PrintWriter out = resp.getWriter();
        String json = new Gson().toJson(respBean,ExceptionResult.class);
        out.write(json);
        out.flush();
        out.close();
    }
}
