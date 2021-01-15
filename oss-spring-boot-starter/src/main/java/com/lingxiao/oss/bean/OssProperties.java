package com.lingxiao.oss.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Admin
 */
@ConfigurationProperties(prefix = "oss")
public class OssProperties {
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String prefixDomain;
    private String temporaryFolder;
    private String rootPath;

    public OssProperties() {
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPrefixDomain() {
        return prefixDomain;
    }

    public void setPrefixDomain(String prefixDomain) {
        this.prefixDomain = prefixDomain;
    }

    public String getTemporaryFolder() {
        return temporaryFolder;
    }

    public void setTemporaryFolder(String temporaryFolder) {
        this.temporaryFolder = temporaryFolder;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String toString() {
        return "OssProperties{" +
                "accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", prefixDomain='" + prefixDomain + '\'' +
                ", temporaryFolder='" + temporaryFolder + '\'' +
                ", rootPath='" + rootPath + '\'' +
                '}';
    }
}