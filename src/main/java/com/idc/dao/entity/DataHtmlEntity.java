package com.idc.dao.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DataHtmlEntity {
    //参数名称
    private String name;
    //参数值
    private String num;
    //参数单位
    private String unit;

    public DataHtmlEntity(String name, String num, String unit) {
        this.name = name;
        this.num = num;
        if (StringUtils.isNotBlank(unit)) {
            this.unit = unit;
        }
    }
}
