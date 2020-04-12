package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.Menu;
import com.lingxiao.blog.bean.Role;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface MenuMapper extends Mapper<Menu>, IdListMapper<Menu,Long> {

}
