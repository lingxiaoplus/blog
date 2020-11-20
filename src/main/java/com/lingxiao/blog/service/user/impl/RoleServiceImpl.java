package com.lingxiao.blog.service.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.po.MenuRole;
import com.lingxiao.blog.bean.po.Role;
import com.lingxiao.blog.bean.po.UserRole;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.MenuRoleMapper;
import com.lingxiao.blog.mapper.RoleMapper;
import com.lingxiao.blog.mapper.UserRoleMapper;
import com.lingxiao.blog.service.user.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuRoleMapper menuRoleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void addRole(Role role) {
        role.setId(null);
        int count = roleMapper.insertSelective(role);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.ROLE_ADD_ERROR);
        }
    }

    @Override
    public void deleteRole(Long id) {
        int count = roleMapper.deleteByPrimaryKey(id);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.ROLE_DELETE_ERROR);
        }
    }

    @Override
    public void updateRole(Role role) {
        int count = roleMapper.updateByPrimaryKeySelective(role);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.ROLE_UPDATE_ERROR);
        }
    }

    @Override
    public Role selectById(Long id) {
        Role role = roleMapper.selectByPrimaryKey(id);
        if (role == null){
            throw new BlogException(ExceptionEnum.ROLE_SELECT_ERROR);
        }
        return role;
    }

    @Override
    public PageResult<Role> selectAll(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Role> roles = roleMapper.selectAll();
        if (CollectionUtils.isEmpty(roles)){
            throw new BlogException(ExceptionEnum.ROLE_SELECT_ERROR);
        }
        roles.forEach((item)->{
            MenuRole menuRole = new MenuRole();
            menuRole.setRid(item.getId());
            List<Long> mIds = menuRoleMapper
                    .select(menuRole)
                    .stream()
                    .map(MenuRole::getMid)
                    .collect(Collectors.toList());
            //List<Menu> menus = menuMapper.selectByIdList(mIds);
            item.setMenuList(mIds);
        });
        PageInfo<Role> pageInfo = PageInfo.of(roles);
        return new PageResult<Role>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public List<Role> getRolesByUser(long uid) {
        UserRole userRole = new UserRole();
        userRole.setUserId(uid);
        List<UserRole> select = userRoleMapper.select(userRole);
        if (CollectionUtils.isEmpty(select)) return null;
        List<Long> ids = select.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleMapper.selectByIdList(ids);
        return roles;
    }

    @Override
    public Role getRoleByLevel(int level) {
        Role role = new Role();
        role.setRoleLevel(level);
        Role selectOne = roleMapper.selectOne(role);
        return selectOne;
    }

    @Override
    public void updateRoleMenu(Long roleId,List<Long> menuIds){
        if (roleId == null || CollectionUtils.isEmpty(menuIds)){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        MenuRole menuRole = new MenuRole();
        menuRole.setRid(roleId);
        List<Long> ids = menuRoleMapper
                .select(menuRole)
                .stream()
                .map(MenuRole::getId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(ids)){
            int count = menuRoleMapper.deleteByIdList(ids);
            if (count != ids.size()){
                throw new BlogException(ExceptionEnum.ROLE_MENU_SECURITY_UPDATE_ERROR);
            }
        }
        List<MenuRole> insertList = menuIds.stream().map((item) -> {
            MenuRole menuRoleInsert = new MenuRole();
            menuRoleInsert.setRid(roleId);
            menuRoleInsert.setMid(item);
            return menuRoleInsert;
        }).collect(Collectors.toList());
        int insertCount = menuRoleMapper.insertList(insertList);
        if (insertCount != insertList.size()) {
            throw new BlogException(ExceptionEnum.ROLE_MENU_SECURITY_UPDATE_ERROR);
        }
    }

    @Override
    public boolean haveAdmin() {
        Role role = new Role();
        role.setRoleTag(ContentValue.USER_TAG_ADMIN);
        role = roleMapper.selectOne(role);
        UserRole userRole = new UserRole();
        userRole.setRoleId(role.getId());
        return userRoleMapper.selectCount(userRole) > 0;
    }
}
