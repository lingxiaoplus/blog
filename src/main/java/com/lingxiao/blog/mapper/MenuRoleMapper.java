package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.MenuRole;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface MenuRoleMapper extends Mapper<MenuRole>, IdListMapper<MenuRole,Long>, InsertListMapper<MenuRole> {

}
