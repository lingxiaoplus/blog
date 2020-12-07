package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.po.IpRegion;

/**
 * @author renml
 * @date 2020/12/7 14:57
 */
public interface IP2RegionService {
    /**
     * ip 查找地址
     * @param ip
     * @return
     */
    IpRegion selectRegionByIp(long ip);
}
