package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    @Select("select * from user where username=#{username}")
    User loginByName(@Param("username") String username);

    @Select("select * from user where email=#{email}")
    User loginByEmail(@Param("email") String email);

    @Select("select * from user where phone_number=#{phoneNumber}")
    User loginByPhone(@Param("phoneNumber") String phoneNumber);

    @Select("select count(user_id) from user where username=#{username}")
    int countByName(@Param("username") String username);
    @Select("select count(user_id) from user where email=#{email}")
    int countByEmail(@Param("email") String email);
    @Select("select count(user_id) from user where phone_number=#{phoneNumber}")
    int countByPhone(@Param("phoneNumber") String phoneNumber);
}
