package com.lingxiao.blog.controller;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.Email;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.EmailService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "添加邮箱")
    @ApiImplicitParam(name = "email",value = "邮箱实体类")
    @OperationLogDetail(detail = "添加邮箱",operationType = OperationType.INSERT)
    @PostMapping
    public ResponseEntity<Void> addEmail(@RequestBody @Valid Email email){
        emailService.addEmail(email);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新邮箱")
    @ApiImplicitParam(name = "email",value = "邮箱实体类")
    @OperationLogDetail(detail = "更新邮箱",operationType = OperationType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> updateLabel(@RequestBody @Valid Email email){
        emailService.updateEmail(email);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除邮箱")
    @ApiImplicitParam(name = "ids",value = "邮箱id列表")
    @OperationLogDetail(detail = "删除邮箱",operationType = OperationType.DELETE)
    @DeleteMapping("/{ids}")
    public ResponseEntity<Void> deleteLabel(@PathVariable("ids") List<Long> ids){
        emailService.deleteEmail(ids);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示多少")
    })
    @ApiOperation(value = "分页获取邮箱")
    @OperationLogDetail(detail = "分页获取邮箱",operationType = OperationType.SELECT)
    @GetMapping
    public ResponseEntity<PageResult<Email>> getLabelList(
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        return ResponseEntity.ok(emailService.getEmails(pageNum,pageSize));
    }
}
