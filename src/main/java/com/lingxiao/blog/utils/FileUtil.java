package com.lingxiao.blog.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    public static void isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (downloadFile.exists()) {
            if (downloadFile.isFile()){
                downloadFile.delete();
                downloadFile.mkdirs();
            }
        }else {
            downloadFile.mkdirs();
        }
    }


    /**
     * 拷贝文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            input.close();
            output.close();
        } catch (IOException e){
            e.printStackTrace();
            if (input != null){
                input.close();
            }
            if (output != null){
                output.close();
            }
        }
    }



    /**
     * 获取文件夹下所有文件集合
     * @param dirPath
     * @return
     */
    public static List<File> getFiles(String dirPath) {
        try {
            File file = new File(dirPath);
            List<File> fileList = new ArrayList<>();
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    fileList.add(f);
                } else {
                    getFiles(f.getAbsolutePath());
                }
            }
            return fileList;
        } catch (NullPointerException e) {
            //ToastUtils.show("出错了："+e.getMessage());
            //Log.i("seedownloadimgact", "出错了");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件大小
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        size = size / 1024 / 1024; //mb
        return size;
    }

    /**
     * 获取文件大小
     * @param
     * @return
     */
    public static String getFileSize(long size) {
        NumberFormat ddf = NumberFormat.getNumberInstance() ;
        ddf.setMaximumFractionDigits(2);
        String fileSize = ddf.format(size) + "b";
        if (size > 1024L){
            double size1 = size/1024d;
            fileSize = ddf.format(size1) + "kb";
            if (size1 > 1024d){
                fileSize = ddf.format(size1/1024d) + "mb";
            }
        }
        return fileSize;
    }
}