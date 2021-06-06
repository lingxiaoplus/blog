package com.lingxiao.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author lingxiao
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     * 异常
     */
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
    ARTICLE_INSERT_ERROR(400,"添加文章失败"),
    COMMENT_INSERT_ERROR(500,"添加评论失败"),
    COMMENT_DELETE_ERROR(500,"删除评论失败"),
    COMMENT_UPDATE_ERROR(500,"修改评论状态失败"),
    SEND_EMAIL_ERROR(500,"发送邮箱验证码失败"),
    SELECT_EMAIL_ERROR(404,"查找启用的邮箱失败"),
    ADD_EMAIL_ERROR(500,"添加邮箱失败"),
    DELETE_EMAIL_ERROR(500,"删除邮箱失败"),
    UPDATE_EMAIL_ERROR(500,"更新邮箱失败"),
    INVALID_EMAIL_CODE_ERROR(400,"邮箱验证码失效，请重新发送"),
    NOTEQUAL_EMAIL_CODE_ERROR(500,"邮箱验证码对比失败"),
    SAVE_THEME_ERROR(500,"保存主题信息失败"),
    SAVE_DEFAULT_THEME_ERROR(500,"不能修改默认主题信息，请指定用户id"),
    SELECT_LINK_ERROR(404,"查找友链失败"),
    ADD_LINK_ERROR(500,"添加友链失败"),
    DELETE_LINK_ERROR(500,"删除友链失败"),
    UPDATE_LINK_ERROR(500,"更新友链失败"),
    ROLE_ADD_ERROR(500,"角色添加失败"),
    ROLE_DELETE_ERROR(500,"角色删除失败"),
    ROLE_UPDATE_ERROR(500,"角色修改失败"),
    ROLE_SELECT_ERROR(404,"角色查询失败"),
    JOB_DELETE_ERROR(10001,"删除任务失败"),
    JOB_ADD_ERROR_CLASS_NOT_FOUND(10002,"添加任务失败，类不存在"),
    JOB_ADD_ERROR_JOB_START(10003,"添加任务失败，任务触发失败"),
    IP_REGION_INIT_ERROR(10004,"ip地址库初始化失败"),
    IP_REGION_TRANSFORM_ERROR(10005,"ip地址转换失败"),

    DATA_DELETE_ERROR(500,"删除数据失败"),
    DATA_SELECT_ERROR(404,"查询数据失败"),
    DATA_UPDATE_ERROR(500,"更新数据失败"),
    DATA_INSERT_ERROR(500,"添加数据失败"),

    UPLOAD_FILE_ERROR(11001,"文件上传失败"),
    DELETE_OSS_FILE_ERROR(11002,"删除oss上的文件失败"),
    MOVE_OR_RENAME_OSS_FILE_ERROR(11003,"移动/重命名oss上的文件失败"),

    ROLE_MENU_SECURITY_UPDATE_ERROR(500,"修改角色菜单权限失败"),
    ;
    private int code;
    private String msg;
}
