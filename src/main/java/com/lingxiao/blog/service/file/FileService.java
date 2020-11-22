package com.lingxiao.blog.service.file;

import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.form.PageQueryForm;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.global.OssProperties;
import com.lingxiao.blog.global.api.PageResult;

import java.io.File;

public interface FileService {
    FileInfo uploadFile(File file);
    void deleteFile(String fileName);
    void moveOrRenameFile(String oldName,String newName, String toBucket);
    PageResult<FileInfo> getFileList(String fileName,String date,int pageNum, int pageSize);

    OssProperties getOssProperties();
    BingImageData getBingImages(int idx);

    PageResult<BingImage> getImageFromDB(PageQueryForm queryForm);

    String getRandomImage();
}
