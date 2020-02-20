package com.lingxiao.blog.utils;

import com.lingxiao.blog.global.OssProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(OssProperties.class)
@Component
@Slf4j
public class UploadUtil {
    @Autowired
    private OssProperties ossProperties;

    private Auth mAuth;

    @PostConstruct
    private void init(){
        mAuth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
    }
    /**
     *
     * @param filePath 上传文件的路径
     * @param fileName 上传到七牛后保存的文件名
     * @return 返回图片链接
     */
    public String upload(String filePath, String fileName){
        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);
        try {
            //调用put方法上传
            Response res = uploadManager.put(filePath, fileName, getUpToken(mAuth));
            //打印返回的信息
            System.out.println(res.bodyString());
            StringMap jsonToMap = res.jsonToMap();
            String key = (String) jsonToMap.get("key");
            String url = ossProperties.getPrefixImg() + key;
            return url;
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return null;
    }

    /**
     *
     * @param key your file key
     */
    public FileInfo getFileInfo(String key){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        BucketManager bucketManager = new BucketManager(mAuth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(ossProperties.getBucketName(), key);
            System.out.println(fileInfo.hash);
            System.out.println(fileInfo.fsize);
            System.out.println(fileInfo.mimeType);
            System.out.println(fileInfo.putTime);
            return fileInfo;
        } catch (QiniuException ex) {
            log.error("获取文件信息错误：{}",ex.response.toString());
        }
        return null;
    }

    /**
     * 修改文件类型
     * @param fileKey
     * @param mimeType
     * @return
     */
    public boolean changeFileType(String fileKey,String mimeType){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        BucketManager bucketManager = new BucketManager(mAuth, cfg);
        //修改文件类型
        try {
            bucketManager.changeMime(ossProperties.getBucketName(), fileKey, mimeType);
            return true;
        } catch (QiniuException ex) {
            log.error("修改文件类型错误：{}",ex.response.toString());
        }
        return false;
    }


    /**
     *
     * @param oldName
     * @param toBucket  需要移动到的新空间  如果为空默认不移动 只是重命名
     * @param newName
     */
    public boolean moveOrRenameFile(String oldName, String toBucket, String newName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        BucketManager bucketManager = new BucketManager(mAuth, cfg);
        try {
            if (StringUtils.isBlank(toBucket)) toBucket = ossProperties.getBucketName();
            bucketManager.move(ossProperties.getBucketName(), oldName, toBucket, newName);
            return true;
        } catch (QiniuException ex) {
            //如果遇到异常，说明移动失败
            log.error("移动/重命名文件错误：错误码：{}，错误详细: {}",ex.code(),ex.response.toString());
        }
        return false;
    }

    public void deleteFile(String fileKey) throws QiniuException {
        Configuration cfg = new Configuration(Region.region0());
        BucketManager bucketManager = new BucketManager(mAuth, cfg);
        bucketManager.delete(ossProperties.getBucketName(), fileKey);
    }

    /**
     * 返回文件列表
     * @param prefix 文件名前缀
     * @return
     */
    public List<com.lingxiao.blog.bean.vo.FileInfo> getFileList(String prefix){
        List<com.lingxiao.blog.bean.vo.FileInfo> infoList = new ArrayList<>();
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        BucketManager bucketManager = new BucketManager(mAuth, cfg);
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(ossProperties.getBucketName(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            /*FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                System.out.println(item.key);
                System.out.println(item.hash);
                System.out.println(item.fsize);
                System.out.println(item.mimeType);
                System.out.println(item.putTime);
                System.out.println(item.endUser);
            }*/
            List<com.lingxiao.blog.bean.vo.FileInfo> collect =
                    Arrays.stream(fileListIterator.next())
                            .map((item)->{
                                com.lingxiao.blog.bean.vo.FileInfo fileInfo = new com.lingxiao.blog.bean.vo.FileInfo();
                                String name = StringUtils.substringAfterLast(item.key, "/");
                                if (StringUtils.isBlank(name)){
                                    name = item.key;
                                }
                                fileInfo.setName(name);
                                fileInfo.setMimeType(item.mimeType);
                                fileInfo.setPath(ossProperties.getPrefixImg() + item.key);
                                fileInfo.setEndUser(item.endUser);
                                fileInfo.setSize(FileUtil.getFileSize(item.fsize));
                                DateTime dateTime = new DateTime(item.putTime/10000);
                                String dateString = dateTime.toString("yyyy-MM-dd HH:mm:ss");
                                fileInfo.setTime(dateString);
                                return fileInfo;
                            })
                            .collect(Collectors.toList());
            infoList.addAll(collect);
        }
        return infoList;
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    private String getUpToken(Auth auth) {
        return auth.uploadToken(ossProperties.getBucketName());
    }
}
