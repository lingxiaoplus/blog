package com.lingxiao.blog.global.security;

import com.lingxiao.blog.bean.UserInfo;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.jwt.JwtProperties;
import com.lingxiao.blog.jwt.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户除登录之外的请求，都要求必须携带JWT Token
 * 这个Filter对这些请求做一个拦截  提取header中的token
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtRequestAuthFilter extends OncePerRequestFilter {

    private final RequestHeaderRequestMatcher requestHeaderRequestMatcher;
    @Autowired
    private JwtProperties jwtProperties;

    private List<RequestMatcher> permissiveRequestMatchers = new ArrayList<>();

    public JwtRequestAuthFilter() {
        this.requestHeaderRequestMatcher = new RequestHeaderRequestMatcher(ContentValue.LOGIN_TOKEN_NAME);
    }

    protected String getJwtToken(HttpServletRequest request){
        String token = request.getHeader(ContentValue.LOGIN_TOKEN_NAME);
        return token;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //header没带token的，直接放过，因为部分url匿名用户也可以访问
        //如果需要不支持匿名用户的请求没带token，这里放过也没问题，因为SecurityContext中没有认证信息，后面会被权限控制模块拦截
        if (!requiresAuthentication(request)){
            filterChain.doFilter(request,response);
            return;
        }
        Authentication authResult = null;
        AuthenticationException failed = null;
        String token = getJwtToken(request);
        if (StringUtils.isBlank(token)) {
            //如果token的长度是0
            failed = new InsufficientAuthenticationException("JWT is Empty");
        }else {
            try {
                UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
                //authResult = getAuthenticationManager().authenticate(userInfo)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (authResult != null){
            //成功的回调方法
        }else {
            //失败
            return;
        }
        filterChain.doFilter(request,response);
    }

    protected boolean requiresAuthentication(HttpServletRequest request) {
        return requestHeaderRequestMatcher.matches(request);
    }

    protected boolean permissiveRequest(HttpServletRequest request) {
        if(permissiveRequestMatchers == null)
            return false;
        for(RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if(permissiveMatcher.matches(request))
                return true;
        }
        return false;
    }
}
