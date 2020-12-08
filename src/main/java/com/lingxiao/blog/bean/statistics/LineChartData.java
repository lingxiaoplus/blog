package com.lingxiao.blog.bean.statistics;

import com.lingxiao.blog.utils.DateUtil;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * @author admin
 */
@Data
@Getter
public class LineChartData {
    private List<String> xAxis = DateUtil.getDays(7);
    private int[] yAxis = new int[]{0,5,10,15,20,25,30};
    private int[] series = new int[7];
}
