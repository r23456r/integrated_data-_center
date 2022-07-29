package com.idc.dao.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DataHtmlEntity {
    private String type;
    private String countryName;
    //参数名称
    private String name;
    //参数值
    private String num;
    //参数单位
    private String unit;


    public DataHtmlEntity(String countryName, String type, String name, String num, String unit) {
        this.type = type;
        this.countryName = countryName;
        this.name = name;
        this.num = num;
        if (StringUtils.isNotBlank(unit)) {
            this.unit = unit;
        }
    }
}
