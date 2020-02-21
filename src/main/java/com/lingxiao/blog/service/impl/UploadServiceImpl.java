package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.service.UploadService;
import com.lingxiao.blog.utils.UploadUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
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
    public FileInfo uploadFile(File file) {
        try {
            return uploadUtil.upload(file);
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            log.error("上传文件失败: {}",r.toString());
            //响应的文本信息
            try {
                System.out.println(r.bodyString());
            } catch (QiniuException ex) {
                ex.printStackTrace();
            }
        }
        return null;
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
        List<FileInfo> fileList = uploadUtil.getFileList("", pageSize*pageNum);
        int totalPage = fileList.size() / pageSize + 1;
        List<FileInfo> subList = fileList.subList((pageSize * (pageNum - 1)), (pageSize * pageNum));
        PageResult<FileInfo> result = new PageResult<>(fileList.size(),totalPage,subList);
        return result;
    }


    @Override
    public void moveOrRenameFile(String oldName,String newName, String toBucket) {
        try {
            uploadUtil.moveOrRenameFile(oldName, newName, toBucket);
        } catch (QiniuException ex) {
            //如果遇到异常，说明移动失败
            //ex.response.error
            log.error("移动/重命名文件错误：错误码：{}，错误详细: {}",ex.code(),ex.response.toString());
            throw new BlogException(ExceptionEnum.MOVE_OR_RENAME_OSS_FILE_ERROR);
        }
    }
}
