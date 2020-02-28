package com.lingxiao.blog.bean.statistics;

import lombok.Data;

@Data
public class AggregationData {
    private int todayIncreased;
    private int articleSize;
    private int commentSize;
    private int categorySize;
    private int labelSize;
    private int linkSize;
}
