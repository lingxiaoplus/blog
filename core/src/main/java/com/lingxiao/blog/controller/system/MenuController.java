package com.lingxiao.blog.controller.system;

import com.lingxiao.blog.bean.po.Menu;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.system.MenuService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api("菜单管理接口")
@RequestMapping(value = "/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "获取所有菜单，不同用户可能显示不同菜单", notes = "获取所有菜单")
    @GetMapping
    public ResponseEntity<ResponseResult<List<Menu>>> selectAllMenu(){
        ResponseResult<List<Menu>> result = new ResponseResult<>(menuService.selectAll());
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "添加菜单",notes = "添加菜单")
    @ApiImplicitParam(name = "menu",value = "menu对象")
    @PostMapping
    public ResponseEntity<Void> addMenu(@RequestBody @Valid Menu menu){
        menuService.addMenu(menu);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新菜单",notes = "更新菜单")
    @ApiImplicitParam(name = "menu",value = "menu对象")
    @PutMapping
    public ResponseEntity<Void> updateMenu(@RequestBody @Valid Menu menu){
        menuService.updateMenu(menu);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除菜单",notes = "删除菜单")
    @ApiImplicitParam(name = "id",value = "菜单id")
    @DeleteMapping(value = "/{mid}")
    public ResponseEntity<Void> deleteMenu(@PathVariable("mid") Long id){
        menuService.deleteMenu(id);
        return ResponseEntity.ok().build();
    }
}
