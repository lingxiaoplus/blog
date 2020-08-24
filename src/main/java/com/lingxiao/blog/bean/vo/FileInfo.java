package com.lingxiao.blog.bean.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileInfo implements Serializable {
    private String name;
    private String path;
    private String size;
    private String time;
    private String mimeType;
    private String endUser;
    private String bucket;
}
