package com.lingxiao.blog.global;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingxiao.blog.bean.po.IpRegion;
import com.lingxiao.blog.bean.po.VisitAnalyse;
import com.lingxiao.blog.mapper.VisitAnalyseMapper;
import com.lingxiao.blog.service.system.IP2RegionService;
import com.lingxiao.blog.utils.DateUtil;
import com.lingxiao.blog.utils.IPUtils;
import com.lingxiao.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author renml
 * @date 2020/12/4 15:53
 */
@WebFilter(filterName = "frontStatisticsFilter")
@Slf4j
public class FrontStatisticsFilter implements Filter {
    private static final String URL_PATTERN = "/front";
    private RedisUtil redisUtil;
    private VisitAnalyseMapper visitAnalyseMapper;
    private IP2RegionService ip2RegionService;
    private Cache ipCache;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ipCache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build();
    }

    private void initBean(HttpServletRequest request){
        if (null == redisUtil){
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            redisUtil = (RedisUtil) factory.getBean("redisUtil");
        }
        if (visitAnalyseMapper == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            visitAnalyseMapper = (VisitAnalyseMapper) factory.getBean("visitAnalyseMapper");
        }
        if (ip2RegionService == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            ip2RegionService = (IP2RegionService) factory.getBean("ip2RegionService");
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //redisUtil.incr(String.format(RedisConstants.KEY_FRONT_STATTICS_SITE_COUNT,servletRequest.getRemoteAddr()),1L);

        if (StringUtils.contains(request.getRequestURI(),URL_PATTERN)){
            log.debug("前端过滤器：{}",request.getRequestURI());
            initBean(request);
            String ipAddress = IPUtils.getIpAddress(request);
            String today = DateUtil.getPastDate(0);
            if (ipCache.getIfPresent(ipAddress+today) == null){
                ipCache.put(ipAddress+today,1);
                VisitAnalyse visitAnalyse = new VisitAnalyse();
                visitAnalyse.setDate(new Date());
                visitAnalyse.setIp(IPUtils.ipToNum(ipAddress));
                IpRegion ipRegion = ip2RegionService.selectRegionByIp(visitAnalyse.getIp());
                if (ipRegion != null){
                    visitAnalyse.setProvince(ipRegion.getProvince());
                    visitAnalyse.setOperators(ipRegion.getOperator());
                }
                visitAnalyseMapper.insertSelective(visitAnalyse);
            }
        }
        //执行
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
