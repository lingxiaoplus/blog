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
    VERIFY_USER_LOGIN_ERROR(500,"验证用户是否登录失败"),
    DELETE_USER_ERROR(500,"删除用户失败");
    private int code;
    private String msg;
}
