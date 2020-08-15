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

    /**
     * 用户登录之后才能操作的权限
     */
    public static final String ROLE_LOGIN = "ROLE_LOGIN";
    public static final String ANONYMOUSUSER = "anonymousUser";

    public static final String LOGIN_TOKEN_NAME = "blog-login-token";   //自定义header必须用-不能用下划线
    public static final String STATISTICS_CACHE_NAME = "statistics_cache";
    public static final int COOKIE_MAXAGE = 30 * 60;


    /**
     * 文章状态
     */
    public static final int ARTICLE_STATUS_DRAFT = 0; //草稿箱
    public static final int ARTICLE_STATUS_PUBLISHED = 1; //已发布
    public static final int ARTICLE_STATUS_DELETED = 2; //已删除


    public static final int LOG_LOGIN = 0;
    public static final int LOG_OPERATION = 1;


    public static final int EMAIL_DISABLE = 0;
    public static final int EMAIL_ENABLE = 1;

    public static final String HITOKOTO_URL = "https://v1.hitokoto.cn?min_length=16";

}
