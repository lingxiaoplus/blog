package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.UserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;

public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("select MAX(role_level) from role where id in (select role_id from user_role where user_id=#{userId})")
    int maxRoleLevel(@Param("userId") long userId);
}
