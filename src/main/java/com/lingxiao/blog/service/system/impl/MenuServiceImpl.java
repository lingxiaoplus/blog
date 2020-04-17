package com.lingxiao.blog.service.system.impl;

import com.lingxiao.blog.bean.Menu;
import com.lingxiao.blog.bean.MenuRole;
import com.lingxiao.blog.bean.Role;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.mapper.MenuMapper;
import com.lingxiao.blog.mapper.MenuRoleMapper;
import com.lingxiao.blog.mapper.RoleMapper;
import com.lingxiao.blog.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuRoleMapper menuRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void addMenu(Menu menu) {
        //验证当前用户是否有修改菜单的权利
        menu.setId(null);
        menu.setCreateAt(new Date());
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
        Menu selectmenu = new Menu();
        selectmenu.setParentId(0L);
        List<Menu> menus = menuMapper.select(selectmenu);
        if (menus == null) {
            throw new BlogException(ExceptionEnum.MENU_SELECT_ERROR);
        }
        menus.forEach(this::getChildrenMenu);
        return menus;
    }

    /**
     * 只有两级 就不用做递归了
     * @param menu
     */
    private void getChildrenMenu(Menu menu){
        long parentId = menu.getId();
        Menu selectMenu = new Menu();
        selectMenu.setParentId(parentId);
        List<Menu> children = menuMapper.select(selectMenu);
        menu.setChildren(children);
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
        if (CollectionUtils.isEmpty(ids)) return null;
        List<Role> roles = roleMapper.selectByIdList(ids);
        /*if (CollectionUtils.isEmpty(roles)){
            throw new BlogException(ExceptionEnum.ROLE_SELECT_ERROR);
        }*/
        return roles;
    }
}
