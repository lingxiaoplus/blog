package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.OperationLog;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.bean.UserInfo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.LoginInterceptor;
import com.lingxiao.blog.jwt.JwtProperties;
import com.lingxiao.blog.jwt.JwtUtils;
import com.lingxiao.blog.mapper.LogMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.OperationLogService;
import com.lingxiao.blog.service.UserService;
import com.lingxiao.blog.utils.IPUtils;
import com.lingxiao.blog.utils.MD5Util;
import com.lingxiao.blog.utils.UIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class UserserviceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private OperationLogService operationLogService;

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
            throw new BlogException(ExceptionEnum.VERIFY_USER_LOGIN_ERROR);
        }
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            log.debug("解密得到用户信息: {}",userInfo);
            User user = userMapper.selectByPrimaryKey(userInfo.getId());
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
            return generateToken;
        } catch (Exception e) {
            log.error("生成token失败 ", e);
        }
        return null;
    }
}
