package com.lingxiao.blog.bean.statistics;

import lombok.Data;

/**
 * @author Admin
 */
@Data
public class Memory{
        private String value;
        private String unit;
        public Memory(String value, String unit) {
            this.value = value;
            this.unit = unit;
        }
}