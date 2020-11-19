package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.BingImage;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @author renml
 * @date 2020/11/19 16:28
 */
public interface BingImageMapper extends Mapper<BingImage>, InsertListMapper<BingImage> {

}
