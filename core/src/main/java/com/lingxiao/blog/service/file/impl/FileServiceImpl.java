package com.lingxiao.blog.service.file.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.google.gson.Gson;
import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.form.PageQueryForm;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.bean.po.ResourceInfo;
import com.lingxiao.blog.bean.po.User;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.BingImageMapper;
import com.lingxiao.blog.mapper.ResourceInfoMapper;
import com.lingxiao.blog.service.file.FileService;
import com.lingxiao.blog.utils.*;
import com.lingxiao.oss.bean.OssFileInfo;
import com.lingxiao.oss.bean.OssProperties;
import com.lingxiao.oss.service.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Admin
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private BingImageMapper imageMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OssFileService ossFileService;
    @Autowired
    private ResourceInfoMapper resourceInfoMapper;

    /**
     * format js/xml
     * idx 0今天 1明天
     * n 1-8
     */
    private static final String BING_API = "https://cn.bing.com/HPImageArchive.aspx?format=js&n=8&mkt=zh-CN&idx=";

    @Autowired
    private BingImageMapper bingImageMapper;

    @Override
    public OssFileInfo uploadFile(File file) {
        String fileMd5 = FileUtil.getFileMd5(file.getAbsolutePath());
        ResourceInfo select = new ResourceInfo();
        select.setResourceMd5(fileMd5);
        ResourceInfo resourceInfo = resourceInfoMapper.selectOne(select);
        if (resourceInfo != null){
            return resource2OssFile(resourceInfo);
        }
        OssFileInfo ossFileInfo = ossFileService.uploadFile(file);
        ResourceInfo insert = ossFile2Resource(ossFileInfo);
        insert.setResourceMd5(fileMd5);
        resourceInfoMapper.insert(insert);
        return ossFileInfo;
    }

    @Override
    public OssFileInfo uploadFile(File file, String folder) {
        String fileMd5 = FileUtil.getFileMd5(file.getAbsolutePath());
        ResourceInfo select = new ResourceInfo();
        select.setResourceMd5(fileMd5);
        ResourceInfo resourceInfo = resourceInfoMapper.selectOne(select);
        if (resourceInfo != null){
            return resource2OssFile(resourceInfo);
        }
        OssFileInfo ossFileInfo = ossFileService.uploadFile(file);
        ResourceInfo insert = ossFile2Resource(ossFileInfo);
        insert.setResourceMd5(fileMd5);
        resourceInfoMapper.insert(insert);
        return ossFileService.uploadFile(file,folder,true);
    }

    @Override
    public OssProperties getOssProperties() {
        OssProperties ossProperties = ossFileService.getOssProperties();
        ossProperties.setSecretKey("***********");
        return ossProperties;
    }

    private OssFileInfo resource2OssFile(ResourceInfo resourceInfo){
        OssFileInfo ossFileInfo = new OssFileInfo();
        ossFileInfo.setFileMd5(resourceInfo.getResourceMd5());
        ossFileInfo.setSize(resourceInfo.getSize());
        ossFileInfo.setPath(resourceInfo.getPath());
        ossFileInfo.setName(resourceInfo.getName());
        ossFileInfo.setBucket(resourceInfo.getBucket());
        ossFileInfo.setTime(DateUtil.parseDate(resourceInfo.getCreateAt()));
        ossFileInfo.setMimeType(FileUtil.getFileSuffix(resourceInfo.getName()));
        return ossFileInfo;
    }

    private ResourceInfo ossFile2Resource(OssFileInfo ossFileInfo){
        ResourceInfo resourceInfo = new ResourceInfo();
        //resourceInfo.setResourceMd5(ossFileInfo.getFileMd5());
        resourceInfo.setPath(ossFileInfo.getPath());
        resourceInfo.setName(ossFileInfo.getName());
        resourceInfo.setBucket(ossFileService.getOssProperties().getBucketName());
        resourceInfo.setSize(ossFileInfo.getSize());
        User user = SecurityUtil.getCurrentUser();
        if (user != null) {
            resourceInfo.setResourceCreator(user.getUserId()+"");
        }else {
            resourceInfo.setResourceCreator("0");
        }
        resourceInfo.setCreateAt(new Date());
        return resourceInfo;
    }

    @Override
    public void deleteFile(String fileName) {
        ossFileService.deleteFile(fileName);
    }

    @Override
    public PageResult<OssFileInfo> getFileList(String fileName, String date, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ResourceInfo> resourceInfos = resourceInfoMapper.selectAll();
        PageInfo<ResourceInfo> pageInfo = PageInfo.of(resourceInfos);
        List<OssFileInfo> resultList = pageInfo.getList().stream().map(this::resource2OssFile).collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),resultList);
    }


    @Override
    public void moveOrRenameFile(String oldName,String newName, String toBucket) {
        ossFileService.moveOrRenameFile(oldName, newName, toBucket);
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
        if (maxPage == null){
            maxPage = 1;
        }
        if (maxPage != null && maxPage <= currentPage) {
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
                    FileUtil.createFolder(ossFileService.getOssProperties().getTemporaryFolder());
                    //FileUtil.createFolder("D:\\bingImage\\");
                    File localFile = new File(ossFileService.getOssProperties().getTemporaryFolder() + hashCode + ".jpg");
                    boolean success = ImageUtil.downloadImageWithHeaders(replaceUrl, "jpg",localFile,downloadheaders);
                    log.info("jsoup：{}, 下载成功：{}",bingImage,success);
                    OssFileInfo fileInfo = uploadFile(localFile, "bingImage");
                    bingImage.setUrl(fileInfo.getPath());
                    images.add(bingImage);
                    try {
                        Files.delete(localFile.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
            if (!CollectionUtils.isEmpty(images)){
                bingImageMapper.insertList(images);
            }
            currentPage++;
            log.info("当前页：{}",currentPage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getBingImageByJsoup(currentPage);
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
        //PageMethod.startPage(queryForm.getPageNum(),queryForm.getPageSize());
        List<BingImage> bingImages = bingImageMapper.selectByRandom(queryForm.getPageSize());
        //PageInfo<BingImage> pageInfo = PageInfo.of(bingImages);
        return new PageResult<>(1,1,bingImages);
    }

    private Random random = new Random();
    @Override
    public String getRandomImage() {
        List<BingImage> bingImages = bingImageMapper.selectAll();
        int nextInt = random.nextInt(bingImages.size());
        return bingImages.get(nextInt).getUrl();
    }
}
