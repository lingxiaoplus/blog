package com.lingxiao.blog.global.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * security是默认使用form提交表单登录的，我们要定义一个Filter来拦截/login
 * 从json body中提取用户名和密码
 */
@Component
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected AuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    /**
     * 从json body中提取出username和password提交给AuthenticationManager
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //从json中获取username和password
        String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        String username = null, password = null;
        if(StringUtils.hasText(body)) {
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            username = jsonObject.get("account").getAsString();
            password = jsonObject.get("password").getAsString();
        }

        if (username == null)
            username = "";
        if (password == null)
            password = "";
        username = username.trim();
        //封装到token中提交
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        return getAuthenticationManager().authenticate(authRequest);
    }
}
