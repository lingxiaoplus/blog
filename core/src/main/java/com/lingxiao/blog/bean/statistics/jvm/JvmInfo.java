package com.lingxiao.blog.bean.statistics.jvm;

import lombok.Data;

@Data
public class JvmInfo{
    /**
     * jvm内存总量
     */
    private String total;
    /**
     * jvm已使用内存
     */
    private String used;
    /**
     * jvm剩余内存
     */
    private String acaliable;
    /**
     * jvm内存使用率
     */
    private String usageRate;
    /**
     * java版本
     */
    private String version;
    private JvmHeapInfo jvmHeapInfo;
}