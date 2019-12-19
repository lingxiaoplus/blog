package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.Label;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface LabelMapper extends Mapper<Label>, IdListMapper<Label,Long> {

}
