package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.User;

public interface UserService {
    /**
     * 登录
     * @param account  用户名/手机号/邮箱 三种方式登录
     * @param password
     * @return
     */
    User login(String account, String password);

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
}
