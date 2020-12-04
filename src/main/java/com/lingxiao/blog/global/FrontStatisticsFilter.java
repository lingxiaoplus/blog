package com.lingxiao.blog.global;

import com.lingxiao.blog.utils.IPUtils;
import com.lingxiao.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @author renml
 * @date 2020/12/4 15:53
 */
@WebFilter(filterName = "frontStatisticsFilter")
@Slf4j
public class FrontStatisticsFilter implements Filter {
    private static final String URL_PATTERN = "/front";
    private RedisUtil redisUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("前端过滤器：init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (null == redisUtil){
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            redisUtil = (RedisUtil) factory.getBean("redisUtil");
        }
        redisUtil.incr(String.format(RedisConstants.KEY_FRONT_STATTICS_SITE_COUNT,servletRequest.getRemoteAddr()),1L);
        if (StringUtils.contains(request.getRequestURI(),URL_PATTERN)){
            log.debug("前端过滤器：{}",request.getRequestURI());
            String ipAddress = IPUtils.getIpAddress(request);
            redisUtil.incr(String.format(RedisConstants.KEY_FRONT_STATTICS_IP_COUNT,ipAddress),1L);
        }
        //执行
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
