package com.lingxiao.blog.controller;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.Category;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
@Api("文章分类管理接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "添加分类")
    @ApiImplicitParam(name = "Category",value = "Category对象")
    @OperationLogDetail(detail = "添加分类",operationType = OperationType.INSERT)
    @PostMapping
    public ResponseEntity<Void> addCategory(@RequestBody @Valid Category category){
        categoryService.addCategory(category);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "获取所有分类", notes = "获取分类列表")
    @OperationLogDetail(detail = "获取分类",operationType = OperationType.SELECT)
    @GetMapping
    public ResponseEntity<ResponseResult> getCategorys(){
        ResponseResult responseResult = new ResponseResult<List<Category>>(categoryService.selectAll());
        return ResponseEntity.ok(responseResult);
    }

    @ApiOperation(value = "根据id获取分类")
    @OperationLogDetail(detail = "根据id获取分类",operationType = OperationType.SELECT)
    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseResult> getCategorys(@PathVariable Long id){
        ResponseResult responseResult = new ResponseResult<Category>(categoryService.selectById(id));
        return ResponseEntity.ok(responseResult);
    }
    @ApiOperation(value = "根据id删除分类")
    @OperationLogDetail(detail = "根据id删除分类",operationType = OperationType.DELETE)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "更新分类")
    @ApiImplicitParam(name = "Category",value = "Category对象")
    @OperationLogDetail(detail = "更新分类",operationType = OperationType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> updateCategory(@RequestBody @Valid Category category){
        categoryService.updateCategory(category);
        return ResponseEntity.ok().build();
    }
}
