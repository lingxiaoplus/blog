package com.lingxiao.blog.controller;

import com.lingxiao.blog.bean.Menu;
import com.lingxiao.blog.global.ResponseResult;
import com.lingxiao.blog.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<ResponseResult<List<Menu>>> selectAllMenu(){
        ResponseResult<List<Menu>> result = new ResponseResult<>(menuService.selectAll());
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<Void> addMenu(@RequestBody @Valid Menu menu){
        menuService.addMenu(menu);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateMenu(@RequestBody @Valid Menu menu){
        menuService.updateMenu(menu);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(value = "/{mid}")
    public ResponseEntity<Void> deleteMenu(@PathVariable("mid") Long id){
        menuService.deleteMenu(id);
        return ResponseEntity.ok().build();
    }
}
