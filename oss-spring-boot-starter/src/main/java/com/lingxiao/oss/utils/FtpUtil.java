package com.lingxiao.oss.utils;

import com.lingxiao.oss.FileException;
import com.lingxiao.oss.bean.FtpProperties;
import com.lingxiao.oss.bean.OssFileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 简单操作FTP工具类 ,此工具类支持中文文件名，不支持中文目录
 * 如果需要支持中文目录，需要 new String(path.getBytes("UTF-8"),"ISO-8859-1") 对目录进行转码
 * @author WZH
 * 
 */
@Slf4j
public class FtpUtil {
    private static FtpUtil ftpUtil = new FtpUtil();
    private static FTPClient ftpClient;
    private static FtpProperties ftpProperties;
    private FtpUtil(){}
    public static FtpUtil getInstance(FtpProperties configure){
        ftpProperties = configure;
        if (ftpClient == null){
            ftpClient = ftpUtil.getFTPClient(configure.getHost(), configure.getPort(), configure.getUsername(), configure.getPassword());
        }
        return ftpUtil;
    }

    /**
     * 获取FTPClient对象
     * @param ftpHost 服务器IP
     * @param ftpPort 服务器端口号
     * @param ftpUserName 用户名
     * @param ftpPassword 密码
     * @return FTPClient
     */
    private FTPClient getFTPClient(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword) {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            // 连接FPT服务器,设置IP及端口
            ftp.connect(ftpHost, ftpPort);
            // 设置用户名和密码
            ftp.login(ftpUserName, ftpPassword);
            // 设置连接超时时间,5000毫秒
            ftp.setConnectTimeout(50000);
            // 设置中文编码集，防止中文乱码
            ftp.setControlEncoding("UTF-8");
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                log.info("未连接到FTP，用户名或密码错误");
                ftp.disconnect();
                throw new FileException("未连接到FTP，用户名或密码错误");
            } else {
                log.info("FTP连接成功");
            }

        } catch (SocketException e) {
            e.printStackTrace();
            throw new FileException("FTP的IP地址可能错误，请正确配置");
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException("FTP的端口错误,请正确配置");
        }
        return ftp;
    }
    
    /**
     * 关闭FTP方法
     * @return
     */
    public static boolean closeFTP(){
        if (ftpClient == null){
            return true;
        }
        try {
            ftpClient.logout();
        } catch (Exception e) {
            log.error("FTP关闭失败,{}",e.getMessage());
        }finally{
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    log.error("FTP关闭失败,{}",ioe.getMessage());
                }
            }
        }
        return false;
    }
    
    
    /**
     * 下载FTP下指定文件
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @param downPath 下载保存的目录
     * @return
     */
    public boolean downLoadFTP(String filePath, String fileName, String downPath) {
        // 默认失败
        boolean flag = false;
        try {
            // 跳转到文件目录
            ftpClient.changeWorkingDirectory(filePath);
            // 获取目录下文件集合
            ftpClient.enterLocalPassiveMode();
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    File downFile = new File(downPath + File.separator
                            + file.getName());
                    OutputStream out = new FileOutputStream(downFile);
                    // 绑定输出流下载文件,需要设置编码集，不然可能出现文件为空的情况
                    flag = ftpClient.retrieveFile(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"), out);
                    // 下载成功删除文件,看项目需求
                    // ftp.deleteFile(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
                    out.flush();
                    out.close();
                    if(flag){
                        log.info("下载成功");
                    }else{
                        log.error("下载失败");
                    }
                }
            }

        } catch (Exception e) {
            log.error("下载失败");
        } 

        return flag;
    }

    /**
     * 获取文件url
     * @param filePath  路径
     * @param fileName 文件名
     * @return
     */
    public String getFileUrl(String filePath, String fileName){
        String fileUrl = "";
        try {
            // 跳转到文件目录
            ftpClient.changeWorkingDirectory(filePath);
            // 获取目录下文件集合
            ftpClient.enterLocalPassiveMode();
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    fileUrl = ftpProperties.getBaseUrl().concat(filePath).concat("/").concat(fileName);
                    break;
                }
                //判断为文件夹，递归
                /*if(file.isDirectory()){
                    String path = filePath + file.getName() + "/";
                    getFileUrl(path,fileName);
                }*/
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    /**
     * FTP文件上传工具类
     * @param filePath
     * @param ftpPath
     * @return
     */
    public String uploadFile(String filePath,String ftpPath){
        String fileUrl =  "";
        try {
         // 设置PassiveMode传输  
            ftpClient.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftpClient.setControlEncoding(StandardCharsets.UTF_8.name());
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if(!ftpClient.changeWorkingDirectory(ftpPath)){
                ftpClient.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftpClient.changeWorkingDirectory(ftpPath);

            //上传文件
            File file = new File(filePath);
            try (InputStream in = new FileInputStream(file)){
                String tempName = ftpPath+"/"+file.getName();
                boolean flag = ftpClient.storeFile(new String (tempName.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1),in);
                if(flag){
                    log.info("上传成功");
                    fileUrl = ftpProperties.getBaseUrl().concat(ftpPath).concat("/").concat(file.getName());
                }else{
                    log.error("上传失败");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传失败");
        }
        return fileUrl;
    }
    
    /**
     * FPT上文件的复制
     * @param ftp  FTPClient对象
     * @param olePath 原文件地址
     * @param newPath 新保存地址
     * @param fileName 文件名
     * @return
     */
    public boolean copyFile(FTPClient ftp, String olePath, String newPath,String fileName) {
        boolean flag = false;

        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(olePath);
            //设置连接模式，不设置会获取为空
            ftp.enterLocalPassiveMode();
            // 获取目录下文件集合
            FTPFile[] files = ftp.listFiles();
            ByteArrayInputStream  in = null;
            ByteArrayOutputStream out = null;
            for (FTPFile file : files) {
                // 取得指定文件并下载 
                if (file.getName().equals(fileName)) {
                    
                    //读取文件，使用下载文件的方法把文件写入内存,绑定到out流上
                    out = new ByteArrayOutputStream();
                    ftp.retrieveFile(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"), out);
                    in = new ByteArrayInputStream(out.toByteArray());
                    //创建新目录
                    ftp.makeDirectory(newPath);
                    //文件复制，先读，再写
                    //二进制
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    flag = ftp.storeFile(newPath+File.separator+(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1")),in);
                    out.flush();
                    out.close();
                    in.close();
                    if(flag){
                        log.info("转存成功");
                    }else{
                        log.error("复制失败");
                    }
                    
                    
                }
            }
        } catch (Exception e) {
            log.error("复制失败");
        } 
        return flag;
    }
    
    /**
     * 实现文件的移动，这里做的是一个文件夹下的所有内容移动到新的文件，
     * 如果要做指定文件移动，加个判断判断文件名
     * 如果不需要移动，只是需要文件重命名，可以使用ftp.rename(oleName,newName)
     * @param ftp
     * @param oldPath
     * @param newPath
     * @return
     */
    public boolean moveFile(FTPClient ftp,String oldPath,String newPath){
        boolean flag = false;
        
        try {
            ftp.changeWorkingDirectory(oldPath);
            ftp.enterLocalPassiveMode();
            //获取文件数组
            FTPFile[] files = ftp.listFiles();
            //新文件夹不存在则创建
            if(!ftp.changeWorkingDirectory(newPath)){
                ftp.makeDirectory(newPath);
            }
            //回到原有工作目录
            ftp.changeWorkingDirectory(oldPath);
            for (FTPFile file : files) {

                //转存目录
                flag = ftp.rename(new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"), newPath+File.separator+new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
                if(flag){
                    log.info(file.getName()+"移动成功");
                }else{
                    log.error(file.getName()+"移动失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("移动文件失败");
        }
        return flag;
    }
    
    /**
     * 删除FTP上指定文件夹下文件及其子文件方法，添加了对中文目录的支持
     * @param ftpFolder 需要删除的文件夹
     * @return
     */
    public boolean deleteByFolder(String ftpFolder){
        boolean flag = false;
        try {
            ftpClient.changeWorkingDirectory(new String(ftpFolder.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1));
            ftpClient.enterLocalPassiveMode();
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                //判断为文件则删除
                if(file.isFile()){
                    ftpClient.deleteFile(new String(file.getName().getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1));
                }
                //判断是文件夹
                if(file.isDirectory()){
                    String childPath = ftpFolder + File.separator+file.getName();
                    //递归删除子文件夹
                    deleteByFolder(childPath);
                }
            }
            //循环完成后删除文件夹
            flag = ftpClient.removeDirectory(new String(ftpFolder.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1));
            if(flag){
                log.info(ftpFolder+"文件夹删除成功");
            }else{
                log.error(ftpFolder+"文件夹删除成功");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败");
        }
        return flag;
        
    }
    
    /**
     * 遍历解析文件夹下所有文件
     * @param folderPath 需要解析的的文件夹
     * @return
     */
    public void readFileByFolder(String folderPath, List<OssFileInfo> fileInfoList){
        try {
            ftpClient.changeWorkingDirectory(new String(folderPath.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1));
            //设置FTP连接模式
            ftpClient.enterLocalPassiveMode();
            ftpClient.setCharset(StandardCharsets.UTF_8);
            //获取指定目录下文件文件对象集合
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                //判断为txt文件则解析
                String fileName = file.getName();
                if(file.isFile()){
                    OssFileInfo fileInfo = new OssFileInfo();
                    fileInfo.setName(fileName);
                    fileInfo.setPath(ftpProperties.getBaseUrl().concat(folderPath).concat(fileName));
                    fileInfo.setTime(new SimpleDateFormat("yyyy-MM-dd").format(file.getTimestamp().getTime()));
                    fileInfoList.add(fileInfo);
                }
                //判断为文件夹，递归
                if(file.isDirectory()){
                    String path = folderPath + file.getName() + "/";
                    readFileByFolder(path,fileInfoList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件解析失败: {}",e.getMessage());
        }
    }

    private String utf82Iso(String vaule){
        return new String(vaule.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1);
    }
    private String iso2Utf8(String vaule){
        return new String(vaule.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
    }
    /**
     * 解析txt文本
     */
    private void analysisTxt(FTPFile file){
        String fileName = file.getName();
        if(!file.isFile() && !fileName.endsWith(".txt")){
            return;
        }
        try (InputStream in = ftpClient.retrieveFileStream(new String(file.getName().getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1 ));
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));){
            String temp;
            StringBuilder builder = new StringBuilder();
            while((temp = reader.readLine())!=null){
                builder.append(temp);
            }
            //ftp.retrieveFileStream使用了流，需要释放一下，不然会返回空指针
            ftpClient.completePendingCommand();
            //这里就把一个txt文件完整解析成了个字符串，就可以调用实际需要操作的方法
            log.debug("txt文件：{}",builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}