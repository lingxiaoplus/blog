package com.lingxiao.blog.bean.po;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author renml
 * @date 2020/12/7 10:03
 */
@Data
public class VisitAnalyse {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long ip;
    private String province;
    private String operators;
    private Date date;

}
