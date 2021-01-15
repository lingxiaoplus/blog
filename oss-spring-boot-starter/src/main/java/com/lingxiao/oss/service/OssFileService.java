package com.lingxiao.oss.service;


import com.lingxiao.oss.bean.OssFileInfo;
import com.lingxiao.oss.bean.OssProperties;
import com.lingxiao.oss.bean.PageResult;

import java.io.File;
import java.util.List;

/**
 * @author Admin
 */
public interface OssFileService {
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
     * @param convert 是否覆盖相同名字文件
     * @return
     */
    OssFileInfo uploadFile(File file, String folder, boolean convert);

    void deleteFile(String fileName);
    void moveOrRenameFile(String oldName,String newName, String toBucket);

    /**
     * 获取oss上的文件
     * @param filePrefix  文件名前缀  目录
     * @param limit 每次查多少条
     * @return
     */
    List<OssFileInfo> getFileList(String filePrefix, int limit);

    OssProperties getOssProperties();
}