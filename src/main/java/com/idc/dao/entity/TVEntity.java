package com.idc.dao.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class TVEntity {
    private String tvName;
    private JSONArray tvInfo;
}
