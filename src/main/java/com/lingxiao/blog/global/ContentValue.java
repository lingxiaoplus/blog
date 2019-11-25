package com.lingxiao.blog.global;

import lombok.Data;
import lombok.Getter;

public class ContentValue {
    /**
     * 用户状态
     */
    public static final int USERTYPE_ENABLE = 0;
    public static final int USERTYPE_DISABLE = 1;

    /**
     * 登录类型  支持三种登录方式
     */
    public static final int LOGIN_TYPE_NAME = 1;
    public static final int LOGIN_TYPE_EMAIL = 2;
    public static final int LOGIN_TYPE_PHONE = 3;
}
