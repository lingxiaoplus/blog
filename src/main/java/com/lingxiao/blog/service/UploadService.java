package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.global.api.PageResult;

import java.io.File;
import java.util.List;

public interface UploadService {
    FileInfo uploadFile(File file);
    void deleteFile(String fileName);
    void moveOrRenameFile(String oldName,String newName, String toBucket);
    PageResult getFileList(String fileName,String date,int pageNum, int pageSize);
}
