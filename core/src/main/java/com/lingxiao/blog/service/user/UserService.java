package com.lingxiao.blog.service.user;

import com.lingxiao.blog.bean.po.User;
import com.lingxiao.blog.bean.vo.UserVo;
import com.lingxiao.blog.global.api.PageResult;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
    /**
     * 登录
     * @param account  用户名/手机号/邮箱 三种方式登录
     * @param password
     * @param loginType
     * @return 返回token
     */
    //String login(String account, String password, int loginType);

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

    User getAdminUser();


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

    /**
     * 发送邮件
     * @param receiver 邮箱地址
     */
    void sendEmail(String receiver);

    UserVo getUserVo(User user);

    PageResult<UserVo> getUserList(int pageNum, int pageSize, Long userId);
}
