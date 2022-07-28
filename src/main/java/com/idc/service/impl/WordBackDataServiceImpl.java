package com.idc.service.impl;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.idc.common.constants.URLConstants;
import com.idc.common.utils.HttpUtils;
import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.TextIOStreamUtils;
import com.idc.dao.entity.BankVo;

import com.idc.dao.mapper.IDCNodeInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("WordBackDataServiceImpl")
public class WordBackDataServiceImpl {
    private final static Logger logger = LoggerFactory.getLogger(WordBackDataServiceImpl.class);


    @Autowired
    private IDCNodeInfoMapper idcNodeInfoMapper;


    public void getBusinessDataApi() {
        //调用接口，调用根节点数据银行
        JSONObject businessData = getZongHeData();
        String json = JSON.toJSONString(businessData, SerializerFeature.DisableCircularReferenceDetect);
        TextIOStreamUtils.writeByFileWrite("D:\\世界银行.json", json);
    }

    @Test
    public void getBusinessDataApi1() {
        //美国-纽约联储每周经济指数[WEI] | MacroMicro 财经M平方
        JSONObject businessData = getJjData();
        String json = JSON.toJSONString(businessData, SerializerFeature.DisableCircularReferenceDetect);
        TextIOStreamUtils.writeByFileWrite("D:\\美国-纽约联储每周经济指数[WEI].json", json);
    }



    private JSONObject getJjData() {
        JSONObject bankDataObject = new JSONObject();
        JSONObject bankDataObject1 = new JSONObject();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Cookie", "PHPSESSID=6cf2a9km677l7672dnqi0186v2; _hjSessionUser_1543609=eyJpZCI6ImIxY2M1OGI1LTYyY2QtNTYzZS1iNzBkLWExZjVkMzU0ZWM4NyIsImNyZWF0ZWQiOjE2NTA4NDkwMTMyNjQsImV4aXN0aW5nIjpmYWxzZX0=; _hjFirstSeen=1; _hjSession_1543609=eyJpZCI6IjllNWRiZjAyLTkyMzUtNDk0OS05MGM4LTBmODA4MmIwYjI2ZCIsImNyZWF0ZWQiOjE2NTA4NDkwMTMyNzUsImluU2FtcGxlIjp0cnVlfQ==; _hjIncludedInSessionSample=1; _hjAbsoluteSessionInProgress=0; _ga=GA1.2.387825206.1650849064; _gid=GA1.2.1030049007.1650849064; _gat=1");
        headers.put("authorization", "Bearer 098b0352001095eca3ad477674e09cac");
       // String responseData = HttpUtil.createGet("http://sc.macromicro.me/charts/data/22289").addHeaders(headers).execute().body();
        String responseData = HttpWormUtils.getString("http://sc.macromicro.me/charts/data/22289");
        JSONObject reObject = new JSONObject();
        JSONArray jsonArray = JSONObject.parseObject(responseData).getJSONObject("data").getJSONObject("c:22289").getJSONArray("s");
        JSONArray jsonArrayData = JSONObject.parseArray(jsonArray.get(0).toString());
        JSONObject attribute = new JSONObject();
        JSONObject data = new JSONObject();
        for (Object obj : jsonArrayData) {
            JSONArray jsonArray1 = JSONObject.parseArray(obj.toString());
            data.put(jsonArray1.get(0).toString(), jsonArray1.get(1));
        }
        attribute.put("name", "美国-纽约联储每周经济指数[WEI]");
        reObject.put("IDCData", data);
        reObject.put("IDCAttribute", attribute);
        bankDataObject1.put("美国-纽约联储每周经济指数[WEI]", reObject);
        bankDataObject.put("经济指数", bankDataObject1);
        return bankDataObject;
    }


    private JSONObject getZongHeData() {


        JSONObject bankDataObject = getBackDataInfo();


        return bankDataObject;
    }


    private Double getDataUnit(JSONObject jsonObject, String key) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (StringUtils.equalsAnyIgnoreCase(key, "LABOURFORCE", "UNEMPLOYMENT")) {
            double value = jsonObject.getDouble(key) / 10000;
            return Double.parseDouble(df.format(value));
        } else if (StringUtils.equalsAnyIgnoreCase(key, "FOREIGNEXCHANGE", "INTERNATIONALRESERVES", "EXPORTSFOB", "IMPORTSCIF")) {
            double value = jsonObject.getDouble(key) / 100000000;
            return Double.parseDouble(df.format(value));
        } else {
            return jsonObject.getDouble(key);
        }

    }


    private JSONObject getBackDataInfo() {
        Map<String, Object> form = new HashMap<String, Object>();
        JSONObject object = getTreeDataInfo("2", "WDI_Series", "[WDI_Series].[Topic]", "World Development Indicators");
        return object;
    }

    private JSONObject getTreeDataInfo(String cubeid, String controlid, String hierarchy, String typeName) {
        Map<String, Object> form = new HashMap<String, Object>();
        form.put("cubeid", cubeid);
        form.put("controlid", controlid);
        form.put("hierarchy", hierarchy);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Cookie", "ASP.NET_SessionId=mukbrwwbzw5ejahuijz1u1qu; databank.cookie=853586186.20992.0000; BIGipServerdatabank-https.sfarm=1621211146.47873.0000; AMCVS_1E7B833554B8360D0A4C98A5@AdobeOrg=1; preview=N; _gcl_au=1.1.921302407.1650511852; cebs=1; _ce.s=v~abf7271d2bfcb69fbf64f88e409004242f1d3b30~vpv~0; s_cc=true; AMCV_1E7B833554B8360D0A4C98A5@AdobeOrg=-2121179033|MCIDTS|19104|MCMID|67555276208064585623908778933677744800|MCAAMLH-1651125266|11|MCAAMB-1651125266|RKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y|MCOPTOUT-1650527666s|NONE|MCAID|NONE|vVersion|5.3.0; TS015f8521=017189f947d221f5020354dd64f95f7d2e0a2483e31b9e1bc55ac08444e528f23908119b4af54d159195deb066e961e7ff617ea46bf12c29621ae7fb18f88dacab861ec6f3b5d854ebd0511cc49e6a1f4578da00cfd00aefe01c8a3810735b3d6e7b2cbad9dc2a4a033cb3ce06774db622aac0e9f6; s_vnum=1682047857512&vn=2; s_invisit=true; s_lv_s=Less than 1 day; s_ppn=en:wb:developmentdata:/source/world-development-indicators; uvts=9c26710e-2314-4aca-4a19-c5bc3b7aa867; TS01b02907=01ebd1be3b2d2f6384600af484354dab2d460665520db228bb338c8322583546ce6129159fda9e13de74c67ff8f747bd249b6542bdc875643def0c41f207c23c19cda84bde0fa230caa862463bd25800e2135358ff; TS018f8378=01359ee9764a9a999127937389b6f592d6d71230880193d04be4150720aa5002127126d3387a3c5c81707161ad5cb42a09edab054bcc436a0f19d1eb0c5ec736288e5b3b22; ADRUM=s=1650526993837&r=https://databank.worldbank.org/source/world-development-indicators?35; s_ppvl=en%3Awb%3Adevelopmentdata%3A/source/world-development-indicators,100,100,746,1536,746,1536,864,1.25,L; s_ptc=[[B]]; s_ppv=en%3Awb%3Adevelopmentdata%3A/source/world-development-indicators,76,76,754.8000001907349,522,746,1536,864,1.25,P; s_nr=1650527010892-Repeat; s_lv=1650527010893; s_sq=[[B]]");
        String responseData = HttpUtils.sendPostRequest(URLConstants.word_develop_Indicators, form, headers);
        JSONObject reObject = new JSONObject();

        reObject.put(typeName, new JSONObject());
        JSONArray jsonArray = JSONObject.parseObject(responseData).getJSONObject("d").getJSONArray("Levels");

        JSONArray jsonArrayMembers = JSONObject.parseObject(responseData).getJSONObject("d").getJSONArray("Members");
        JSONObject attribute = new JSONObject();
        for (Object object : jsonArrayMembers) {
            JSONObject object1 = JSONObject.parseObject(object.toString());

            String name = object1.getString("Name");
            String mecode = object1.getString("Code");

            JSONArray jsonArray1 = object1.getJSONArray("HLS");
            if (jsonArray1 != null && jsonArray1.size() > 0) {
                String code = jsonArray1.getString(jsonArray1.size() - 1);
                if (attribute.containsKey(code)) {
                    attribute.getJSONObject(code).put("name", name);
                    attribute.getJSONObject(code).put("mecode", mecode);
                } else {
                    JSONObject object2 = new JSONObject();
                    object2.put("mecode", mecode);
                    object2.put("name", name);
                    attribute.put(code, object2);
                }
            }
        }
        //这里处理数据和属性
        List<BankVo> list = idcNodeInfoMapper.selectAllBankData();
        Map<String, List<BankVo>> newDataMap = list.stream().collect(Collectors.groupingBy(BankVo::getCountryName));
        JSONObject dataObject = handleData(attribute, newDataMap);

        JSONObject fatherObjcet = reObject.getJSONObject(typeName);
        handleTree(fatherObjcet, jsonArray, dataObject);
        return reObject;
    }

    private JSONObject handleData(JSONObject attribute, Map<String, List<BankVo>> newDataMap) {
        JSONObject object = new JSONObject();


        for (String hid : attribute.keySet()) {
            JSONObject childObject1 = new JSONObject();
            JSONObject childObject2 = new JSONObject();
            JSONObject childObject3 = new JSONObject();
            JSONObject iDCDataObject = new JSONObject();
            JSONObject IDCAttributeObject = new JSONObject();
            JSONObject jsonObject = attribute.getJSONObject(hid);
            String name = jsonObject.getString("name");
            String code = jsonObject.getString("mecode");
            IDCAttributeObject.put("name", name);

            for (Map.Entry<String, List<BankVo>> keyValue : newDataMap.entrySet()) {
                String gjName = keyValue.getKey();
                List<BankVo> bankVoList = keyValue.getValue();
                Map<String, BankVo> bankVoMap = bankVoList.stream().collect(Collectors.toMap(BankVo::getSeriesCode, Function.identity()));
                if (bankVoMap.containsKey(code)) {
                    iDCDataObject.put("2016", bankVoMap.get(code).getData2016());
                    iDCDataObject.put("2017", bankVoMap.get(code).getData2017());
                    iDCDataObject.put("2018", bankVoMap.get(code).getData2018());
                    iDCDataObject.put("2019", bankVoMap.get(code).getData2019());
                    iDCDataObject.put("2020", bankVoMap.get(code).getData2020());
                    childObject2.put("IDCData", iDCDataObject);
                    childObject2.put("IDCAttribute", IDCAttributeObject);
                }
                childObject3.put(name, childObject2);
                childObject1.put(gjName, childObject3);
            }


            object.put(hid, childObject1);
        }
        return object;
    }

    private void handleTree(JSONObject fatherObjcet, JSONArray jsonArray, JSONObject attribute) {
        for (Object item : jsonArray) {
            JSONObject jsonObject = JSONObject.parseObject(item.toString());
            String name = jsonObject.getString("HN");
            String code = jsonObject.getString("HID");

            fatherObjcet.put(name, new JSONObject());
            JSONArray jsonArray1 = jsonObject.getJSONArray("LV");
            //获取这个节点对应的子节点
            if (jsonArray1 != null && jsonArray1.size() > 0) {
                JSONObject child = fatherObjcet.getJSONObject(name);
                handleTree(child, jsonArray1, attribute);
            } else {
                if (attribute.containsKey(code)) {

                    fatherObjcet.put(name, attribute.getJSONObject(code));
                }
            }

        }
    }

}
