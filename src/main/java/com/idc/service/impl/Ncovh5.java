package com.idc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.constants.URLConstants;
import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.TextIOStreamUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class Ncovh5 {
    // 确诊人数
    private String confirmedCount = "confirmedCount";
    // 新增确诊
    private String confirmedIncr = "confirmedIncr";
    // 治愈人数
    private String curedCount = "curedCount";
    // 新增治愈人数
    private String curedIncr = "curedIncr";
    // 今日确诊人数
    private String currentConfirmedCount = "currentConfirmedCount";
    // 今日新增确诊（今日新增-今日治愈-今日死亡）
    private String currentConfirmedIncr = "currentConfirmedIncr";
    // 日期ID
    private String dateId = "dateId";
    // 死亡人数
    private String deadCount = "deadCount";
    // 新增死亡人数
    private String deadIncr = "deadIncr";
    // 高风险地区
    private String highDangerCount = "highDangerCount";
    // 中风险地区
    private String midDangerCount = "midDangerCount";
    // 疑似病例
    private String suspectedCount = "suspectedCount";
    // 新增疑似病例
    private String suspectedCountIncr = "suspectedCountIncr";

    @Test
    public void test() {
        String url = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
        getJsonUrl(url);
    }

    public void getJsonUrl(String url) {

        String html = HttpWormUtils.getHtml(url);
        Document doc = Jsoup.parse(html);
        Elements fieldList = doc.select("body>script[id]");
        for (int i = 0; i < fieldList.size(); i++) {
            Element field = fieldList.get(i);
            String id = field.attr("id");
            String text = field.childNode(0).toString();

            if (text.contains("window." + id) && text.contains("}catch")) {
                text = text.split("window." + id + " =")[1];
                text = text.split("}catch")[0];
//                if ("getListByCountryTypeService2true".equals(id)) {
//                    getListByCountryTypeService2true(text);
//                }
                if ("getAreaStat".equals(id)) {
                    getAreaStat(text);
                }
            }

        }

        System.out.println(fieldList.size());
    }

    private void getAreaStat(String jsonDataStr) {
        JSONArray jsonDatas = JSONArray.parseArray(jsonDataStr);
        JSONObject provinceinfo = new JSONObject();
        for (int i = 0; i < jsonDatas.size(); i++) {
            JSONObject jsonData = jsonDatas.getJSONObject(i);
            String statisticsData = jsonData.getString("statisticsData");
            String provinceName = jsonData.getString("provinceName");

            JSONObject datas = getStatisticsData(statisticsData);
            TextIOStreamUtils.writeByFileWrite("E:\\deveData\\ncovh5\\province\\" + provinceName + ".json", datas.toJSONString());
            provinceinfo.put(provinceName, jsonData);
        }
        TextIOStreamUtils.writeByFileWrite("E:\\deveData\\ncovh5\\province\\provinceinfo.json", provinceinfo.toJSONString());

    }

    private void getListByCountryTypeService2true(String jsonDataStr) {
        JSONArray jsonDatas = JSONArray.parseArray(jsonDataStr);
        JSONObject countryJson = new JSONObject();
        for (int i = 0; i < jsonDatas.size(); i++) {
            JSONObject jsonData = jsonDatas.getJSONObject(i);
            String continents = jsonData.getString("continents");
            String provinceName = jsonData.getString("provinceName");
            String statisticsData = jsonData.getString("statisticsData");
            JSONObject zhouJson;
            if (countryJson.containsKey(continents)) {
                zhouJson = countryJson.getJSONObject(continents);
            } else {
                zhouJson = new JSONObject();

            }
            JSONObject datas = getStatisticsData(statisticsData);

            TextIOStreamUtils.writeByFileWrite("E:\\deveData\\ncovh5\\country\\" + continents + "\\" + provinceName + ".json", datas.toJSONString());

            zhouJson.put(provinceName, jsonData);
            countryJson.put(continents, zhouJson);
        }

        TextIOStreamUtils.writeByFileWrite("E:\\deveData\\ncovh5\\country\\countryInfo.json", countryJson.toJSONString());
    }

    private JSONObject getStatisticsData(String statisticsData) {
        String html = HttpWormUtils.getHtml(statisticsData);
        JSONObject res = JSONObject.parseObject(html);
        JSONObject ncovData = new JSONObject();

        if (res.containsKey("data")) {
            JSONArray datas = res.getJSONArray("data");
            if (datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    JSONObject data = datas.getJSONObject(i);
                    String dateId = data.getString("dateId");
                    setData(ncovData, data, "confirmedCount");
                    setData(ncovData, data, "confirmedIncr");
                    setData(ncovData, data, "curedCount");
                    setData(ncovData, data, "curedIncr");
                    setData(ncovData, data, "currentConfirmedCount");
                    setData(ncovData, data, "currentConfirmedIncr");
                    setData(ncovData, data, "deadIncr");
                    setData(ncovData, data, "deadCount");
                    setData(ncovData, data, "highDangerCount");
                    setData(ncovData, data, "midDangerCount");
                    setData(ncovData, data, "suspectedCount");
                    setData(ncovData, data, "suspectedCountIncr");
                }

            }
        }
        return ncovData;
    }

    private void setData(JSONObject ncovData, JSONObject data, String id) {
        String dateId = data.getString("dateId");
        Integer countData = data.getInteger(id);
        JSONObject datas;
        if (ncovData.containsKey(id)) {
            datas = ncovData.getJSONObject(id);
        } else {
            datas = new JSONObject();
        }
        datas.put(dateId, countData);
        ncovData.put(id, datas);
    }
}
