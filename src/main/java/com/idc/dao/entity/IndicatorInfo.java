package com.idc.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorInfo {
    private String IndicatorName;
    private String countryName;
    private String date;
    private String value;

}
