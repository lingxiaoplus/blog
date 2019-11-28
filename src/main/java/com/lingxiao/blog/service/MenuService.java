package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.Menu;

import java.util.List;

public interface MenuService {
    void addMenu(Menu menu);
    void deleteMenu(Long id);
    void updateMenu(Menu menu);
    Menu selectById(Long id);
    List<Menu> selectAll(Long uid);
}
