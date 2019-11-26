package com.lingxiao.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    ILLEGA_ARGUMENT(500,"传递的参数不正确"),
    UPDATE_USER_ERROR(500,"更新用户信息失败"),
    REGISTER_EMAIL_ERROR(500,"邮箱已被注册，请更换邮箱"),
    REGISTER_USERNAME_ERROR(500,"用户名已被注册，请更换用户名"),
    REGISTER_PHONE_ERROR(500,"手机已被注册，请更换手机"),
    LOGIN_NAME_ERROR(500,"登录失败，请检查用户名是否正确"),
    LOGIN_PASSWORD_ERROR(500,"登录失败，密码错误"),
    VERIFY_USER_LOGIN_ERROR(500,"用户未登录"),
    DELETE_USER_ERROR(500,"删除用户失败");
    private int code;
    private String msg;
}
