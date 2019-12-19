package com.lingxiao.blog.controller;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.Label;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.LabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "标签接口")
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @ApiOperation(value = "添加标签")
    @ApiImplicitParam(name = "label",value = "标签实体类")
    @OperationLogDetail(detail = "添加标签",operationType = OperationType.INSERT)
    @PostMapping
    public ResponseEntity<Void> addLabel(@RequestBody Label label){
        labelService.addLabel(label);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新标签")
    @ApiImplicitParam(name = "label",value = "标签实体类")
    @OperationLogDetail(detail = "更新标签",operationType = OperationType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> updateLabel(@RequestBody Label label){
        labelService.updateLabel(label);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除标签")
    @ApiImplicitParam(name = "ids",value = "标签id列表")
    @OperationLogDetail(detail = "删除标签",operationType = OperationType.DELETE)
    @DeleteMapping("/{ids}")
    public ResponseEntity<Void> deleteLabel(@PathVariable("ids") List<Long> ids){
        labelService.deleteLabel(ids);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示多少"),
            @ApiImplicitParam(name = "keyword",value = "关键词")
    })
    @ApiOperation(value = "分页获取标签")
    @OperationLogDetail(detail = "分页获取标签",operationType = OperationType.SELECT)
    @GetMapping
    public ResponseEntity<PageResult<Label>> getLabelList(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        return ResponseEntity.ok(labelService.getLabels(keyword,pageNum,pageSize));
    }
}
