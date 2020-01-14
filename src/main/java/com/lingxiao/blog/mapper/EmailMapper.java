package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.Email;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface EmailMapper extends Mapper<Email>, IdListMapper<Email,Long> {
}
