package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    /**
     * 登录
     * @param account  用户名/手机号/邮箱 三种方式登录
     * @param password
     * @param loginType
     * @return 返回token
     */
    String login(String account, String password, int loginType);

    /**
     * 登录成功返回一个token
     * @param user
     * @param ip
     * @return
     */
    String register(User user, String ip);

    /**
     * 通过token验证用户是否登录
     * @param token
     * @return
     */
    User verify(String token);
    void changeUser(User user);
    void deleteUser(long userId);


    /**
     * 生成token
     * @param user
     * @return
     */
    String authEntication(User user);

    /**
     * 初始化系统管理员
     */
    void initAdmin();
}
