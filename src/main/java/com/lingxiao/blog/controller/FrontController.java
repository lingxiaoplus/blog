package com.lingxiao.blog.controller;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.Category;
import com.lingxiao.blog.bean.vo.ArticleVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.ArticleService;
import com.lingxiao.blog.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/front")
@Api("首页数据接口")
public class FrontController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "获取所有分类", notes = "获取分类列表")
    @GetMapping("/category")
    public ResponseEntity<ResponseResult> getCategorys(){
        ResponseResult responseResult = new ResponseResult<List<Category>>(categoryService.selectAll());
        return ResponseEntity.ok(responseResult);
    }

    @ApiOperation(value = "分页获取文章")
    @OperationLogDetail(detail = "分页获取文章",operationType = OperationType.SELECT)
    @GetMapping("/article")
    public ResponseEntity<PageResult<ArticleVo>> getArticles(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        return ResponseEntity.ok(articleService.getArticles(keyword,pageNum,pageSize));
    }

}
