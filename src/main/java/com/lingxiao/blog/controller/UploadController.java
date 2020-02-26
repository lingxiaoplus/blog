package com.lingxiao.blog.controller;

import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.global.OssProperties;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
@Api("文件上传")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    private Logger logger = LoggerFactory.getLogger(UploadController.class);

    @ApiOperation(value = "上传文件，上传成功返回文件链接")
    @ApiImplicitParam(name = "file",value = "文件")
    @PostMapping
    public ResponseEntity<FileInfo> uploadFile(HttpSession session, @RequestParam(value = "file",required = false) MultipartFile file){
        FileInfo fileInfo = null;
        try {
            String path = session.getServletContext().getRealPath("upload");
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
                dir.setWritable(true);
            }
            File uploadFile = new File(path,file.getOriginalFilename());
            file.transferTo(uploadFile);
            fileInfo = uploadService.uploadFile(uploadFile);
            System.out.println("文件路径"+uploadFile.getAbsolutePath());
            logger.debug("文件路径: {}",uploadFile.getAbsolutePath());
            //上传成功之后，删除本地文件
            uploadFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(fileInfo);
    }

    @ApiOperation(value = "oss文件列表")
    @GetMapping("/list")
    public ResponseEntity<PageResult<FileInfo>> getFileList(
            @RequestParam(value = "fileName",defaultValue = "") String fileName,
            @RequestParam(value = "date",defaultValue = "") String date,
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){
        return ResponseEntity.ok(uploadService.getFileList(fileName, date, pageNum, pageSize));
    }

    @ApiOperation(value = "删除oss文件")
    @ApiImplicitParam(name = "fileName",value = "文件名")
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable(value = "fileName") String fileName){
        uploadService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "移动/重命名oss文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName",value = "旧的文件名"),
            @ApiImplicitParam(name = "newName",value = "新的文件名"),
            @ApiImplicitParam(name = "toBucket",value = "需要移动到的bucket")
    })
    @PutMapping("/{fileName}")
    public ResponseEntity<Void> moveOrRenameFile(
            @PathVariable(value = "fileName") String fileName,
            @RequestParam(value = "newName",defaultValue = "") String newName,
            @RequestParam(value = "toBucket",defaultValue = "") String toBucket){
        uploadService.moveOrRenameFile(fileName, newName, toBucket);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/oss_properties")
    public ResponseEntity<ResponseResult<OssProperties>> getOssProperties(){
        return ResponseEntity.ok(new ResponseResult<>(uploadService.getOssProperties()));
    }
}
