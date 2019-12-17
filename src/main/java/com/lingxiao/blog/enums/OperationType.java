package com.lingxiao.blog.enums;

public enum OperationType {
    /**
     * 操作类型
     */
    UNKNOWN(5,"未知"),
    DELETE(4,"删除"),
    SELECT(3,"查询"),
    UPDATE(2,"更新"),
    INSERT(1,"增加"),
    LOGIN(0,"登录")
    ;
    private int code;
    private String value;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    OperationType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    OperationType(int code) {
        this.code = code;
    }
}
