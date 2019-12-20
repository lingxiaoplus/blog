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
    DELETE_USER_ERROR(500,"删除用户失败"),
    MENU_ADD_ERROR(500,"添加菜单失败"),
    MENU_DELETE_ERROR(500,"删除菜单失败"),
    MENU_UPDATE_ERROR(500,"更新菜单失败"),
    MENU_SELECT_ERROR(404,"查询菜单失败"),
    ROLE_SELECT_ERROR(404,"查询角色失败"),
    ACCESS_DENIED_ERROR(403,"权限不足"),
    CATEGORY_DELETE_ERROR(500,"删除分类失败"),
    CATEGORY_SELECT_ERROR(404,"查询分类失败"),
    CATEGORY_UPDATE_ERROR(500,"更新分类失败"),
    CATEGORY_INSERT_ERROR(500,"添加分类失败"),
    LABEL_INSERT_ERROR(500,"添加标签失败"),
    ARTICLE_LABEL_INSERT_ERROR(500,"给文章添加标签失败"),
    LABEL_UPDATE_ERROR(500,"更新标签失败"),
    LABEL_DELETE_ERROR(500,"删除标签失败"),
    ARTICLE_DELETE_ERROR(500,"删除文章失败"),
    ARTICLE_SELECT_ERROR(404,"未找到该文章"),
    ARTICLE_UPDATE_ERROR(400,"更新文章失败"),
    ;
    private int code;
    private String msg;
}
