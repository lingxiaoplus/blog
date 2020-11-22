package com.lingxiao.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.bean.form.PageQueryForm;
import com.lingxiao.blog.bean.vo.FileInfo;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.OssProperties;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.service.file.FileService;
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
import java.nio.file.Files;

@RestController
@RequestMapping("/upload")
@Api("文件上传")
public class FileController {
    @Autowired
    private FileService uploadService;
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @ApiOperation(value = "上传文件，上传成功返回文件链接")
    @ApiImplicitParam(name = "file",value = "文件")
    @PostMapping
    public ResponseEntity<FileInfo> uploadFile(HttpSession session, @RequestParam(value = "file",required = false) MultipartFile file){
        try {
            String path = session.getServletContext().getRealPath("upload");
            File dir = new File(path);
            if (!dir.exists()){
                if(dir.mkdirs()){
                    dir.setWritable(true);
                }
            }
            File uploadFile = new File(path,file.getOriginalFilename());
            file.transferTo(uploadFile);
            FileInfo fileInfo = uploadService.uploadFile(uploadFile);
            logger.debug("文件路径: {}",uploadFile.getAbsolutePath());
            //上传成功之后，删除本地文件
            Files.deleteIfExists(uploadFile.toPath());
            return ResponseEntity.ok(fileInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
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


    @Deprecated
    @GetMapping("/bing_image")
    public ResponseEntity<ResponseResult<BingImageData>> getBingImages(@RequestParam(value = "idx",defaultValue = "0") int idx){
        return ResponseEntity.ok(new ResponseResult<>(uploadService.getBingImages(idx)));
    }

    @ApiOperation(value = "获取bing日图")
    @ApiImplicitParam(name = "queryForm",value = "查询对象")
    @GetMapping("/bingImage")
    public ResponseEntity<ResponseResult<Object>> getBingImagesFromDB(PageQueryForm queryForm){
        JSONObject object = new JSONObject();
        object.put("images",uploadService.getImageFromDB(queryForm));
        object.put("resolution", ContentValue.SUFFIX_IMAGE_MAP);
        return ResponseEntity.ok(new ResponseResult<>(object));
    }

    @ApiOperation(value = "随机获取一张图")
    @GetMapping(value = "/bingImage/random")
    public ResponseEntity<String> getRandomImage(){
        return ResponseEntity.ok(uploadService.getRandomImage());
    }
}
