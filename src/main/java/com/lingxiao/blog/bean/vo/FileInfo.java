package com.lingxiao.blog.bean.vo;

import lombok.Data;

@Data
public class FileInfo {
    private String name;
    private String path;
    private String size;
    private String time;
    private String mimeType;
    private String endUser;
}
