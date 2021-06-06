package com.lingxiao.blog.global.base;

import com.lingxiao.blog.bean.po.Menu;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author renml
 * @date 2021/6/5 11:27 上午
 */
public interface BaseMapper<T, K> extends Mapper<T>, IdListMapper<T,K> {
}
