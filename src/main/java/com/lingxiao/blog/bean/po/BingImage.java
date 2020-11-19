package com.lingxiao.blog.bean.po;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author renml
 * @date 2020/11/19 16:26
 */
@Table(name = "bing_image")
@Data
public class BingImage {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String url;
    private String urlBase;
    private String title;
    private String hashCode;
    private Date startDate;
    private Date createDate;
}
