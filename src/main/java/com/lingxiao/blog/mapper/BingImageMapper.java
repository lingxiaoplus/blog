package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.BingImage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @author renml
 * @date 2020/11/19 16:28
 */
public interface BingImageMapper extends Mapper<BingImage>, InsertListMapper<BingImage> {
    /**
     * 查询总数
     * @param hashCode
     * @return
     */
    @Select("select count(id) from bing_image where hash_code = #{hashCode}")
    int selectCountByHashCode(@Param("hashCode") String hashCode);
}
