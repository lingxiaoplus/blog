package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.IpRegion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface IP2RegionMapper extends Mapper<IpRegion>, InsertListMapper<IpRegion> {

    @Select("SELECT country,province,city,operator FROM `ip_region` where #{ip} BETWEEN ip_start and ip_end")
    IpRegion selectRegionByIp(@Param("ip") Long ip);
}
