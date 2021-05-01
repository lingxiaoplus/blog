package com.lingxiao.blog.bean.statistics;

import lombok.Data;

import java.util.List;

/**
 * @author lingxiao
 * 坐标系
 */
@Data
public class Series<T>{
    private String name;
    /**
     * 类型 line
     */
    private String type = "line";

    /**
     * 描述
     */
    private String stack;
    /**
     * 数据
     */
    private List<T> data;

    /**
     * 单位
     */
    private String unit;
}