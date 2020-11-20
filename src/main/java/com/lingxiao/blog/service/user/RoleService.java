package com.lingxiao.blog.service.user;

import com.lingxiao.blog.bean.po.Role;
import com.lingxiao.blog.global.api.PageResult;

import java.util.List;

public interface RoleService {
    void addRole(Role role);
    void deleteRole(Long id);
    void updateRole(Role role);
    Role selectById(Long id);
    PageResult<Role> selectAll(int pageNum, int pageSize);
    Role getRoleByLevel(int level);

    /**
     * 获取这个菜单能被哪些角色访问
     * @return
     */
    List<Role> getRolesByUser(long uid);

    void updateRoleMenu(Long roleId,List<Long> menuIds);

    boolean haveAdmin();
}
