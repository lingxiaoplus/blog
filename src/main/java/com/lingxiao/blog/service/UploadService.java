package com.lingxiao.blog.service;

import com.lingxiao.blog.global.api.PageResult;

import java.io.File;
import java.util.List;

public interface UploadService {
    String uploadFile(File file);
    void deleteFile(String fileName);

    PageResult getFileList(String fileName,String date,int pageNum, int pageSize);
}
