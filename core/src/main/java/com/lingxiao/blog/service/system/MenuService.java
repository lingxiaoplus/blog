package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.po.Menu;
import com.lingxiao.blog.bean.po.Role;
import com.lingxiao.blog.service.BaseService;

import java.util.List;

/**
 * @author lingxiao
 */
public interface MenuService extends BaseService<Menu> {
    /**
     * 获取这个菜单能被哪些角色访问
     * @param mid 角色id
     * @return
     */
    List<Role> getRolesByMenu(long mid);
}
