package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.ResourceInfo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @author lingxiao
 */
public interface ResourceInfoMapper extends Mapper<ResourceInfo>, IdListMapper<ResourceInfo,Long>, InsertListMapper<ResourceInfo> {
    /**
     * 根据md5查文件
     * @param resourceMd5
     * @return
     */
    @Select("select count(1) from resource_info where resource_md5 = #{resource_md5}")
    int selectByMd5(String resourceMd5);
}
