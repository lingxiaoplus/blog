package com.lingxiao.blog.service;

import java.io.File;

public interface UploadService {
    String uploadFile(File file);
    void deleteFile(String fileName);
}
