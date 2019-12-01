package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.Menu;
import com.lingxiao.blog.bean.MenuRole;
import com.lingxiao.blog.bean.Role;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.mapper.MenuMapper;
import com.lingxiao.blog.mapper.MenuRoleMapper;
import com.lingxiao.blog.mapper.RoleMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.MenuService;
import com.lingxiao.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuRoleMapper menuRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

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
    public List<Menu> selectAll() {
        List<Menu> menus = menuMapper.selectAll();
        if (menus == null) {
            throw new BlogException(ExceptionEnum.MENU_SELECT_ERROR);
        }
        return menus;
    }

    @Override
    public List<Role> getRolesByMenu(long mid) {
        MenuRole menuRole = new MenuRole();
        menuRole.setMid(mid);
        List<Long> ids = menuRoleMapper
                .select(menuRole)
                .stream()
                .map(MenuRole::getId)
                .collect(Collectors.toList());
        List<Role> roles = roleMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(roles)){
            throw new BlogException(ExceptionEnum.ROLE_SELECT_ERROR);
        }
        return roles;
    }
}