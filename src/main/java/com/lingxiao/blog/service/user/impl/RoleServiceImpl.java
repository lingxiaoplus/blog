package com.lingxiao.blog.service.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.Comment;
import com.lingxiao.blog.bean.Menu;
import com.lingxiao.blog.bean.MenuRole;
import com.lingxiao.blog.bean.Role;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.MenuMapper;
import com.lingxiao.blog.mapper.MenuRoleMapper;
import com.lingxiao.blog.mapper.RoleMapper;
import com.lingxiao.blog.service.system.MenuService;
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
    private MenuMapper menuMapper;

    @Override
    public void addRole(Role role) {
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
        roles.stream().forEach((item)->{
            MenuRole menuRole = new MenuRole();
            menuRole.setRid(item.getId());
            List<Long> mIds = menuRoleMapper
                    .select(menuRole)
                    .stream()
                    .map(MenuRole::getMid)
                    .collect(Collectors.toList());
            List<Menu> menus = menuMapper.selectByIdList(mIds);
            item.setMenuList(menus);
        });
        PageInfo<Role> pageInfo = PageInfo.of(roles);
        return new PageResult<Role>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public List<Role> getRolesByUser(long uid) {

        return null;
    }
}