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
import com.lingxiao.blog.global.OssProperties;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.BingImageMapper;
import com.lingxiao.blog.service.file.FileService;
import com.lingxiao.blog.utils.UploadUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private UploadUtil uploadUtil;
    /**
     * format js/xml
     * idx 0今天 1明天
     * n 1-8
     */
    private static final String BING_API = "https://cn.bing.com/HPImageArchive.aspx?format=js&n=8&mkt=zh-CN&idx=";
    @Autowired
    private BingImageMapper bingImageMapper;
    /**
     * 文件上传，返回oss地址
     * @param file
     * @return
     */
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
        }
        return null;
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

    @Override
    public PageResult<BingImage> getImageFromDB(PageQueryForm queryForm){
        PageHelper.startPage(queryForm.getPageNum(),queryForm.getPageSize());
        List<BingImage> bingImages = bingImageMapper.selectAll();
        PageInfo<BingImage> pageInfo = PageInfo.of(bingImages);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),bingImages);
    }
}
