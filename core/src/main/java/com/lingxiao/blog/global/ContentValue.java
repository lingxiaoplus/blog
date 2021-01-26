package com.lingxiao.blog.global;


import java.util.HashMap;
import java.util.Map;

/**
 * @author lingxiao
 * 常量类
 */
public class ContentValue {
    private ContentValue() {
    }

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

    /**
     * 自定义header必须用-不能用下划线
     */
    public static final String LOGIN_TOKEN_NAME = "blog-login-token";
    public static final String STATISTICS_CACHE_NAME = "statistics_cache";
    public static final int COOKIE_MAXAGE = 30 * 60;


    /**
     * 文章状态
     */
    public static final int ARTICLE_STATUS_NONE = -1;
    /**
     * 文章状态 草稿箱
     */
    public static final int ARTICLE_STATUS_DRAFT = 0;
    /**
     * 文章状态 已发布
     */
    public static final int ARTICLE_STATUS_PUBLISHED = 1;
    /**
     * 文章状态 已删除
     */
    public static final int ARTICLE_STATUS_DELETED = 2;


    public static final int LOG_LOGIN = 0;
    public static final int LOG_OPERATION = 1;


    public static final int EMAIL_DISABLE = 0;
    public static final int EMAIL_ENABLE = 1;

    public static final String HITOKOTO_URL = "https://v1.hitokoto.cn?min_length=16";

    /**
     * 角色tag 管理员
     */
    public static final String USER_TAG_ADMIN = "admin";

    /**
     * 角色tag 普通用户
     */
    public static final String USER_TAG_NORMAL_USER = "user";

    /**
     * 图片分辨率 1080p
     */
    public static final String SUFFIX_IMAGE_1080P = "1920x1080.jpg";
    public static final String SUFFIX_IMAGE_720P = "1280x720.jpg";
    public static final String SUFFIX_IMAGE_480P = "640x480.jpg";
    public static final String SUFFIX_IMAGE_360P = "480x360.jpg";
    public static final String SUFFIX_IMAGE_240P = "320x240.jpg";
    /**
     * bing日图
     */
    public static final String BING_IOLIU_URL = "https://bing.ioliu.cn/";
    /**
     * 浏览器标志
     */
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0";

    public static final Map<String,String> SUFFIX_IMAGE_MAP = new HashMap<>();
    static {
        SUFFIX_IMAGE_MAP.put("1080p",SUFFIX_IMAGE_1080P);
        SUFFIX_IMAGE_MAP.put("720p",SUFFIX_IMAGE_720P);
        SUFFIX_IMAGE_MAP.put("480p",SUFFIX_IMAGE_480P);
        SUFFIX_IMAGE_MAP.put("360p",SUFFIX_IMAGE_360P);
        SUFFIX_IMAGE_MAP.put("240p",SUFFIX_IMAGE_240P);
    }

}
