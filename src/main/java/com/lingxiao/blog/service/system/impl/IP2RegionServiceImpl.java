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
 * @author renml
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
        }catch (IOException e){
            e.printStackTrace();
        } catch (DbMakerConfigException e) {
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
            ipRegion.setCountry(split[1]);
            ipRegion.setProvince(split[3]);
            ipRegion.setCity(split[4]);
            if (split.length >=6){
                ipRegion.setOperator(split[5]);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new BlogException(ExceptionEnum.IP_REGION_INIT_ERROR);
        }
        return ipRegion;
    }

}
