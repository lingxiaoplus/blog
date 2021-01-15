package com.lingxiao.blog.bean.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author renml
 * @date 2020/11/20 9:21
 */
@Data
@ApiModel(value = "分页查询对象")
public class PageQueryForm {
    @ApiModelProperty("页码")
    private int pageNum = 1;

    @ApiModelProperty("每页条数")
    private int pageSize = 5;

    @ApiModelProperty("查询条件")
    private String condition;

}
