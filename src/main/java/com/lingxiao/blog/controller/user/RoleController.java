package com.lingxiao.blog.controller.user;

import com.lingxiao.blog.annotation.OperationLogDetail;
import com.lingxiao.blog.bean.Role;
import com.lingxiao.blog.enums.OperationType;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.user.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api("角色管理接口")
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "添加角色",notes = "角色")
    @ApiImplicitParam(name = "role",value = "role对象")
    @PostMapping
    @OperationLogDetail(detail = "添加角色",operationType = OperationType.INSERT)
    public ResponseEntity<Void> addRole(@Valid Role role){
        roleService.addRole(role);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询角色",notes = "查询角色")
    @GetMapping("/all")
    @OperationLogDetail(detail = "查询角色",operationType = OperationType.SELECT)
    public ResponseEntity<PageResult<Role>> selectAllRoles(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                                           @RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        PageResult<Role> roles = roleService.selectAll(pageNum,pageSize);
        return ResponseEntity.ok(roles);
    }
}
