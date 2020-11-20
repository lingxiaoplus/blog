package com.lingxiao.blog.service.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.*;
import com.lingxiao.blog.bean.po.Email;
import com.lingxiao.blog.bean.po.Role;
import com.lingxiao.blog.bean.po.User;
import com.lingxiao.blog.bean.po.UserRole;
import com.lingxiao.blog.bean.vo.UserVo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.jwt.JwtProperties;
import com.lingxiao.blog.jwt.JwtUtils;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.mapper.UserRoleMapper;
import com.lingxiao.blog.service.system.EmailService;
import com.lingxiao.blog.service.user.RoleService;
import com.lingxiao.blog.service.user.UserService;
import com.lingxiao.blog.utils.EmailUtil;
import com.lingxiao.blog.utils.IPUtils;
import com.lingxiao.blog.utils.UIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class UserServiceImpl implements UserService{
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

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleService roleService;

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

        String encryptedPwd = PasswordEncoderFactories
                .createDelegatingPasswordEncoder()
                .encode(user.getPassword());
        user.setPassword(encryptedPwd);

        int count = userMapper.insertSelective(user);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        return authEntication(user);
    }

    @Override
    public User verify(String token) {
        if (StringUtils.isBlank(token)){
            log.error("登录的cookie为空");
        }
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            log.debug("解密得到用户信息: {}",userInfo);
            User user = userMapper.selectByPrimaryKey(userInfo.getId());
            user.setUId(String.valueOf(user.getUserId()));
            user.setUIp(IPUtils.numToIP(user.getUserIp()));
            return user;
        } catch (Exception e) {
            log.error("解密失败", e);
            throw new BlogException(ExceptionEnum.VERIFY_USER_LOGIN_ERROR);
        }
    }

    @Override
    public UserVo getUserVo(User user){
        UserVo userVo = new UserVo();
        userVo.setUsername(user.getUsername());
        userVo.setUserId(String.valueOf(user.getUserId()));
        userVo.setUserIp(IPUtils.numToIP(user.getUserIp()));
        userVo.setNickname(user.getNickname());
        userVo.setAge(user.getAge());
        userVo.setBirthday(user.getBirthday());
        userVo.setEmail(user.getEmail());
        userVo.setHeadPortrait(user.getHeadPortrait());
        userVo.setPhoneNumber(user.getPhoneNumber());
        userVo.setStatus(user.getStatus());
        userVo.setRoles(user.getRoles());
        return userVo;
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
    public User getAdminUser() {
        Role role = roleService.getRoleByLevel(1);
        UserRole userRole = new UserRole();
        userRole.setRoleId(role.getId());
        UserRole selectOne = userRoleMapper.selectOne(userRole);
        Long userId = selectOne.getUserId();
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void initAdmin() {
        //判断是否有系统管理员, 没有就注册
        if (roleService.haveAdmin()){
            log.info("有系统管理员");
        }else {

        }
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
        try {
            log.debug("加密前的用户信息: {}", userInfo);
            return JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        } catch (Exception e) {
            log.error("生成token失败 ", e);
        }
        return null;
    }


    @Override
    public PageResult<UserVo> getUserList(int pageNum, int pageSize, Long userId) {
        if (userId == null){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        PageHelper.startPage(pageNum,pageSize);
        int maxLevel = userRoleMapper.maxRoleLevel(userId);
        log.debug("用户的角色最大等级：{}",maxLevel);
        List<User> users = userMapper.selectAll();
        PageInfo<User> pageInfo = PageInfo.of(users);
        List<UserVo> userVoList = pageInfo
                .getList()
                .stream()
                .map(item->{
                    List<Role> roles = roleService.getRolesByUser(item.getUserId());
                    item.setRoles(roles);
                    return getUserVo(item);
                })
                .collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), userVoList);
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
        httpSession.setAttribute(PREFIX + receiver, randomCode);
        httpSession.setMaxInactiveInterval(minute);
        try {
            emailUtil.sendEmail(emailConfigure);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new BlogException(ExceptionEnum.SEND_EMAIL_ERROR);
        }
    }

    private static Random random = new Random();
    private static String randomCode() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userMapper.loginByName(username);
        if (user == null){
            throw new BlogException(ExceptionEnum.LOGIN_NAME_ERROR);
        }
        List<Role> roles = roleService.getRolesByUser(user.getUserId());
        user.setRoles(roles);
        return user;
    }
}
