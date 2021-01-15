package com.lingxiao.blog.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息表(ResourceInfo)实体类
 *
 * @author makejava
 * @since 2021-01-15 13:33:44
 */
@Data
@Table(name = "resource_info")
public class ResourceInfo implements Serializable {
    private static final long serialVersionUID = 264158201678126095L;
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    /**
     * 文件md5
     */
    private String resourceMd5;

    private String name;
    /**
     * 文件路径
     */
    private String path;

    private Long size;

    private String bucket;
    /**
     * 文件上传者
     */
    private String resourceCreator;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createAt;


}