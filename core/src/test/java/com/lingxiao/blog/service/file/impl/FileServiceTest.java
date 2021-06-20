package com.lingxiao.blog.service.file.impl;

import com.lingxiao.blog.BaseApplicationTests;
import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.bean.po.ResourceInfo;
import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.BingImageMapper;
import com.lingxiao.blog.mapper.ResourceInfoMapper;
import com.lingxiao.blog.service.file.FileService;
import com.lingxiao.blog.utils.DateUtil;
import com.lingxiao.blog.utils.ImageUtil;
import com.lingxiao.blog.utils.MD5Util;
import com.lingxiao.blog.utils.RedisUtil;
import com.lingxiao.oss.bean.OssFileInfo;
import com.lingxiao.oss.service.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
@Slf4j
class FileServiceTest extends BaseApplicationTests {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BingImageMapper bingImageMapper;
    @Autowired
    private FileService fileService;

    @Autowired
    private OssFileService ossFileService;
    @Autowired
    private ResourceInfoMapper resourceInfoMapper;

    @Test
    void getBingImages(){
        fileService.getBingImageByJsoup(1);
    }

    @Test
    void getOssFile(){
        List<OssFileInfo> fileList = ossFileService.getFileList("/image//E:\\bingImage", 1000);
        Assertions.assertNotNull(fileList);
        List<ResourceInfo> resultData = resourceInfoMapper.selectAll();
        fileList.forEach(item ->{
            resultData.stream().filter(res -> StringUtils.equals(item.getFileMd5(),res.getResourceMd5())).findFirst().ifPresent(res->{
                int index = StringUtils.ordinalIndexOf(item.getPath(), ":", 2);
                String substring = StringUtils.substring(item.getPath(), index);
                String prefix = StringUtils.substring(item.getPath(), 0,index);
                try {
                    res.setPath(prefix + URLEncoder.encode(substring, StandardCharsets.UTF_8.name()));
                    int count = resourceInfoMapper.updateByPrimaryKeySelective(res);
                    if (count != 1){
                        throw new BlogException(ExceptionEnum.DATA_UPDATE_ERROR);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}