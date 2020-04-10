package com.lingxiao.blog.global.security.filter;

import com.lingxiao.blog.bean.Menu;
import com.lingxiao.blog.bean.Role;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 通过当前的请求地址，获取该地址需要的用户角色
 */
@Component
public class UrlMetadataSourceFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求地址
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        if ("/login".equals(requestUrl) || "/register".equals(requestUrl)) {
            return null;
        }
        List<Menu> menus = menuService.selectAll();
        for (Menu menu: menus) {
            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
                List<Role> roles = menuService.getRolesByMenu(menu.getId());
                int size = roles.size();
                String[] values = new String[size];
                for (int i = 0; i < size; i++) {
                    values[i] = roles.get(i).getRoleName();
                }
                return SecurityConfig.createList(values);
            }
        }
        //没有匹配到资源，都需要登录访问
        return SecurityConfig.createList(ContentValue.ROLE_LOGIN);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}