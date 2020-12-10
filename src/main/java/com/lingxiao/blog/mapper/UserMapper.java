package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author admin
 */
public interface UserMapper extends Mapper<User> {

    /**
     * 登录
     * @param condition 名字/邮箱/电话
     * @return
     */
    User login(@Param("condition") String condition);

    /**
     * 根据条件查询用户数
     * @param account  名字/邮箱/电话
     * @return
     */
    int selectCountByAccount(@Param("account")String account);

    /**
     * 今日新增用户
     * @return
     */
    @Select("select count(user_id) from `user` where to_days(create_at) = to_days(now())")
    int todayIncreased();
}
