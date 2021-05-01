package com.lingxiao.blog.bean.statistics;

import com.lingxiao.blog.utils.DateUtil;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author admin
 */
@Data
public class LineChartData<T> {
    //private List<String> xAxis = DateUtil.getDays(7);
    /**
     * x轴
     */
    private BlockingQueue<String> xAxis;
    /**
     * y轴一般不需要变化
     */
    private int[] yAxis;
    //private int[] yAxis = new int[]{0,5,10,15,20,25,30};
    //private int[] series = new int[7];
    private List<Series<T>> series;
}
