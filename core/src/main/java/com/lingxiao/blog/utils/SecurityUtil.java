package com.lingxiao.blog.utils;

import com.lingxiao.blog.bean.po.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Admin
 */
public class SecurityUtil {
    public static User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
