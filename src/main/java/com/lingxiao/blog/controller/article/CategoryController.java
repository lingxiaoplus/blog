package com.lingxiao.blog.controller.article;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.Category;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.article.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示多少"),
            @ApiImplicitParam(name = "keyword",value = "关键词")
    })
    @ApiOperation(value = "分页获取分类")
    @OperationLogDetail(detail = "分页获取分类",operationType = OperationType.SELECT)
    @GetMapping
    public ResponseEntity<PageResult<Category>> getCategories(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        return ResponseEntity.ok(categoryService.getCategories(keyword,pageNum,pageSize));
    }


    @ApiOperation(value = "根据id获取分类")
    @OperationLogDetail(detail = "根据id获取分类",operationType = OperationType.SELECT)
    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseResult<Category>> getCategoryById(@PathVariable Long id){
        ResponseResult<Category> responseResult = new ResponseResult<>(categoryService.selectById(id));
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
