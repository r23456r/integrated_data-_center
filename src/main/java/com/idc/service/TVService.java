
package com.idc.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.utils.DateUtils;
import com.idc.dao.entity.TVEntity;
import com.idc.dao.entity.TVInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TVService {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2023-01-06");
        String dateStr = format.format(date);
        String format1 = String.format("https://tvbsapp.tvbs.com.tw/pg_api/pg_list/3/%s/1/2", dateStr);
        String srcStr = HttpUtil.get(format1);
        JSONObject jsonObject = JSONObject.parseObject(srcStr);
        jsonObject.put("tvName", "CNN");
        TVEntity entity = new TVEntity();
        JSONArray tvInfoArr = new JSONArray();
        entity.setTvName("TVBS");
        for (int i = 0; i < jsonObject.size(); i++) {
            JSONArray data = JSONArray.parseArray(jsonObject.get("data").toString());
            for (Object datum : data) {
                JSONObject jsonObject1 = JSONObject.parseObject(datum.toString());
                Object date1 = jsonObject1.get("date");
                if (date1.toString().equals(dateStr)) {
                    JSONArray resultData = JSONArray.parseArray(jsonObject1.get("data").toString());
                    for (int i1 = 0; i1 < resultData.size(); i1++) {
                        JSONObject jsonObject2 = JSONObject.parseObject(resultData.get(i1).toString());
                        TVInfo info = new TVInfo();
                        if (i1 + 1 < resultData.size()) {
                            JSONObject nextArr = JSONObject.parseObject(resultData.get(i1 + 1).toString());
                            String endTime = String.valueOf(nextArr.get("pg_hour"));
                            String endTimeBJ = date1.toString() + " " + endTime;
                            info.setEndTimeBJ(endTimeBJ);
                            info.setEndTimePST(DateUtils.BJ2PST(endTimeBJ));
                        }
                        if (i1 == resultData.size() - 1) {
                            String endTime = String.valueOf(jsonObject2.get("pg_hour"));
                            String endTimeBJ = date1.toString() + " " + endTime;
                            info.setEndTimeBJ(DateUtils.NEXTMIDNIGHT(endTimeBJ));
                            info.setEndTimePST(DateUtils.BJ2PST(DateUtils.NEXTMIDNIGHT(endTimeBJ)));
                        }
                        String startTime = String.valueOf(jsonObject2.get("pg_hour"));
                        String programName = String.valueOf(jsonObject2.get("pg_name"));
                        jsonObject2.get("pg_name");
                        String startTimeBJ = date1.toString() + " " + startTime;
                        info.setStartTimeBJ(startTimeBJ);
                        info.setStartTimePST(DateUtils.BJ2PST(startTimeBJ));
                        info.setProgramName(programName);
                        tvInfoArr.add(info);
                    }
                }
            }
            entity.setTvInfo(tvInfoArr);
            System.out.println(JSONObject.toJSONString(entity));
        }
    }

}
