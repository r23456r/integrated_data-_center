package com.idc.dao.entity;

import lombok.Data;

@Data
public class DataRankOldEntity {
    String rank;
    String country;
    String year;

    public DataRankOldEntity(String rank, String country, String year) {
        this.rank = rank;
        this.country = country;
        this.year = year;
    }
}
