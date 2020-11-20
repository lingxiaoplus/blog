package com.lingxiao.blog.controller.user;

import com.lingxiao.blog.service.user.RoleService;
import com.lingxiao.blog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author renml
 * @date 2020/11/20 17:24
 */
@Controller
public class AdminRegisterController {
    @RequestMapping("/admin/login")
    public String hello(HttpServletRequest request, @RequestParam(value = "name", defaultValue = "springboot-thymeleaf") String name) {
        request.setAttribute("name", name);
        return "login";
    }
}
