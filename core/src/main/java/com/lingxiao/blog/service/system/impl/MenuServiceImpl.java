package com.lingxiao.blog.service.system.impl;

import com.lingxiao.blog.bean.po.Menu;
import com.lingxiao.blog.bean.po.MenuRole;
import com.lingxiao.blog.bean.po.Role;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.base.BaseMapper;
import com.lingxiao.blog.mapper.MenuMapper;
import com.lingxiao.blog.mapper.MenuRoleMapper;
import com.lingxiao.blog.mapper.RoleMapper;
import com.lingxiao.blog.service.BaseServiceImpl;
import com.lingxiao.blog.service.system.MenuService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingxiao
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuRoleMapper menuRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public BaseMapper<Menu,Long> getMapper() {
        return menuMapper;
    }

    @Override
    public List<Menu> getByCondition(@Nullable Menu data) {
        Menu selectMenu = new Menu();
        selectMenu.setParentId(0L);
        List<Menu> menus = menuMapper.select(selectMenu);
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
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyList();
        List<Role> roles = roleMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(roles)){
            return Collections.emptyList();
        }
        return roles;
    }
}
