package com.lingxiao.blog.controller;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.po.Category;
import com.lingxiao.blog.bean.po.FriendLink;
import com.lingxiao.blog.bean.po.Label;
import com.lingxiao.blog.bean.vo.ArticleDetailVo;
import com.lingxiao.blog.bean.vo.ArticleVo;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.bean.vo.HomePageVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.article.ArticleService;
import com.lingxiao.blog.service.article.CategoryService;
import com.lingxiao.blog.service.article.LabelService;
import com.lingxiao.blog.service.user.CommentService;
import com.lingxiao.blog.service.system.FriendLinkService;
import com.lingxiao.blog.utils.IPUtils;
import com.lingxiao.blog.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
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
    @Autowired
    private RedisUtil redisUtil;

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
        return ResponseEntity.ok(articleService.getArticlesFromPublished(keyword,pageNum,pageSize));
    }

    @GetMapping("/banner")
    public ResponseEntity<ResponseResult<HomePageVo>> getBanner(@RequestParam(value = "bannerSize",defaultValue = "4")int bannerSize){
        return ResponseEntity.ok(articleService.getHomePageBanner(bannerSize));
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<ResponseResult<ArticleDetailVo>> getArticle(@PathVariable("id") Long id){
        ResponseResult<ArticleDetailVo> responseResult = new ResponseResult<>(articleService.getArticleContent(id));
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


    @ApiOperation(value = "获取年度文章")
    @OperationLogDetail(detail = "获取年度文章",operationType = OperationType.SELECT)
    @GetMapping("/article/timeLineForYear")
    public ResponseEntity<List<ArticleVo>> getArticlesForYear(@RequestParam(value = "date")String date){
        try {
            Date parseDate = DateUtils.parseDate(date, "yyyy-MM-dd");
            return ResponseEntity.ok(articleService.getTimeLineArticle(parseDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "获取所有标签")
    @OperationLogDetail(detail = "获取所有标签",operationType = OperationType.SELECT)
    @GetMapping("/labels")
    public ResponseEntity<ResponseResult<List<Label>>> getLabels(){
        return ResponseEntity.ok(labelService.getAllLabels());
    }

    @GetMapping("/realIp")
    public ResponseEntity<String> getRealIp(HttpServletRequest request){
        String ipAddress = IPUtils.getIpAddress(request);
        return ResponseEntity.ok(ipAddress);
    }
    @Autowired
    private SpringTemplateEngine templateEngine;
    @RequestMapping("/mail")
    public String getEmail(){
        Context context = new Context();
        context.setVariable("title", "blog");
        String emailText = templateEngine.process("index", context);
        return emailText;
    }
}
