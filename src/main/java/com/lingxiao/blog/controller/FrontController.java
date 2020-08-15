package com.lingxiao.blog.controller;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.Category;
import com.lingxiao.blog.bean.FriendLink;
import com.lingxiao.blog.bean.Label;
import com.lingxiao.blog.bean.vo.ArticleDetailVo;
import com.lingxiao.blog.bean.vo.ArticleVo;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.bean.vo.HomePageVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.mapper.LabelMapper;
import com.lingxiao.blog.service.article.ArticleService;
import com.lingxiao.blog.service.article.CategoryService;
import com.lingxiao.blog.service.article.LabelService;
import com.lingxiao.blog.service.system.ThemeService;
import com.lingxiao.blog.service.user.CommentService;
import com.lingxiao.blog.service.system.FriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/front")
@Api("首页数据接口")
public class FrontController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FriendLinkService linkService;
    @Autowired
    private LabelService labelService;

    @ApiOperation(value = "获取所有分类", notes = "获取分类列表")
    @GetMapping("/category")
    public ResponseEntity<ResponseResult<List<Category>>> getCategorys(){
        ResponseResult<List<Category>> responseResult = new ResponseResult<>(categoryService.selectAll());
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

    @GetMapping("/banner")
    public ResponseEntity<ResponseResult<HomePageVo>> getBanner(@RequestParam(value = "bannerSize",defaultValue = "4")int bannerSize){
        return ResponseEntity.ok(articleService.getHomePageBanner(bannerSize));
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<ResponseResult<ArticleDetailVo>> getArticle(@PathVariable("id") Long id){
        ResponseResult responseResult = new ResponseResult<ArticleDetailVo>(articleService.getArticleContent(id));
        return ResponseEntity.ok(responseResult);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示多少"),
            @ApiImplicitParam(name = "keyword",value = "关键词")
    })
    @ApiOperation(value = "分页获取评论")
    @OperationLogDetail(detail = "分页获取评论",operationType = OperationType.SELECT)
    @GetMapping("/comments/{id}")
    public ResponseEntity<PageResult<CommentVo>> getComments(
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize,
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(commentService.getCommentsByArticleId(pageNum,pageSize,id));
    }

    @GetMapping("/link")
    public ResponseEntity<PageResult<FriendLink>> getLink(
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize
    ){
        PageResult<FriendLink> result = linkService.getLinks(pageNum, pageSize);
        return ResponseEntity.ok(result);
    }


    @ApiOperation(value = "获取所有标签")
    @OperationLogDetail(detail = "获取所有标签",operationType = OperationType.SELECT)
    @GetMapping("/labels")
    public ResponseEntity<ResponseResult<List<Label>>> getLabels(){
        return ResponseEntity.ok(labelService.getAllLabels());
    }
}
