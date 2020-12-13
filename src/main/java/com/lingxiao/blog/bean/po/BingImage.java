package com.lingxiao.blog.bean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author renml
 * @date 2020/11/19 16:26
 */
@Table(name = "bing_image")
@Data
@ToString
public class BingImage implements Serializable {
    private static final long serialVersionUID = 4920600747801363341L;
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String url;
    private String urlBase;
    private String title;
    private String hashCode;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createDate;
}
