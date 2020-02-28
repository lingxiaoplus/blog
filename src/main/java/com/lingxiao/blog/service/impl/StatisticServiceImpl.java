package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.*;
import com.lingxiao.blog.bean.statistics.AggregationData;
import com.lingxiao.blog.bean.statistics.ArticleIncreasedData;
import com.lingxiao.blog.bean.statistics.WeekData;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.mapper.*;
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
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private FriendLinkMapper linkMapper;

    public ResponseResult<Map<String, Object>> getArticleWeekIncreased(){
        List<WeekData> weekData = articleMapper.weekIncreased();
        Map<String, Object> map = new HashMap<>();
        ArticleIncreasedData increasedData = new ArticleIncreasedData();
        int[] series = increasedData.getSeries();
        List<String> weekXAxis = increasedData.getXAxis();
        if (!CollectionUtils.isEmpty(weekData)){
            for (int i = 0; i < weekXAxis.size(); i++) {
                for (int j = 0; j < weekData.size(); j++) {
                    WeekData data = weekData.get(j);
                    if (weekXAxis.get(i).equals(data.getTime())){
                        series[i] = data.getCount();
                        break;
                    }
                }
            }
        }

        map.put("articleIncreasedData",increasedData);
        getTotalNumber(map);
        ResponseResult result = new ResponseResult<Map<String, Object>>(map);
        return result;
    }


    private void getTotalNumber(Map<String, Object> map){
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
        map.put("aggregationData",aggregationData);
    }

}
