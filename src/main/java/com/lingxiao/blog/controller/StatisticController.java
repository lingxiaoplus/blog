package com.lingxiao.blog.controller;

import com.lingxiao.blog.bean.statistics.WeekData;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/article/week")
    public ResponseEntity<ResponseResult<Map<String, Object>>> getArticleWeekIncreased(){
        return ResponseEntity.ok(statisticService.getArticleWeekIncreased());
    }
}
