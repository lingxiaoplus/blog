package com.lingxiao.blog.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    private static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DateUtil(){

    }
    /**
     * .Description://根据字符日期返回星期几
     * .@Date: 2018/12/29
     */
    public static String getWeek(String dateTime){
        String week = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateTime);
            SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
            week = dateFm.format(date);
            week=week.replaceAll("星期","周");
        }catch (ParseException e){
            e.printStackTrace();
        }
        return week;
    }

    /**
     * 获取过去7天内的日期数组
     * @param intervals      intervals天内
     * @return              日期数组
     */
    public static List<String> getDays(int intervals) {
        List<String> pastDaysList = new ArrayList<>();
        for (int i = intervals -1; i >= 0; i--) {
            pastDaysList.add(getPastDate(i));
        }
        return pastDaysList;
    }
    /**
     * 获取过去第几天的日期
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return yearFormat.format(today);
    }

    /**
     *
     * @param date  20201118 这种类型的时间
     * @return
     */
    public static Date getDateFromString(String date){
        String year = StringUtils.left(date, 4);
        String month = StringUtils.substring(date, 4, 6);
        String day = StringUtils.substring(date, 6);
        try {
            return DateFormat.getDateInstance().parse(year.concat("-").concat(month).concat("-").concat(day));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
