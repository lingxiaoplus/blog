package com.lingxiao.blog.controller;

import com.lingxiao.blog.bean.Comment;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<PageResult<CommentVo>> getComments(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize
    ){
        return ResponseEntity.ok(commentService.getComments(keyword,pageNum,pageSize));
    }


}
