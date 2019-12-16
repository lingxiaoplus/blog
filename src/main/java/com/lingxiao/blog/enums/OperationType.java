package com.lingxiao.blog.enums;

public enum OperationType {
    /**
     * 操作类型
     */
    UNKNOWN("未知"),
    DELETE("删除"),
    SELECT("查询"),
    UPDATE("更新"),
    INSERT("增加"),
    LOGIN("登录")
    ;
    private String value;

    public String getValue() {
        return value;
    }

    OperationType(String value) {
        this.value = value;
    }
}
