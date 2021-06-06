package com.lingxiao.blog.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author lingxiao
 */
@Data
public class BaseModel {
    @Id
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createAt;
    private Date updateAt;
}
