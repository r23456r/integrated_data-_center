package com.idc.common.utils;

import com.alibaba.fastjson.JSONObject;

public class JsonUtils {

    public static JSONObject getValueObject(Object o, String key) {
        Object o1 = JSONObject.parseObject(o.toString()).get(key);
        return JSONObject.parseObject(o1.toString());
    }

    public static String getValue(Object o, String key) {
        Object o1 = JSONObject.parseObject(o.toString()).get(key);
        return o1.toString();
    }
}
