package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.Menu;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.mapper.MenuMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.MenuService;
import com.lingxiao.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addMenu(Menu menu) {
        //验证当前用户是否有修改菜单的权利
        menu.setId(null);
        int count = menuMapper.insertSelective(menu);
        if (count != 1){
            throw new BlogException(ExceptionEnum.MENU_ADD_ERROR);
        }
    }

    @Override
    public void deleteMenu(Long id) {
        int count = menuMapper.deleteByPrimaryKey(id);
        if (count != 1){
            throw new BlogException(ExceptionEnum.MENU_DELETE_ERROR);
        }
    }

    @Override
    public void updateMenu(Menu menu) {
        int count = menuMapper.updateByPrimaryKeySelective(menu);
        if (count != 1){
            throw new BlogException(ExceptionEnum.MENU_UPDATE_ERROR);
        }
    }

    @Override
    public Menu selectById(Long id) {
        Menu menu = menuMapper.selectByPrimaryKey(id);
        if (menu == null){
            throw new BlogException(ExceptionEnum.MENU_SELECT_ERROR);
        }
        return menu;
    }

    @Override
    public List<Menu> selectAll(Long uid) {
        List<Menu> menus = menuMapper.selectAll();
        if (menus == null) {
            throw new BlogException(ExceptionEnum.MENU_SELECT_ERROR);
        }
        return menus;
    }
}
