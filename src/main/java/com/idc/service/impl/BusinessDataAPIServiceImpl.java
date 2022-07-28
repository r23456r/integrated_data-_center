//package com.idc.service.impl;
//
//import cn.hutool.core.collection.CollectionUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.idc.common.constants.GuoBieConstants;
//import com.idc.common.constants.URLConstants;
//import com.idc.common.utils.HttpUtils;
//import com.idc.common.utils.TextIOStreamUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service("XHCountryDataService")
//public class BusinessDataAPIServiceImpl {
//    private final static Logger logger = LoggerFactory.getLogger(BusinessDataAPIServiceImpl.class);
//
//    @Test
//    public void getBusinessDataApi() {
//        JSONObject businessData = new JSONObject();
//
//        // 获取综合统计模块的数据
//        JSONObject zongHeData1 = getZongHeData();
//        businessData.put("综合统计", zongHeData1);
//
//        // 获取国别统计模块的数据
//        JSONObject guoBieData = getGuoBieData();
//        businessData.put("国别统计", guoBieData);
//
//        // 获取投资合作模块的数据
//        JSONObject touZiData = getLiYongWaiZiData();
//        businessData.put("利用外资", touZiData);
//
//        // 获取利用外资模块的数据
//
//
//        // 获取服务贸易模块的数据
//
//
//        JSONObject data = new JSONObject();
//        data.put("商务部", businessData);
//
//
//        TextIOStreamUtils.writeByFileWrite("D:\\商务部.json", data.toString());
//    }
//
//    private JSONObject getLiYongWaiZiData() {
//        // 吸收外商直接投资月报
//        Map<String, Object> form = new HashMap<>();
//        String stringMonthData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_TOU_ZI_URL_1, form);
//        JSONArray arrayMonthData = JSONArray.parseArray(stringMonthData);
//        JSONObject idcProjectNumberData = new JSONObject();
//        JSONObject idcProjectPerData = new JSONObject();
//        JSONObject idcActualAmountData = new JSONObject();
//        JSONObject idcActualAmountPerData = new JSONObject();
//        for (int i = 1; i <= 1; i++) {
//            JSONArray jsonArray = arrayMonthData.getJSONArray(1);
//            for (int j = 0; j < jsonArray.size(); j++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(j);
//                String dataTime = jsonObject.getString("data_time");
//                idcProjectNumberData.put(dataTime, jsonObject.getString("project_number"));
//                idcProjectPerData.put(dataTime, jsonObject.getString("project_per"));
//                idcActualAmountData.put(dataTime, jsonObject.getString("actual_amount"));
//                idcActualAmountPerData.put(dataTime, jsonObject.getString("actual_amount_per"));
//            }
//        }
//
//        JSONObject idcProjectNum = new JSONObject();
//        idcProjectNum.put("IDCData", idcProjectNumberData);
//
//
//        JSONObject idcProjectPer = new JSONObject();
//        idcProjectPer.put("IDCData", idcProjectPerData);
//
//        JSONObject idcActualAmount = new JSONObject();
//        idcActualAmount.put("IDCData", idcActualAmountData);
//
//        JSONObject idcActualAmountPer = new JSONObject();
//        idcActualAmountPer.put("IDCData", idcActualAmountPerData);
//
//        JSONObject touZiYueBaoData = new JSONObject();
//        touZiYueBaoData.put("新设立外商投资企业（家）", idcProjectNum);
//        touZiYueBaoData.put("数量同比（%）", idcProjectPer);
//        touZiYueBaoData.put("实际使用外资金额(亿美元)", idcActualAmount);
//        touZiYueBaoData.put("实际使用外资金额同比(%)", idcActualAmountPer);
//        JSONObject xiYinWaiShangZhiJieTouZiYueBao = new JSONObject();
//        xiYinWaiShangZhiJieTouZiYueBao.put("吸收外商直接投资月报", touZiYueBaoData);
//
//        // 吸收外商直接投资按投资方式统计
///*        String stringInvestorData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_TOU_ZI_URL_2, form);
//        JSONObject jsonObject = JSONObject.parseObject(stringInvestorData);*/
//
//        // 外商投资企业进出口统计
//        String stringImportAndExportData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_TOU_ZI_URL_3, form);
//        JSONArray arrayImportAndExportData = JSONArray.parseArray(stringImportAndExportData);
//        if (CollectionUtil.isEmpty(arrayImportAndExportData)) {
//            logger.error("外商投资企业进出口统计数据为空");
//            return new JSONObject();
//        }
//        JSONArray jsonArray = arrayImportAndExportData.getJSONArray(0);
//        if (CollectionUtil.isEmpty(jsonArray)){
//            logger.error("外商投资企业进出口统计数据为空1");
//            return new JSONObject();
//        }
//        JSONObject jsonObject = jsonArray.getJSONObject(1);
//        if (jsonObject == null){
//            logger.error("外商投资企业进出口统计数据为空2");
//            return new JSONObject();
//        }
//        JSONObject data = jsonObject.getJSONObject("data");
//        if (data == null){
//            logger.error("外商投资企业进出口统计数据为空3");
//            return new JSONObject();
//        }
//        JSONObject wsImportJsonObject = new JSONObject();
//        JSONObject wsExportGrouthJsonObject = new JSONObject();
//        JSONObject wsExportJsonObject = new JSONObject();
//        JSONObject wsTotalJsonObject = new JSONObject();
//        JSONObject wsTotalGrouthJsonObject = new JSONObject();
//        JSONObject wsImportGrouthJsonObject = new JSONObject();
//        for (String key : data.keySet()) {
//            JSONObject dataJsonObject = data.getJSONObject(key);
//            wsImportJsonObject.put(key, dataJsonObject.getString("ws_import"));
//            wsExportGrouthJsonObject.put(key, dataJsonObject.getString("ws_export_grouth"));
//            wsExportJsonObject.put(key, dataJsonObject.getString("ws_export"));
//            wsTotalJsonObject.put(key, dataJsonObject.getString("ws_total"));
//            wsTotalGrouthJsonObject.put(key, dataJsonObject.getString("ws_total_grouth"));
//            wsImportGrouthJsonObject.put(key, dataJsonObject.getString("ws_import_grouth"));
//        }
//
//        JSONObject wsImportData = new JSONObject();
//        wsImportData.put("IDCData", wsImportJsonObject);
//
//        JSONObject wsExportGrouthData = new JSONObject();
//        wsExportGrouthData.put("IDCData", wsExportGrouthJsonObject);
//
//        JSONObject wsExportData = new JSONObject();
//        wsExportData.put("IDCData", wsExportJsonObject);
//
//        JSONObject wsTotalData = new JSONObject();
//        wsTotalData.put("IDCData", wsTotalJsonObject);
//
//        JSONObject wsTotalGrouthData = new JSONObject();
//        wsTotalGrouthData.put("IDCData", wsTotalGrouthJsonObject);
//
//        JSONObject wsImportGrouthData = new JSONObject();
//        wsImportGrouthData.put("IDCData", wsImportGrouthJsonObject);
//
//        JSONObject jinKouTongJiJsonObject = new JSONObject();
//        jinKouTongJiJsonObject.put("总值(亿美元)", wsTotalData);
//        jinKouTongJiJsonObject.put("进出口同比(%)", wsTotalGrouthData);
//        jinKouTongJiJsonObject.put("出口总值(亿美元)", wsExportData);
//        jinKouTongJiJsonObject.put("出口同比(%)", wsExportGrouthData);
//        jinKouTongJiJsonObject.put("进口总值(亿美元)", wsImportData);
//        jinKouTongJiJsonObject.put("进口同比(%)", wsImportGrouthData);
//        xiYinWaiShangZhiJieTouZiYueBao.put("外商投资企业进出口统计", jinKouTongJiJsonObject);
//
//
//        /*// 外商投资企业进出口分省市统计
//        String stringPrivinceData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_TOU_ZI_URL_4, form);
//        JSONArray arrayPrivinceData = JSONArray.parseArray(stringPrivinceData);
//        for (int i = 0; i < arrayPrivinceData.size(); i++) {
//
//        }
//
//        // 对华直接投资前十国家和地区
//        String stringCountryData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_TOU_ZI_URL_5, form);
//        JSONArray arrayCountryData = JSONArray.parseArray(stringCountryData);
//        for (int i = 0; i < arrayCountryData.size(); i++) {
//
//        }*/
//
//
//        return xiYinWaiShangZhiJieTouZiYueBao;
//
//    }
//
//    private JSONObject getZongHeData() {
//        JSONObject zongHeData = new JSONObject();
//        String typeNameGDP = "国内生产总值(GDP)";
//        JSONObject GDPData = getGDPDataInfo(typeNameGDP);
//        zongHeData.put(typeNameGDP, GDPData);
//
//        // 获取商务部网站-综合统计-社会消费品零售总额(TRS)
//        String typeNameTRS = "社会消费品零售总额(TRS)";
//        JSONObject TRSData = getTRSDataInfo(typeNameTRS);
//        zongHeData.put(typeNameTRS, TRSData);
//
//        // 获取商务部网站-综合统计-全国企业商品价格指数(CGPI)
//        String typeNameCGPI = "全国企业商品价格指数(CGPI)";
//        JSONObject CGPIData = getCGPIDataInfo(typeNameCGPI);
//        zongHeData.put(typeNameCGPI, CGPIData);
//
//        // 获取商务部网站-综合统计-居民消费价格指数(CPI)
//        String typeNameCPI = "居民消费价格指数(CPI)";
//        JSONObject CPIData = getCPIDataInfo(typeNameCPI);
//        zongHeData.put(typeNameCPI, CPIData);
//
//        // 获取商务部网站-综合统计-工业生产者出厂价格指数
//        String typeNameIndustrial = "工业生产者出厂价格指数";
//        JSONObject IndustrialData = getIndustrialDataInfo(typeNameIndustrial);
//        zongHeData.put(typeNameIndustrial, IndustrialData);
//
//        // 获取商务部网站-综合统计-采购经理人指数(PMI)
//        String typeNamePMI = "采购经理人指数(PMI)";
//        JSONObject PMIData = getPMIDataInfo(typeNamePMI);
//        zongHeData.put(typeNamePMI, PMIData);
//
//        // 获取商务部网站-综合统计-货币供应量
//        String typeNameM2 = "货币供应量";
//        JSONObject M2Data = getM2DataInfo(typeNameM2);
//        zongHeData.put(typeNameM2, M2Data);
//
//        // 获取商务部网站-综合统计-国家外汇储备
//        String typeNameFER = "国家外汇储备";
//        JSONObject FERData = getFERDataInfo(typeNameFER);
//        zongHeData.put(typeNameFER, FERData);
//
//        // 获取商务部网站-综合统计-银行业景气指数
//        String typeNameJqzs = "银行业景气指数";
//        JSONObject JqzsData = getJqzsDataInfo(typeNameJqzs);
//        zongHeData.put(typeNameJqzs, JqzsData);
//
//        // 获取商务部网站-综合统计-银行家宏观经济热度
//        String typeNameHgJjrd = "银行家宏观经济热度";
//        JSONObject HgJjrdData = getHgJjrdDataInfo(typeNameHgJjrd);
//        zongHeData.put(typeNameHgJjrd, HgJjrdData);
//
//        return zongHeData;
//    }
//
//
//    private JSONObject getGuoBieData() {
//        Map<String, Object> form = new HashMap<>();
//        String stringGDPData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_GUO_BIE_URL, form);
//        JSONArray arrayGDPData = JSONArray.parseArray(stringGDPData);
//        JSONObject countryDataJson = new JSONObject();
//        for (int i = 0; i < arrayGDPData.size(); i++) {
//            JSONObject jsonObject = arrayGDPData.getJSONObject(i);
//            String countryName = jsonObject.getString("COUNTRY_NAME");
//            JSONObject counrtryJsonObject = new JSONObject();
//            for (String key : jsonObject.keySet()) {
//                if (StringUtils.equalsAnyIgnoreCase(key, "COUNTRY_NAME", "COUNTRY_ID", "BROADMONEY", "CPICHANGE")) {
//                    continue;
//                }
//                JSONObject idcData = new JSONObject();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//                String dateNowStr = sdf.format(new Date());
//                //
//                idcData.put(dateNowStr, getDataUnit(jsonObject, key));
//                JSONObject keyJsonObject = new JSONObject();
//                keyJsonObject.put("IDCData", idcData);
//                String keyValue = GuoBieConstants.KEY_VALUE_MAP.get(key);
//                counrtryJsonObject.put(keyValue, keyJsonObject);
//            }
//            countryDataJson.put(countryName, counrtryJsonObject);
//        }
//        return countryDataJson;
//    }
//
//    private Double getDataUnit(JSONObject jsonObject, String key) {
//        DecimalFormat df = new DecimalFormat("#.##");
//        if (StringUtils.equalsAnyIgnoreCase(key, "LABOURFORCE", "UNEMPLOYMENT")) {
//            double value = jsonObject.getDouble(key) / 10000;
//            return Double.parseDouble(df.format(value));
//        } else if (StringUtils.equalsAnyIgnoreCase(key, "FOREIGNEXCHANGE", "INTERNATIONALRESERVES", "EXPORTSFOB", "IMPORTSCIF")) {
//            double value = jsonObject.getDouble(key) / 100000000;
//            return Double.parseDouble(df.format(value));
//        } else {
//            return jsonObject.getDouble(key);
//        }
//
//    }
//
//    private JSONObject getDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", 1);
//        String stringGDPData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_GDP_URL, form);
//        JSONArray arrayGDPData = JSONArray.parseArray(stringGDPData);
//
//        JSONObject idcData = new JSONObject();
//        for (int i = 0; i < arrayGDPData.size(); i++) {
//            JSONArray jsonArray = arrayGDPData.getJSONArray(i);
//            for (int j = 0; j < jsonArray.size(); j++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(j);
//                if (jsonObject.containsKey("time_view") || jsonObject.containsKey("value")) {
//                    idcData.put(jsonObject.getString("time_view"), jsonObject.getDouble("value"));
//                }
//            }
//        }
//        JSONObject typeData = new JSONObject();
//        typeData.put("IDCData", idcData);
//        return typeData;
//    }
//
//    private JSONObject getGDPDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "1");
//
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_GDP_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getTRSDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", 2);
//        form.put("module_no", 042);
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_ZHTJ_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getCGPIDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "3");
//        form.put("module_no", "043");
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_ZHTJ_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getCPIDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "4");
//        form.put("module_no", "044");
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_ZHTJ_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getIndustrialDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "5");
//        form.put("module_no", "045");
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_ZHTJ_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getPMIDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "6");
//        form.put("module_no", "046");
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_ZHTJ_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getM2DataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "7");
//        form.put("module_no", "047");
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_ZHTJ_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getFERDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "8");
//        form.put("module_no", "048");
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_ZHTJ_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getJqzsDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "10");
//        form.put("module_no", "049");
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_JQZS_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject getHgJjrdDataInfo(String typeName) {
//        Map<String, Object> form = new HashMap<String, Object>();
//        form.put("type", "11");
//        form.put("module_no", "049");
//        String responseData = HttpUtils.sendPostRequest(URLConstants.BUSINESS_ZONG_HE_JQZS_URL, form);
//        return handleData(typeName, responseData);
//    }
//
//    private JSONObject handleData(String typeName, String responseData) {
//        JSONArray arrayGDPData = JSONArray.parseArray(responseData);
//        JSONObject idcData = new JSONObject();
//        for (int i = 0; i < arrayGDPData.size(); i++) {
//            JSONArray jsonArray = arrayGDPData.getJSONArray(i);
//            for (int j = 0; j < jsonArray.size(); j++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(j);
//                if (jsonObject.containsKey("time_view") || jsonObject.containsKey("value")) {
//                    idcData.put(jsonObject.getString("time_view"), jsonObject.getDouble("value"));
//                }
//            }
//        }
//        JSONObject typeData = new JSONObject();
//        typeData.put("IDCData", idcData);
//        return typeData;
//    }
//}
