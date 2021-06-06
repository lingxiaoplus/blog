package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.BaseModel;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author renml
 * @date 2021/6/5 10:56 上午
 */
public interface BaseService<T extends BaseModel> {
    /**
     * 增加
     * @param data 实体类
     */
    void add(T data);

    /**
     * 删除
     * @param ids 批量
     */
    void delete(List<Long> ids);

    /**
     * 修改
     * @param data 实体类
     */
    void update(T data);

    /**
     * 获取详情
     * @param id 主键
     * @return 实体类
     */
    T getById(long id);

    /**
     * 获取列表
     * @param pageNum 页码
     * @param pageSize 页数
     * @return 列表
     */
    PageResult<T> getList(int pageNum, int pageSize);

    /**
     * 根据条件获取列表
     * @param data 实体类
     * @return 列表
     */
    List<T> getByCondition(@Nullable T data);
}
