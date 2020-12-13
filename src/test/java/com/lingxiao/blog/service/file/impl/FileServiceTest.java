package com.lingxiao.blog.service.file.impl;

import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.service.file.FileService;
import com.lingxiao.blog.utils.DateUtil;
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

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
class FileServiceTest {
    @Autowired
    private RedisUtil redisUtil;
    @Test
    void getBingImages(){
        try {
            int currentPage = 1;
            Connection connection = Jsoup.connect(ContentValue.BING_IOLIU_URL + "?p=" + currentPage)
                    .header("Referer", ContentValue.BING_IOLIU_URL)
                    .header("User-Agent", ContentValue.USER_AGENT)
                    .timeout(5000);
            Connection.Response response = connection.response();
            response.cookies();
            Document doc = connection.get();
            Elements elementPage = doc.getElementsByClass("container");
            elementPage.forEach(item ->{
                Elements itemElements = item.getElementsByClass("item");
                itemElements.forEach(image ->{
                    String imageUrl = image.select("img").attr("src");
                    int hashCode = imageUrl.hashCode();

                    String description = image.getElementsByClass("description").first().text();
                    String calendar = image.getElementsByClass("calendar").first().select("em").text();
                    String replaceUrl = StringUtils.replace(imageUrl, "640x480", "1920x1080");
                    BingImage bingImage = new BingImage();
                    bingImage.setTitle(description);
                    bingImage.setUrl(replaceUrl);
                    bingImage.setUrlBase("");
                    bingImage.setHashCode(String.valueOf(hashCode));
                    bingImage.setStartDate(DateUtil.getDateFromString(calendar,"yyyy-MM-dd"));
                    bingImage.setCreateDate(new Date());
                    log.info("jsoup 爬取到的： {}",bingImage);
                });
            });
            Integer maxPage = redisUtil.getValueByKey(RedisConstants.KEY_BACK_BINGIMAGE_TASK_MAXPAGE);
            if (maxPage == null){
                Elements pageElement = doc.getElementsByClass("page");
                String span = pageElement.select("span").text();
                String[] split = StringUtils.split(span, "/");
                if (split.length > 1){
                    maxPage = Integer.valueOf(split[1].trim());
                    log.info("最大页数，{}",maxPage);
                    redisUtil.pushValue(RedisConstants.KEY_BACK_BINGIMAGE_TASK_MAXPAGE,maxPage, TimeUnit.DAYS.toMillis(30));
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}