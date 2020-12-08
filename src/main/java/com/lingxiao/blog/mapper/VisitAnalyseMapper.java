package com.lingxiao.blog.mapper;

import com.lingxiao.blog.bean.po.VisitAnalyse;
import com.lingxiao.blog.bean.statistics.WeekData;
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

    /**
     * 获取一周的ip访问数据
     * @return
     */
    @Select("SELECT date_format(date,'%Y-%m-%d') `time`, count(id) `count` FROM visit_analyse\n" +
            "        WHERE YEARWEEK(date_format(date,'%Y-%m-%d'),1) = YEARWEEK(now(),1)\n" +
            "        GROUP BY date_format(date,'%Y-%m-%d')")
    List<WeekData> getWeekData();

    /**
     * 获取一个月的省份访问数据
     * @return
     */
    @Select("SELECT count(province) count,province FROM visit_analyse where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(date) group by province")
    List<Map<String,Object>> getProvinceMonthData();
}
