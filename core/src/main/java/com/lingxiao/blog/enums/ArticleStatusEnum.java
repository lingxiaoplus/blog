package com.lingxiao.blog.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Admin
 */

public enum ArticleStatusEnum {
    /**
     * 文章状态
     */
    ARTICLE_STATUS_DRAFT(0,"草稿箱"),
    ARTICLE_STATUS_PUBLISHED(1,"已发布"),
    ARTICLE_STATUS_DELETED(2,"已删除"),
    ;
    private final int code;
    private final String name;

    ArticleStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, ArticleStatusEnum> LOOKUP = new HashMap<>(3);
    public static ArticleStatusEnum get(Integer code) {
        return LOOKUP.get(code);
    }

    static {
        for (ArticleStatusEnum channelEnum : EnumSet.allOf(ArticleStatusEnum.class)) {
            LOOKUP.put(channelEnum.code, channelEnum);
        }

    }
}
