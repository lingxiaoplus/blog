package com.lingxiao.blog.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lingxiao.blog.bean.po.*;
import com.lingxiao.blog.bean.statistics.AggregationData;
import com.lingxiao.blog.bean.statistics.LineChartData;
import com.lingxiao.blog.bean.statistics.WeekData;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.mapper.*;
import com.lingxiao.blog.service.system.StatisticService;
import com.lingxiao.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 */
@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private FriendLinkMapper linkMapper;
    @Autowired
    private VisitAnalyseMapper visitAnalyseMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public ResponseResult<Object> getArticleWeekIncreased(){
        JSONObject jsonObject = redisUtil.getValueByKey(RedisConstants.KEY_FRONT_STATTICS_ARTICLE_INCREASE);
        if (jsonObject != null){
            return new ResponseResult<>(jsonObject);
        }
        List<WeekData> weekData = articleMapper.weekIncreased();
        jsonObject = new JSONObject();
        LineChartData increasedData = transformWeekData(weekData);
        jsonObject.put("articleIncreasedData",increasedData);
        getTotalNumber(jsonObject);
        redisUtil.pushValue(RedisConstants.KEY_FRONT_STATTICS_ARTICLE_INCREASE, jsonObject, TimeUnit.HOURS.toMillis(4));
        return new ResponseResult<>(jsonObject);
    }

    private LineChartData transformWeekData(List<WeekData> weekDataList){
        LineChartData increasedData = new LineChartData();
        int[] series = increasedData.getSeries();
        List<String> weekXAxis = increasedData.getXAxis();
        if (!CollectionUtils.isEmpty(weekDataList)){
            for (int i = 0; i < weekXAxis.size(); i++) {
                for (int j = 0; j < weekDataList.size(); j++) {
                    WeekData data = weekDataList.get(j);
                    if (weekXAxis.get(i).equals(data.getTime())){
                        series[i] = data.getCount();
                        break;
                    }
                }
            }
        }
        return increasedData;
    }

    private void getTotalNumber(JSONObject jsonObject){
        int todayIncreased = userMapper.todayIncreased();
        int articleSize = articleMapper.selectCount(new Article());
        int commentSize = commentMapper.selectCount(new Comment());
        int categorySize = categoryMapper.selectCount(new Category());
        int labelSize = labelMapper.selectCount(new Label());
        int linkSize = linkMapper.selectCount(new FriendLink());

        AggregationData aggregationData = new AggregationData();
        aggregationData.setTodayIncreased(todayIncreased);
        aggregationData.setArticleSize(articleSize);
        aggregationData.setCommentSize(commentSize);
        aggregationData.setCategorySize(categorySize);
        aggregationData.setLabelSize(labelSize);
        aggregationData.setLinkSize(linkSize);
        jsonObject.put("aggregationData", aggregationData);
    }

    @Override
    public ResponseResult<Object> getOperatorDistributed(){
        JSONObject cachedData = redisUtil.getValueByKey(RedisConstants.KEY_FRONT_STATTICS_VISIT_ANALYSE);
        if (cachedData != null){
            return new ResponseResult<>(cachedData);
        }
        JSONObject jsonObject = new JSONObject();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        getIpVisitAnalayse(jsonObject,countDownLatch);
        getOperatorsAnalyse(jsonObject,countDownLatch);
        getProvinceAnalyse(jsonObject,countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisUtil.pushValue(RedisConstants.KEY_FRONT_STATTICS_VISIT_ANALYSE,jsonObject, TimeUnit.HOURS.toMillis(4));
        return new ResponseResult<>(jsonObject);
    }

    private void getOperatorsAnalyse(JSONObject jsonObject,CountDownLatch countDownLatch){
        Executors.newSingleThreadExecutor().execute(()->{
            List<Map<String, Object>> operatorsAnalyse = visitAnalyseMapper.getOperatorsAnalyse();
            long total = operatorsAnalyse.stream().mapToLong(map -> (Long) map.get("count")).sum();
            JSONObject operators = new JSONObject();
            operators.put("total",total);
            operators.put("list",operatorsAnalyse);
            jsonObject.put("operatorAnalyse",operators);
            countDownLatch.countDown();
        });
    }

    private void getIpVisitAnalayse(JSONObject jsonObject,CountDownLatch countDownLatch){
        Executors.newSingleThreadExecutor().execute(()->{
            List<WeekData> weekData = visitAnalyseMapper.getWeekData();
            LineChartData lineChartData = transformWeekData(weekData);
            jsonObject.put("lineCharData",lineChartData);
            countDownLatch.countDown();
        });
    }

    private void getProvinceAnalyse(JSONObject jsonObject,CountDownLatch countDownLatch){
        Executors.newSingleThreadExecutor().execute(()->{
            List<Map<String, Object>> provinceMonthData = visitAnalyseMapper.getProvinceMonthData();
            long total = provinceMonthData.stream().mapToLong(map -> (Long) map.get("count")).sum();
            JSONObject provinceObject = new JSONObject();
            provinceObject.put("total",total);
            provinceObject.put("list",provinceMonthData);
            jsonObject.put("provinceMonthData",provinceObject);
            countDownLatch.countDown();
        });
    }
}
