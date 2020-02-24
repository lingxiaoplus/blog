package com.lingxiao.blog.controller;

import com.lingxiao.blog.bean.Theme;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/theme")
@Api("个性化主题")
public class ThemeController {
    @Autowired
    private ThemeService themeService;

    @PostMapping
    public ResponseEntity<Void> saveTheme(@RequestBody @Valid Theme theme){
        themeService.saveTheme(theme);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("根据用户id获取主题")
    @ApiImplicitParam(name = "uid",value = "用户id")
    @GetMapping("/{uid}")
    public ResponseEntity<ResponseResult<Theme>> getThemeById(@PathVariable("uid") Long uid){
        return ResponseEntity.ok(new ResponseResult<>(themeService.getThemeById(uid)));
    }

}
