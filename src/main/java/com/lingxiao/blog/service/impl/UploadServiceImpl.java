package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.service.UploadService;
import com.lingxiao.blog.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UploadUtil uploadUtil;
    /**
     * 文件上传，返回oss地址
     * @param file
     * @return
     */
    @Override
    public String uploadFile(File file) {
        return uploadUtil.upload(file.getPath(),file.getName());
    }

    @Override
    public void deleteFile(String fileName) {

    }
}
