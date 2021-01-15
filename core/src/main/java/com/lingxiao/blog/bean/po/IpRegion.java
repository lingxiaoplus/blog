package com.lingxiao.blog.bean.po;

import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name = "ip_region")
public class IpRegion {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long ipStart;
    private Long ipEnd;
    private String country;
    private String province;
    private String city;
    private String operator;

}
