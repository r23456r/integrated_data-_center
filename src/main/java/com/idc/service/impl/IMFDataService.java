package com.idc.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.constants.IMFCodeEnum;
import com.idc.common.constants.IMFCodeTransEnum;
import com.idc.common.constants.IMFCountryTransEnum;
import com.idc.common.translate.TransApi;
import com.idc.common.utils.JsonUtils;
import com.idc.common.utils.UtilHandle;
import com.idc.dao.entity.Country;
import com.idc.dao.entity.IndicatorInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IMFDataService {

    private final Map<String, String> countryBiMap = new HashMap<>();

    //    @PostConstruct
    public List<IndicatorInfo> test() {
        List<IndicatorInfo> infoList = new ArrayList<>();
        String url = "https://www.imf.org/external/datamapper/api/index.php?values=%s";
        for (IMFCodeEnum value : IMFCodeEnum.values()) {
            try {
                String indicatorName = value.getCode();
                String jsonVo = HttpUtil.get(String.format(url, indicatorName));
                if (jsonVo.equals("[]")) {
                    continue;
                }
                JSONObject jsonObject = JSONObject.parseObject(jsonVo);
                JSONObject indicatorsObj = JsonUtils.getValueObject(jsonObject, "indicators");
                JSONObject indicatorObj = JsonUtils.getValueObject(indicatorsObj, indicatorName);
                JSONObject valuesObj = JsonUtils.getValueObject(indicatorObj, "values");
                for (String country : valuesObj.keySet()) {
                    String dateAndValueStr = valuesObj.get(country).toString();
                    JSONObject dateAndValueObj = JSONObject.parseObject(dateAndValueStr);
                    for (String s : dateAndValueObj.keySet()) {
                        String dateStr = s;
                        String valueStr = dateAndValueObj.get(s).toString();
                        System.err.println(indicatorName + "||" + countryBiMap.get(country) + "||" + dateStr + "||" + valueStr);
                        infoList.add(new IndicatorInfo(indicatorName, country, dateStr, valueStr));
                    }
                }
            } catch (Exception e) {
                log.error(value + " || " + e.getLocalizedMessage());
            }
        }
        return infoList;
    }


    public void write2Txt(List<IndicatorInfo> infoList) {
        FileUtil.writeString(JSONObject.toJSONString(infoList), "C:\\Users\\PandaIP\\Desktop\\test001.txt", Charset.defaultCharset());
    }

    public List<IndicatorInfo> readFromTxt() {
        String infoStr = FileUtil.readString("C:\\Users\\PandaIP\\Desktop\\test001.txt", Charset.defaultCharset());
        List<IndicatorInfo> infoList = JSON.parseArray(infoStr, IndicatorInfo.class);
        return infoList;
    }

    @PostConstruct
    void initCache() {
        //所有国家
        String jsonVo = HttpUtil.get("https://www.imf.org/external/datamapper/api/?geoitems");
        String geoitems = JSONObject.parseObject(jsonVo).get("geoitems").toString();
        JSONObject countryJson = JSONObject.parseObject(geoitems);
        Set<String> countrySets = countryJson.keySet();
        for (String country : countrySets) {
            Country countyBean = JSONObject.parseObject(countryJson.get(country).toString(), Country.class);
//            System.out.println(country +" "+ countyBean.getLabel());
            countryBiMap.put(country, countyBean.getLabel());
        }
//        List<IndicatorInfo> infoList = test();
//        write2Txt(infoList);
        List<IndicatorInfo> infoList = readFromTxt();
//        infoList = infoList.subList(1, 15000);
        Map<String, List<IndicatorInfo>> collect = infoList.stream().collect(Collectors.groupingBy(IndicatorInfo::getCountryName));
        JSONObject root = new JSONObject();
        for (String countryAbbr : collect.keySet()) {
            JSONObject jsonObject1 = new JSONObject();
            String countryName = countryBiMap.get(countryAbbr);
            List<IndicatorInfo> countryGroupList = collect.get(countryAbbr);
            Map<String, List<IndicatorInfo>> indicatorGroupList = countryGroupList.stream().sorted(Comparator.comparing(IndicatorInfo::getDate)).collect(Collectors.groupingBy(IndicatorInfo::getIndicatorName));

            for (String indicator : indicatorGroupList.keySet()) {
                List<IndicatorInfo> indicatorInfos = indicatorGroupList.get(indicator);
                JSONObject attrA = new JSONObject();
                Map<String, String> dataA = new LinkedHashMap<>();
                String indicatorValue = IMFCodeEnum.getValue(indicator);
                attrA.put("indicator", indicatorValue);
                String value = IMFCodeTransEnum.getCn(indicatorValue);
                attrA.put("indicator_cn", value);
                for (IndicatorInfo bean : indicatorInfos) {
                    dataA.put(bean.getDate(), bean.getValue());
                }
                jsonObject1.put(indicator, UtilHandle.setNodeDataOrdered(attrA, dataA));
            }
            String cn = IMFCountryTransEnum.getCn(countryName);
            if (StringUtils.isNotBlank(cn)) {
                root.put(cn, jsonObject1);
            }
        }
        log.info("treeJsonData: \n" + root.size() + "\n");
        FileUtil.writeBytes(JSONObject.toJSONString(root).getBytes(StandardCharsets.UTF_8), new File("C:\\Users\\PandaIP\\Desktop\\vital.json"));
    }
}
