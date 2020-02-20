package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.UploadService;
import com.lingxiao.blog.utils.UploadUtil;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
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
        List<FileInfo> fileList = uploadUtil.getFileList("");
        PageResult<FileInfo> result = new PageResult<>(fileList.size(),1,fileList);
        return result;
    }
}
