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

@RestController
@RequestMapping("/statistics")
@Slf4j
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/article/week")
    public ResponseEntity<ResponseResult<Map<String, Object>>> getArticleWeekIncreased(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue(value = ContentValue.STATISTICS_CACHE_NAME,required = false) String cookieCache){
        //String cache = CookieUtils.getCookieValue(request, ContentValue.STATISTICS_CACHE_NAME);
        Gson gson = new Gson();
        Type type = new TypeToken<ResponseResult<Map<String, Object>>>() {}.getType();
        ResponseResult<Map<String, Object>> result;
        //先从cookie里面获取，如果没有再从数据库查
        if (!StringUtils.isBlank(cookieCache)){
            result = gson.fromJson(cookieCache, type);
            log.info("cookie缓存里有数据，直接返回,{}",result);
        }else {
            result = statisticService.getArticleWeekIncreased();
            String json = gson.toJson(result, type);
            CookieUtils.setCookie(request,response, ContentValue.STATISTICS_CACHE_NAME,json,60*60*24,true);
        }
        return ResponseEntity.ok(result);
    }
}
