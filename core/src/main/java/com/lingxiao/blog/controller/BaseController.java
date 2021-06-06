package com.lingxiao.blog.controller;

import com.google.common.collect.Lists;
import com.lingxiao.blog.bean.BaseModel;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author renml
 * @date 2021/6/4 10:50 下午
 */
public abstract class BaseController<T extends BaseModel> {
    /**
     * 需要传入具体的service
     * @return
     */
    public abstract BaseService<T> getService();

    @PostMapping
    protected ResponseEntity<Void> addData(@RequestBody T data){
        getService().add(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    protected ResponseEntity<Void> updateData(@RequestBody T data){
        getService().update(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    protected ResponseEntity<Void> deleteData(@PathVariable("id") Long id){
        getService().delete(Lists.newArrayList(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ResponseResult<List<T>>> selectAll(){
        ResponseResult<List<T>> result = new ResponseResult<>(getService().getByCondition(null));
        return ResponseEntity.ok(result);
    }
}
