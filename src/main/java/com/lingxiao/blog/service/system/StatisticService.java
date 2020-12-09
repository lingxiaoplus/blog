package com.lingxiao.blog.service.system;

import com.lingxiao.blog.global.api.ResponseResult;

/**
 * @author admin
 */
public interface StatisticService {
    /**
     * 文章相关的统计
     * @return
     */
    ResponseResult<Object> getArticleWeekIncreased();

    /**
     * 访问ip数和运营商
     * @return
     */
    ResponseResult<Object> getOperatorDistributed();
}
