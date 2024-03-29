package com.lingxiao.blog.controller.article;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.ArticleCommitResponse;
import com.lingxiao.blog.bean.ArticleCommitUrlVo;
import com.lingxiao.blog.bean.po.Article;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.article.ArticleService;
import com.lingxiao.blog.bean.vo.ArticleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "文章接口")
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    @ApiOperation(value = "添加文章")
    @ApiImplicitParam(name = "Article",value = "文章实体")
    @OperationLogDetail(detail = "添加文章",operationType = OperationType.INSERT)
    public ResponseEntity<Long> addArticle(@RequestBody @Valid Article article){
        return ResponseEntity.ok(articleService.addArticle(article));
    }

    @PutMapping
    @ApiOperation(value = "更新文章状态")
    @ApiImplicitParam(name = "Article",value = "文章实体")
    @OperationLogDetail(detail = "更新文章状态",operationType = OperationType.UPDATE)
    public ResponseEntity<Void> updateArticle(@RequestBody Article article){
        articleService.updateArticle(article);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除文章")
    @ApiImplicitParam(name = "id",value = "文章id")
    @OperationLogDetail(detail = "删除文章",operationType = OperationType.DELETE)
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id){
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult<ArticleVo>> getArticle(@PathVariable("id") Long id){
        ResponseResult<ArticleVo> responseResult = new ResponseResult<>(articleService.getArticleContent(id));
        return ResponseEntity.ok(responseResult);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示多少"),
            @ApiImplicitParam(name = "keyword",value = "关键词")
    })
    @ApiOperation(value = "分页获取文章")
    @OperationLogDetail(detail = "分页获取文章",operationType = OperationType.SELECT)
    @GetMapping
    public ResponseEntity<PageResult<ArticleVo>> getArticles(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        return ResponseEntity.ok(articleService.getArticles(keyword, null, pageNum,pageSize));
    }

    @PutMapping
    @ApiOperation(value = "提交文章链接")
    @ApiImplicitParam(name = "ArticleCommitUrlVo",value = "文章提交链接")
    @OperationLogDetail(detail = "提交文章链接",operationType = OperationType.INSERT)
    public ResponseEntity<ArticleCommitResponse> commitArticles(@RequestBody ArticleCommitUrlVo articleCommit){
        return articleService.commitUrl(articleCommit);
    }
}
