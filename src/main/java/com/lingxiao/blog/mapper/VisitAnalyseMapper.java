package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.VisitAnalyse;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author renml
 * @date 2020/12/7 10:06
 */
public interface VisitAnalyseMapper extends BaseMapper<VisitAnalyse> {
    /**
     * 获取运营商数据分析
     * @return
     */
    @Select("SELECT operators,count(ip) count FROM `visit_analyse` group by operators")
    List<Map<String,Object>> getOperatorsAnalyse();
}
