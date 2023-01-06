package com.idc.dao.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class TVInfo {
    private String startTimePST;
    private String endTimePST;
    private String startTimeBJ;
    private String endTimeBJ;
    private String programName;
}
