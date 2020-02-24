package com.lingxiao.blog.controller;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.FriendLink;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.FriendLinkService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/friend_link")
public class FriendLinkController {
    @Autowired
    private FriendLinkService friendLinkService;

    @ApiOperation(value = "添加友链")
    @ApiImplicitParam(name = "friendLink",value = "友链实体类")
    @OperationLogDetail(detail = "添加友链",operationType = OperationType.INSERT)
    @PostMapping
    public ResponseEntity<Void> addLink(@RequestBody @Valid FriendLink friendLink){
        friendLinkService.addLink(friendLink);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新友链")
    @ApiImplicitParam(name = "email",value = "友链实体类")
    @OperationLogDetail(detail = "更新友链",operationType = OperationType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> updateLink(@RequestBody @Valid FriendLink friendLink){
        friendLinkService.updateLink(friendLink);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除友链")
    @ApiImplicitParam(name = "ids",value = "友链id列表")
    @OperationLogDetail(detail = "删除友链",operationType = OperationType.DELETE)
    @DeleteMapping("/{ids}")
    public ResponseEntity<Void> deleteLink(@PathVariable("ids") List<Long> ids){
        friendLinkService.deleteLink(ids);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示多少")
    })
    @ApiOperation(value = "分页获取友链")
    @OperationLogDetail(detail = "分页获取友链",operationType = OperationType.SELECT)
    @GetMapping
    public ResponseEntity<PageResult<FriendLink>> getLabelList(
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        return ResponseEntity.ok(friendLinkService.getLinks(pageNum,pageSize));
    }
}
