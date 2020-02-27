package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.statistics.WeekData;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.mapper.ArticleMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.StatisticService;
import com.lingxiao.blog.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;



    public ResponseResult<Map<String, Object>> getArticleWeekIncreased(){
        List<WeekData> weekData = articleMapper.weekIncreased();
        Map<String, Object> map = new HashMap<>();
        List<String> days = getDays(7);
        map.put("xAxis",days);
        int[] series = new int[days.size()];

        if (CollectionUtils.isEmpty(weekData)){

        }
        for (int i = 0; i < days.size(); i++) {
            for (int j = 0; j < weekData.size(); j++) {
                WeekData data = weekData.get(j);
                if (days.get(i).equals(data.getTime())){
                    series[i] = data.getCount();
                    break;
                }
            }
        }
        map.put("yAxis",getYData());
        map.put("series",series);
        Integer todayIncreased = userMapper.todayIncreased();
        map.put("todayIncreased", todayIncreased);
        //PageResult<WeekData> result = new PageResult<>(days.size(),1,days);
        ResponseResult result = new ResponseResult<Map<String, Object>>(map);
        return result;
    }

    private List<String> getDays(int intervals) {
        List<String> pastDaysList = new ArrayList<>();
        for (int i = intervals -1; i >= 0; i--) {
            pastDaysList.add(DateUtil.getPastDate(i));
        }
        return pastDaysList;
    }


    private List<Integer> getYData() {
        List<Integer> data = new ArrayList<>();
        data.add(0);
        data.add(10);
        data.add(15);
        data.add(20);
        data.add(25);
        data.add(30);
        return data;
    }
}
