package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.Dictionary;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface DictionaryMapper extends Mapper<Dictionary>, IdListMapper<Dictionary,String> {

}
