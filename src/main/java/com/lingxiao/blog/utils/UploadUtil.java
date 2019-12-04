package com.lingxiao.blog.utils;

import com.lingxiao.blog.global.OssProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties(OssProperties.class)
@Component
public class UploadUtil {
    @Autowired
    private OssProperties ossProperties;
    /**
     *
     * @param filePath 上传文件的路径
     * @param fileName 上传到七牛后保存的文件名
     * @return 返回图片链接
     */
    public String upload(String filePath, String fileName){
        //密钥配置
        Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);
        try {
            //调用put方法上传
            Response res = uploadManager.put(filePath, fileName, getUpToken(auth));
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

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    private String getUpToken(Auth auth) {
        return auth.uploadToken(ossProperties.getBucketName());
    }
}
