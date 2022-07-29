package com.idc.dao.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DataHtmlEntity {
    private String type;
    private InnerData data;


    public DataHtmlEntity(String type, String name, String num, String unit) {
        this.data = new InnerData(name, num, unit);
        this.type = type;
    }
}
