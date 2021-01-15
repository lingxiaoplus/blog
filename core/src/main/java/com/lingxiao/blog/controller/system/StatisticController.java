package com.lingxiao.blog.controller.system;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.system.StatisticService;
import com.lingxiao.blog.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author 统计
 */
@RestController
@RequestMapping("/statistics")
@Slf4j
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/article/week")
    public ResponseEntity<ResponseResult<Object>> getArticleWeekIncreased(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue(value = ContentValue.STATISTICS_CACHE_NAME,required = false) String cookieCache){
        return ResponseEntity.ok(statisticService.getArticleWeekIncreased());
    }

    @GetMapping("/visitAnalyse")
    public ResponseEntity<ResponseResult<Object>> getVisitAnalyse(){
        return ResponseEntity.ok(statisticService.getOperatorDistributed());
    }
}
