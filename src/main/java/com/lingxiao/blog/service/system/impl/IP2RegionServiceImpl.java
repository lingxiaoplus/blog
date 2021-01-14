package com.lingxiao.blog.service.system.impl;

import com.lingxiao.blog.bean.po.IpRegion;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.service.system.IP2RegionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author renml  引用一个轻量级数据库 抛弃原有的ip段查询地址提高查询效率
 * @date 2020/12/7 14:58
 */
@Service("ip2RegionService")
@Slf4j
public class IP2RegionServiceImpl implements IP2RegionService {
    private DbSearcher dbSearcher;
    @Value("${ipregion.path}")
    private String regionPath;
    @PostConstruct
    private void init() {
        try {
            dbSearcher = new DbSearcher(new DbConfig(), regionPath);
        }catch (IOException|DbMakerConfigException e){
            log.error("ip地址库初始化失败");
            e.printStackTrace();
        }
    }

    @Override
    public IpRegion selectRegionByIp(long ip){
        if (dbSearcher == null){
            log.error("ip地址库初始化失败");
            throw new BlogException(ExceptionEnum.IP_REGION_INIT_ERROR);
        }
        IpRegion ipRegion = new IpRegion();
        try {
            DataBlock dataBlock = dbSearcher.btreeSearch(ip);
            String region = dataBlock.getRegion();
            String[] split = StringUtils.split(region,"\\|");
            if (split.length < 5){
                throw new BlogException(ExceptionEnum.IP_REGION_TRANSFORM_ERROR);
            }
            ipRegion.setCountry(split[0]);
            ipRegion.setProvince(split[2]);
            ipRegion.setCity(split[3]);
            ipRegion.setOperator(split[4]);
        } catch (IOException exception) {
            log.error("ip地址转换失败");
            exception.printStackTrace();
            throw new BlogException(ExceptionEnum.IP_REGION_TRANSFORM_ERROR);
        }
        return ipRegion;
    }

}
