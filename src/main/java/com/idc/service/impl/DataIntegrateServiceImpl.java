package com.idc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.constants.URLConstants;
import com.idc.common.utils.HttpUtils;
import com.idc.common.utils.JsonVoConvertUtils;
import com.idc.common.utils.TextIOStreamUtils;
import com.idc.common.utils.UtilHandle;
import com.idc.dao.entity.BankVo;
import com.idc.dao.entity.DataSourceInfo;
import com.idc.dao.entity.IDCNodeDataVo;
import com.idc.dao.entity.IDCNodeInfoVo;
import com.idc.dao.mapper.DataSourceInfoMapper;
import com.idc.dao.mapper.IDCNodeDataMapper;
import com.idc.dao.mapper.IDCNodeInfoMapper;
import com.idc.service.DataIntegrateService;
import com.idc.vo.IntegratedDataVo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("DataIntegrateServiceImpl")
public class DataIntegrateServiceImpl implements DataIntegrateService {

    @Autowired
    private IDCNodeInfoMapper idcNodeInfoMapper;

    @Autowired
    private IDCNodeDataMapper idcNodeDataMapper;

    @Autowired
    private DataSourceInfoMapper dataSourceInfoMapper;

    @Override
    public boolean insertData(String dataSource, JSONObject jsonData) {
        // 查询表名称
        DataSourceInfo dataSourceInfo = dataSourceInfoMapper.selectDataByDataSource(dataSource);
        if (dataSourceInfo == null) {
            return false;
        }
        if (jsonData == null || jsonData.isEmpty()) {
            return false;
        }
        IntegratedDataVo integratedDataVo = JsonVoConvertUtils.jsonToVo(jsonData, dataSource);
        List<IDCNodeInfoVo> idcNodeInfoVos = idcNodeInfoMapper.selectAllIDCNodeInfos(dataSourceInfo.getNodeInfoTblName());
        List<IDCNodeDataVo> idcNodeDataVos = idcNodeDataMapper.selectAllIDCNodeDatas(dataSourceInfo.getNodeDataTblName());
        UtilHandle.compareIDCNode(idcNodeInfoVos, idcNodeDataVos, integratedDataVo);
        // 数据更新
        updateDatastoDB(dataSourceInfo, integratedDataVo);
        return true;
    }

    @Test
    public void test(){
        String jsonStr= getAlldata("GlobalFirePower");

//        System.out.println(jsonObject.toJSONString());
    }

    @Override
    public String getAlldata(String dataSource) {

        // 查询表名称
        DataSourceInfo dataSourceInfo = dataSourceInfoMapper.selectDataByDataSource(dataSource);
        if (dataSourceInfo == null) {
            return "";
        }

        List<IDCNodeInfoVo> idcNodeInfoVos = idcNodeInfoMapper.selectAllIDCNodeInfos(dataSourceInfo.getNodeInfoTblName());
        List<IDCNodeDataVo> idcNodeDataVos = idcNodeDataMapper.selectAllIDCNodeDatas(dataSourceInfo.getNodeDataTblName());

        return JsonVoConvertUtils.voToJson(idcNodeInfoVos, idcNodeDataVos).toJSONString();
    }

    private void updateDatastoDB(DataSourceInfo dataSourceInfo, IntegratedDataVo integratedDataVo) {
        if (integratedDataVo.getNodeInfos() != null && !integratedDataVo.getNodeInfos().isEmpty()) {
            batchIDCNodeInfos(dataSourceInfo.getNodeInfoTblName(), integratedDataVo.getNodeInfos(), "insert");
        }
        if (integratedDataVo.getNodeDatas() != null && !integratedDataVo.getNodeDatas().isEmpty()) {
            batchIDCNodeDatas(dataSourceInfo.getNodeDataTblName(), integratedDataVo.getNodeDatas(), "insert");
        }
        if (integratedDataVo.getUpdateNodeInfos() != null && !integratedDataVo.getUpdateNodeInfos().isEmpty()) {
            batchIDCNodeInfos(dataSourceInfo.getNodeInfoTblName(), integratedDataVo.getUpdateNodeInfos(), "update");
        }
        if (integratedDataVo.getUpdateNodeDatas() != null && !integratedDataVo.getUpdateNodeDatas().isEmpty()) {
            batchIDCNodeDatas(dataSourceInfo.getNodeDataTblName(), integratedDataVo.getUpdateNodeDatas(), "update");
        }
    }

    private void batchIDCNodeInfos(String tableName, List<IDCNodeInfoVo> batchList, String type) {
        int length = batchList.size();
        int groupSize = 10000;
        int num = (length + groupSize - 1) / groupSize;
        for (int i = 0; i < num; i++) {
            int fromIndex = i * groupSize;
            int toIndex = (i + 1) * groupSize < length ? (i + 1) * groupSize : length;
            if ("insert".equals(type)) {
                idcNodeInfoMapper.batchInsertIDCNodeInfos(tableName, batchList.subList(fromIndex, toIndex));
            } else {
                idcNodeInfoMapper.batchUpdateIDCNodeInfos(tableName, batchList.subList(fromIndex, toIndex));
            }

        }
    }

    private void batchIDCNodeDatas(String tableName, List<IDCNodeDataVo> batchList, String type) {
        int length = batchList.size();
        int groupSize = 10000;
        int num = (length + groupSize - 1) / groupSize;
        for (int i = 0; i < num; i++) {
            int fromIndex = i * groupSize;
            int toIndex = (i + 1) * groupSize < length ? (i + 1) * groupSize : length;
            if ("insert".equals(type)) {
                idcNodeDataMapper.batchInsertIDCNodeDatas(tableName, batchList.subList(fromIndex, toIndex));
            } else {
                idcNodeDataMapper.batchUpdateIDCNodeDatas(tableName, batchList.subList(fromIndex, toIndex));
            }
        }
    }


    @Test
    public void getBusinessDataApi() {
        //调用接口，调用根节点数据银行
        JSONObject businessData = getZongHeData();
        TextIOStreamUtils.writeByFileWrite("D:\\世界银行.json", businessData.toString());
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
                    attribute.getJSONObject(code).put(name, mecode);
                } else {
                    JSONObject object2 = new JSONObject();
                    object2.put(name, mecode);
                    attribute.put(code, object2);
                }
            }
        }
        //这里处理数据和属性
        List<BankVo> list = idcNodeInfoMapper.selectAllBankData();
        Map<String, List<BankVo>> newDataMap = list.stream().collect(Collectors.groupingBy(BankVo::getCountryName));
        JSONObject dataObject = handleData(attribute, list, newDataMap);

        JSONObject fatherObjcet = reObject.getJSONObject(typeName);
        handleTree(fatherObjcet, jsonArray, dataObject);
        return reObject;
    }

    private JSONObject handleData(JSONObject attribute, List<BankVo> list, Map<String, List<BankVo>> newDataMap) {
        JSONObject object = new JSONObject();


        for (String key : attribute.keySet()) {
            JSONObject childObject1 = new JSONObject();
            JSONObject childObject2 = new JSONObject();
            JSONObject iDCDataObject = new JSONObject();
            JSONObject IDCAttributeObject = new JSONObject();
            IDCAttributeObject.put("name", key);
            String code = attribute.getString(key);
            childObject2.put("IDCAttribute", IDCAttributeObject);
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
                }
                childObject2.put(key, childObject2);
                childObject1.put(gjName, childObject2);

            }
            object.put(key, childObject1);
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
