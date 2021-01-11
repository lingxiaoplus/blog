package com.lingxiao.blog.service.file.impl;

import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.mapper.BingImageMapper;
import com.lingxiao.blog.service.file.FileService;
import com.lingxiao.blog.utils.DateUtil;
import com.lingxiao.blog.utils.ImageUtil;
import com.lingxiao.blog.utils.MD5Util;
import com.lingxiao.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
class FileServiceTest {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BingImageMapper bingImageMapper;
    @Autowired
    private FileService fileService;

    @Test
    void getBingImages(){
        fileService.getBingImageByJsoup(1);
    }
}