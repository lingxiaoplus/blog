package com.lingxiao.blog.bean.statistics.jvm;

import com.lingxiao.blog.bean.statistics.LineChartData;
import com.lingxiao.blog.bean.statistics.Memory;
import lombok.Data;

/**
 * @author Admin
 */
@Data
public class JvmHeapInfo{
    /**
     * 初始化堆大小
     */
    private String initMemory;
    /**
     * 最大堆大小
     */
    private String maxMemory;
    private LineChartData<String> heapLineChartData;
}