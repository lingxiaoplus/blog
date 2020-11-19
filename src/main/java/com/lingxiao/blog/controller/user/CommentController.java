package com.lingxiao.blog.controller.user;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.po.Comment;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.user.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "评论接口")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示多少"),
            @ApiImplicitParam(name = "keyword",value = "关键词")
    })
    @ApiOperation(value = "分页获取评论")
    @OperationLogDetail(detail = "分页获取评论",operationType = OperationType.SELECT)
    @GetMapping
    public ResponseEntity<PageResult<CommentVo>> getComments(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize
    ){
        return ResponseEntity.ok(commentService.getComments(keyword,pageNum,pageSize));
    }

    @ApiImplicitParam(name = "comment",value = "评论实体类")
    @ApiOperation(value = "添加评论")
    @OperationLogDetail(detail = "添加评论",operationType = OperationType.INSERT)
    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody @Valid Comment comment){
        commentService.addComment(comment);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParam(name = "ids",value = "评论id列表")
    @ApiOperation(value = "删除评论")
    @OperationLogDetail(detail = "删除评论",operationType = OperationType.DELETE)
    @DeleteMapping
    public ResponseEntity<Void> deleteComment(@RequestParam("ids") List<Long> ids){
        commentService.deleteComments(ids);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParam(name = "ids",value = "评论id列表")
    @ApiOperation(value = "删除评论")
    @OperationLogDetail(detail = "删除评论",operationType = OperationType.DELETE)
    @PutMapping
    public ResponseEntity<Void> deleteComment(@RequestParam("ids") List<Long> ids,@RequestParam("type") Integer type){
        commentService.setCommentStatus(ids,type);
        return ResponseEntity.ok().build();
    }
}
