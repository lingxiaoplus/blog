package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.bean.UserInfo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.UserStatus;
import com.lingxiao.blog.jwt.JwtProperties;
import com.lingxiao.blog.jwt.JwtUtils;
import com.lingxiao.blog.mapper.UserMapper;
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

    @Override
    public User login(String account, String password) {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        return null;
    }

    @Override
    public String register(User user,String ip) {
        user.setCreateAt(new Date());
        user.setStatus(UserStatus.USERTYPE_ENABLE);
        user.setUserId(UIDUtil.nextId());
        user.setUserIp(IPUtils.ipToNum(ip));
        //密码加密
        byte[] saltByte = MD5Util.createSalt();
        String salt = MD5Util.byteToHexString(saltByte);
        String encryptedPwd = MD5Util.getEncryptedPwd(user.getPassword(), saltByte);
        user.setPassword(encryptedPwd);
        user.setSalt(salt);

        int count = userMapper.insertSelective(user);
        if (count != 1){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        String token = authEntication(user);
        return token;
    }

    @Override
    public User verify(String token) {
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            User user = userMapper.selectByPrimaryKey(userInfo.getId());
            return user;
        } catch (Exception e) {
            log.error("解密失败",e);
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
        if (count != 1){
            throw new BlogException(ExceptionEnum.UPDATE_USER_ERROR);
        }
    }

    @Override
    public void deleteUser(long userId) {
        int count = userMapper.deleteByPrimaryKey(userId);
        if (count != 1){
            throw new BlogException(ExceptionEnum.DELETE_USER_ERROR);
        }
    }

    private String authEntication(User user) {
        if (user == null){
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
            String generateToken = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            return generateToken;
        } catch (Exception e) {
            log.error("生成token失败 ",e);
        }
        return null;
    }
}
