package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.UserStatus;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserserviceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String account, String password) {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        return null;
    }

    @Override
    public void register(User user,String ip) {
        user.setCreateAt(new Date());
        user.setStatus(UserStatus.USERTYPE_ENABLE);
        int count = userMapper.insertSelective(user);
        if (count != 1){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
    }

    @Override
    public User verify(String token) {
        return null;
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
}
