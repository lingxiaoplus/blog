package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.FriendLink;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface FriendLinkMapper extends Mapper<FriendLink> , IdListMapper<FriendLink,Long> {

}
