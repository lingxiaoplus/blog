package com.lingxiao.blog.service.file;

import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.form.PageQueryForm;
import com.lingxiao.blog.bean.po.BingImage;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.oss.bean.OssFileInfo;
import com.lingxiao.oss.bean.OssProperties;

import java.io.File;

public interface FileService {
    /**
     * 文件上传，返回oss地址
     * @param file 文件
     * @return
     */
    OssFileInfo uploadFile(File file);
    /**
     * 文件上传，返回oss地址
     * @param file 文件
     * @param folder 文件夹
     * @return
     */
    OssFileInfo uploadFile(File file,String folder);
    void deleteFile(String fileName);
    void moveOrRenameFile(String oldName,String newName, String toBucket);
    PageResult<OssFileInfo> getFileList(String fileName, String date, int pageNum, int pageSize);

    OssProperties getOssProperties();
    BingImageData getBingImages(int idx);

    void getBingImageByJsoup(int currentPage);

    PageResult<BingImage> getImageFromDB(PageQueryForm queryForm);

    String getRandomImage();
}
