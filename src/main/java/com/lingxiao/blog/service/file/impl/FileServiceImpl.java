package com.lingxiao.blog.service.file.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.form.PageQueryForm;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.OssProperties;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.BingImageMapper;
import com.lingxiao.blog.service.file.FileService;
import com.lingxiao.blog.utils.*;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Admin
 */
@EnableConfigurationProperties(OssProperties.class)
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private UploadUtil uploadUtil;
    @Autowired
    private BingImageMapper imageMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OssProperties ossProperties;

    /**
     * format js/xml
     * idx 0今天 1明天
     * n 1-8
     */
    private static final String BING_API = "https://cn.bing.com/HPImageArchive.aspx?format=js&n=8&mkt=zh-CN&idx=";

    @Autowired
    private BingImageMapper bingImageMapper;

    @Override
    public FileInfo uploadFile(File file) {
        try {
            return uploadUtil.upload(file);
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            log.error("上传文件失败: {}",r.toString());
            //响应的文本信息
            try {
                log.error("上传文件失败body: {}",r.bodyString());
            } catch (QiniuException ex) {
                ex.printStackTrace();
            }
            throw new BlogException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public FileInfo uploadFile(File file, String folder) {
        try {
            return uploadUtil.upload(file,folder);
        } catch (QiniuException e) {
            try {
                Response r = e.response;
                // 请求失败时打印的异常的信息
                log.error("上传文件失败: {},上传文件失败body: {}",r.toString(),r.bodyString());
            } catch (QiniuException ex) {
                ex.printStackTrace();
            }
            throw new BlogException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public OssProperties getOssProperties() {
        OssProperties ossProperties = uploadUtil.getOssProperties();
        ossProperties.setSecretKey("***********");
        return ossProperties;
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            uploadUtil.deleteFile(fileName);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            log.error("删除文件错误，错误码：{}，错误详细;{}",ex.code(),ex.response.toString());
            throw new BlogException(ExceptionEnum.DELETE_OSS_FILE_ERROR);
        }
    }

    @Override
    public PageResult getFileList(String fileName, String date,int pageNum, int pageSize) {
        List<FileInfo> fileList = uploadUtil.getFileList("", pageSize*pageNum);
        int totalPage = fileList.size() / pageSize + 1;
        List<FileInfo> subList = fileList.subList((pageSize * (pageNum - 1)), (pageSize * pageNum));
        return new PageResult<>(fileList.size(),totalPage,subList);
    }


    @Override
    public void moveOrRenameFile(String oldName,String newName, String toBucket) {
        try {
            uploadUtil.moveOrRenameFile(oldName, newName, toBucket);
        } catch (QiniuException ex) {
            //如果遇到异常，说明移动失败
            //ex.response.error
            log.error("移动/重命名文件错误：错误码：{}，错误详细: {}",ex.code(),ex.response.toString());
            throw new BlogException(ExceptionEnum.MOVE_OR_RENAME_OSS_FILE_ERROR);
        }
    }


    @Cacheable(value = "bingPictures")
    @Override
    public BingImageData getBingImages(int idx){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BING_API + idx);
            //得到connection对象。
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            //设置请求方式
            connection.setRequestMethod("GET");
            //连接
            connection.connect();
            //得到响应码
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                //将响应流转换成字符串
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String reponse = sb.toString();
                BingImageData bingImageData = new Gson().fromJson(reponse, BingImageData.class);
                bingImageData.getImages().forEach(item-> item.setUrl("https://cn.bing.com"+item.getUrl()));


                bingImageData.getImages().forEach(image -> {
                    BingImage bingImage = new BingImage();
                    bingImage.setTitle(image.getCopyright());
                    bingImage.setUrl(image.getUrl());
                    bingImage.setUrlBase("https://cn.bing.com".concat(image.getUrlbase()));
                    bingImage.setHashCode(image.getHsh());
                    bingImage.setStartDate(DateUtil.getDateFromString(image.getStartdate()));
                    bingImage.setCreateDate(new Date());
                    try {
                        imageMapper.insert(bingImage);
                    }catch (Exception e){
                        log.debug("插入失败");
                    }

                });


                return bingImageData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    private Map<String,String> downloadheaders = new HashMap<>(1);
    @Override
    public void getBingImageByJsoup(int currentPage){
        Integer maxPage = redisUtil.getValueByKey(RedisConstants.KEY_BACK_BINGIMAGE_TASK_MAXPAGE);
        if (maxPage != null && maxPage.intValue() >= currentPage) {
            return;
        }
        try {
            downloadheaders.put("User-Agent",ContentValue.USER_AGENT);
            Connection connection = Jsoup.connect(ContentValue.BING_IOLIU_URL + "?p=" + currentPage)
                    .header("Referer", ContentValue.BING_IOLIU_URL)
                    .header("User-Agent", ContentValue.USER_AGENT)
                    .timeout(5000);
            Connection.Response response = connection.response();
            response.cookies();
            Document doc = connection.get();
            List<BingImage> images = new ArrayList<>();
            Elements elementPage = doc.getElementsByClass("container");
            elementPage.forEach(item ->{
                Elements itemElements = item.getElementsByClass("item");
                itemElements.forEach(image ->{
                    String imageUrl = image.select("img").attr("src");
                    String description = image.getElementsByClass("description").first().text();
                    String calendar = image.getElementsByClass("calendar").first().select("em").text();
                    String replaceUrl = StringUtils.replace(imageUrl, "640x480", "1920x1080");
                    String hashCode = MD5Util.hashKeyForDisk(replaceUrl);

                    int count = bingImageMapper.selectCountByHashCode(hashCode);
                    if (count != 0){
                        return;
                    }

                    BingImage bingImage = new BingImage();
                    bingImage.setTitle(description);
                    bingImage.setUrlBase(replaceUrl);
                    bingImage.setHashCode(hashCode);
                    bingImage.setStartDate(DateUtil.getDateFromString(calendar,"yyyy-MM-dd"));
                    bingImage.setCreateDate(new Date());

                    //下载文件并上传到oss
                    File localFile = new File(ossProperties.getTemporaryFolder() + hashCode + ".jpg");
                    boolean success = ImageUtil.downloadImageWithHeaders(replaceUrl, "jpg",localFile,downloadheaders);
                    log.info("jsoup：{}, 下载成功：{}",bingImage,success);
                    FileInfo fileInfo = uploadFile(localFile, "bingImage");
                    bingImage.setUrl(fileInfo.getPath());
                    images.add(bingImage);
                });
            });
            bingImageMapper.insertList(images);
            redisUtil.pushValue(RedisConstants.KEY_BACK_BINGIMAGE_TASK_CURRENTPAGE,currentPage,TimeUnit.DAYS.toMillis(30));
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

    @Override
    public PageResult<BingImage> getImageFromDB(PageQueryForm queryForm){
        PageHelper.startPage(queryForm.getPageNum(),queryForm.getPageSize());
        List<BingImage> bingImages = bingImageMapper.selectAll();
        PageInfo<BingImage> pageInfo = PageInfo.of(bingImages);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),bingImages);
    }

    private Random random = new Random();
    @Override
    public String getRandomImage() {
        List<BingImage> bingImages = bingImageMapper.selectAll();
        int nextInt = random.nextInt(bingImages.size());
        return bingImages.get(nextInt).getUrl();
    }
}
