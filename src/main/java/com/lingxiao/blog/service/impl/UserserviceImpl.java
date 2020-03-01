package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.Email;
import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.bean.UserInfo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.LoginInterceptor;
import com.lingxiao.blog.global.TaskConfig;
import com.lingxiao.blog.jwt.JwtProperties;
import com.lingxiao.blog.jwt.JwtUtils;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.EmailService;
import com.lingxiao.blog.service.UserService;
import com.lingxiao.blog.utils.EmailUtil;
import com.lingxiao.blog.utils.IPUtils;
import com.lingxiao.blog.utils.MD5Util;
import com.lingxiao.blog.utils.UIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Random;

@Service
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class UserserviceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HttpSession httpSession;
    public static final String PREFIX = "email_code: ";
    @Autowired
    private EmailUtil emailUtil;

    @Override
    public String login(String account, String password, int loginType) {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        User user = null;
        switch (loginType) {
            case ContentValue.LOGIN_TYPE_NAME:
                user = userMapper.loginByName(account);
                break;
            case ContentValue.LOGIN_TYPE_EMAIL:
                user = userMapper.loginByEmail(account);
                break;
            case ContentValue.LOGIN_TYPE_PHONE:
                user = userMapper.loginByPhone(account);
                break;
        }
        if (user == null){
            throw new BlogException(ExceptionEnum.LOGIN_NAME_ERROR);
        }
        byte[] salt = MD5Util.hexStringToByte(user.getSalt());
        boolean equals = MD5Util.validPassword(password, user.getPassword(), salt);
        if (!equals){
            throw new BlogException(ExceptionEnum.LOGIN_PASSWORD_ERROR);
        }
        String token = authEntication(user);
        return token;
    }

    @Override
    public String register(User user, String ip) {
        if (userMapper.countByEmail(user.getEmail()) > 0){
            throw new BlogException(ExceptionEnum.REGISTER_EMAIL_ERROR);
        }
        if (userMapper.countByName(user.getUsername()) > 0){
            throw new BlogException(ExceptionEnum.REGISTER_USERNAME_ERROR);
        }
        if (userMapper.countByPhone(user.getUsername()) > 0){
            throw new BlogException(ExceptionEnum.REGISTER_PHONE_ERROR);
        }

        Object attribute = httpSession.getAttribute(PREFIX + user.getEmail());
        if (attribute == null){
            throw new BlogException(ExceptionEnum.INVALID_EMAIL_CODE_ERROR);
        }else {
            String code = (String) attribute;
            if (!code.equals(user.getVerifyCode())){
                throw new BlogException(ExceptionEnum.NOTEQUAL_EMAIL_CODE_ERROR);
            }
        }

        user.setCreateAt(new Date());
        user.setStatus(ContentValue.USERTYPE_ENABLE);
        user.setUserId(UIDUtil.nextId());
        user.setUserIp(IPUtils.ipToNum(ip));
        //密码加密
        byte[] saltByte = MD5Util.createSalt();
        String salt = MD5Util.byteToHexString(saltByte);
        String encryptedPwd = MD5Util.getEncryptedPwd(user.getPassword(), saltByte);
        user.setPassword(encryptedPwd);
        user.setSalt(salt);

        int count = userMapper.insertSelective(user);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        String token = authEntication(user);
        return token;
    }

    @Override
    public User verify(String token) {
        if (StringUtils.isBlank(token)){
            log.error("登录的cookie为空");
            throw new BlogException(ExceptionEnum.VERIFY_USER_LOGIN_ERROR);
        }
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            log.debug("解密得到用户信息: {}",userInfo);
            User user = userMapper.selectByPrimaryKey(userInfo.getId());
            user.setUId(String.valueOf(user.getUserId()));
            user.setUIp(IPUtils.numToIP(user.getUserIp()));
            user.setUserId(null);
            user.setUserIp(null);
            user.setPassword(null);
            return user;
        } catch (Exception e) {
            log.error("解密失败", e);
            throw new BlogException(ExceptionEnum.VERIFY_USER_LOGIN_ERROR);
        }
    }

    @Override
    public void changeUser(User user) {
        user.setUpdateAt(new Date());
        user.setPassword(null);
        user.setUsername(null);
        user.setEmail(null);
        user.setPhoneNumber(null);
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.UPDATE_USER_ERROR);
        }
    }

    @Override
    public void deleteUser(long userId) {
        int count = userMapper.deleteByPrimaryKey(userId);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.DELETE_USER_ERROR);
        }
    }

    @Override
    public void initAdmin() {
        //判断是否有系统管理员, 没有就注册
    }
    @Override
    public String authEntication(User user) {
        if (user == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setPhoneNumber(user.getPhoneNumber());
        userInfo.setEmail(user.getEmail());
        /*userInfo.setNickname(user.getNickname());
        userInfo.setHeadPortrait(user.getHeadPortrait());
        userInfo.setAge(user.getAge());
        userInfo.setStatus(user.getStatus());*/
        try {
            log.debug("加密前的用户信息: {}", userInfo);
            String generateToken = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            LoginInterceptor.setUserInfo(userInfo);
            return generateToken;
        } catch (Exception e) {
            log.error("生成token失败 ", e);
        }
        return null;
    }

    @Override
    public void sendEmail(String receiver){
        EmailUtil.EmailConfigure emailConfigure = new EmailUtil.EmailConfigure();
        Email enableEmail = emailService.getEnableEmail();
        emailConfigure.setSendAddress(enableEmail.getEmail());
        emailConfigure.setReceiveAddress(receiver);
        emailConfigure.setAuthCode(enableEmail.getAuthCode());
        emailConfigure.setTitle("注册博客");
        int minute = 60*60*3;
        emailConfigure.setMinute(minute);
        String randomCode = randomCode();
        StringBuilder builder = new StringBuilder();
        builder.append("您正在注册博客，验证码为：")
                .append(randomCode)
                .append("  ，有效时间为3分钟");
        emailConfigure.setVerifyCode(randomCode);
        //emailConfigure.setContent(builder.toString());
        httpSession.setAttribute(PREFIX + receiver, randomCode);
        httpSession.setMaxInactiveInterval(minute);
        try {
            emailUtil.sendEmail(emailConfigure);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new BlogException(ExceptionEnum.SEND_EMAIL_ERROR);
        }
    }

    private static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
