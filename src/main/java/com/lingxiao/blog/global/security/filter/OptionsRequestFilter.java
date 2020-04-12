package com.lingxiao.blog.global.security.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OncePerRequestFilter过滤器保证一次请求只调用一次doFilterInternal方法;如内部的forward不会再多执行一次
 */
@Component
public class OptionsRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getMethod().equals("OPTIONS")) {
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setHeader("Access-Control-Allow-Headers", response.getHeader("Access-Control-Request-Headers"));
			return;
		}
		filterChain.doFilter(request, response);
	}

}