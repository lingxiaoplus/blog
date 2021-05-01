package com.lingxiao.blog.utils;

import com.lingxiao.blog.bean.statistics.Memory;
import lombok.Data;

import java.text.DecimalFormat;

public class NumberUtil {

    public static Memory formatByte(long byteNumber) {
        //换算单位
        final double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new Memory(new DecimalFormat("#.##").format(kbNumber),"KB");
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new Memory(new DecimalFormat("#.##").format(mbNumber),"MB");
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new Memory(new DecimalFormat("#.##").format(gbNumber),"GB");
        }
        double tbNumber = gbNumber / FORMAT;
        return new Memory(new DecimalFormat("#.##").format(tbNumber),"TB");
    }

}
