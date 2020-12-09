package com.lingxiao.blog.service.system.impl;

import com.lingxiao.blog.bean.po.IpRegion;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.service.system.IP2RegionService;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author renml  引用一个轻量级数据库 抛弃原有的ip段查询地址提高查询效率
 * @date 2020/12/7 14:58
 */
@Service("ip2RegionService")
public class IP2RegionServiceImpl implements IP2RegionService {
    @Autowired
    private ResourceLoader resourceLoader;
    private DbSearcher dbSearcher;

    @PostConstruct
    private void init(){
        Resource resource = resourceLoader.getResource("classpath:ip2region.db");
        try {
            dbSearcher = new DbSearcher(new DbConfig(), resource.getFile().getPath());
        }catch (IOException|DbMakerConfigException e){
            e.printStackTrace();
        }
    }

    @Override
    public IpRegion selectRegionByIp(long ip){
        if (dbSearcher == null){
            throw new BlogException(ExceptionEnum.IP_REGION_INIT_ERROR);
        }
        IpRegion ipRegion = new IpRegion();
        try {
            DataBlock dataBlock = dbSearcher.btreeSearch(ip);
            String region = dataBlock.getRegion();
            String[] split = StringUtils.split(region,"\\|");
            if (split.length < 5){
                throw new BlogException(ExceptionEnum.IP_REGION_INIT_ERROR);
            }
            ipRegion.setCountry(split[0]);
            ipRegion.setProvince(split[2]);
            ipRegion.setCity(split[3]);
            ipRegion.setOperator(split[4]);
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new BlogException(ExceptionEnum.IP_REGION_INIT_ERROR);
        }
        return ipRegion;
    }

}
