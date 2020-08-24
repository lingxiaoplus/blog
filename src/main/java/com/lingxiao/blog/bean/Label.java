package com.lingxiao.blog.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "labels")
public class Label implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @NotBlank(message = "标签名字不能为空")
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createAt;
    private String image;
}
