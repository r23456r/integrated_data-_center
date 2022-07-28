package com.idc.dao.entity;

import lombok.Data;

@Data
public class DataSortEntity {
    String title;
    String description;
    String rank;
    String country;
    String numStr;
    String unit;

    public DataSortEntity(String title, String description, String rank, String country, String numStr, String unit) {
        this.title = title;
        this.description = description;
        this.rank = rank;
        this.country = country;
        this.numStr = numStr;
        this.unit = unit;
    }
}
