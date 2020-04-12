package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.Menu;
import com.lingxiao.blog.bean.Role;

import java.util.List;

public interface MenuService {
    void addMenu(Menu menu);
    void deleteMenu(Long id);
    void updateMenu(Menu menu);
    Menu selectById(Long id);
    List<Menu> selectAll();

    /**
     * 获取这个菜单能被哪些角色访问
     * @return
     */
    List<Role> getRolesByMenu(long mid);
}
