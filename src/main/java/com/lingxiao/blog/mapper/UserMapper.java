package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    @Select("select * from user where username=#{username} and password=#{password}")
    void login(@Param("username") String username,@Param("password") String password);
}
