package com.lingxiao.oss.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Admin
 */
public class OssFileInfo implements Serializable {
    private String name;
    private String path;
    private long size;
    private String time;
    private String mimeType;
    private String endUser;
    private String bucket;
    private String fileMd5;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getEndUser() {
        return endUser;
    }

    public void setEndUser(String endUser) {
        this.endUser = endUser;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    @Override
    public String toString() {
        return "OssFileInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", time='" + time + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", endUser='" + endUser + '\'' +
                ", bucket='" + bucket + '\'' +
                ", fileMd5='" + fileMd5 + '\'' +
                '}';
    }
}
