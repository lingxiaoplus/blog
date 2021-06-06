package com.lingxiao.blog.service;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.lingxiao.blog.bean.BaseModel;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.base.BaseMapper;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author renml
 * @date 2021/6/5 11:10 上午
 */
public abstract class BaseServiceImpl<T extends BaseModel> implements BaseService<T>{
    /**
     * 获取mapper
     * @return
     */
    public abstract BaseMapper<T,Long> getMapper();
    @Override
    public void add(T data) {
        data.setCreateAt(new Date());
        int count = getMapper().insert(data);
        if (count != 1){
            throw new BlogException(ExceptionEnum.DATA_INSERT_ERROR);
        }
    }

    @Override
    public void update(T data) {
        int count = getMapper().updateByPrimaryKeySelective(data);
        if (count != 1){
            throw new BlogException(ExceptionEnum.DATA_UPDATE_ERROR);
        }
    }

    @Override
    public T getById(long id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public PageResult<T> getList(int pageNum, int pageSize) {
        PageMethod.startPage(pageNum,pageSize);
        List<T> list = getMapper().selectAll();
        PageInfo<T> pageInfo = PageInfo.of(list);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }

    @Override
    public List<T> getByCondition(@Nullable T data) {
        if (data == null){
            return getMapper().selectAll();
        }
        List<T> select = getMapper().select(data);
        if (CollectionUtils.isEmpty(select)){
            return Collections.emptyList();
        }
        return select;
    }

    @Override
    public void delete(List<Long> ids) {
        int count = getMapper().deleteByIdList(ids);
        if (count != ids.size()){
            throw new BlogException(ExceptionEnum.DATA_DELETE_ERROR);
        }
    }
}
