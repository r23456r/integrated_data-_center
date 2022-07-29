package com.idc.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.translate.TransApi;
import com.idc.common.translate.TransVo;
import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.TextIOStreamUtils;
import com.idc.common.utils.UtilHandle;
import com.idc.dao.entity.DataHtmlEntity;
import com.idc.dao.entity.DataRankOldEntity;
import com.idc.dao.entity.DataSortEntity;
import com.idc.dao.entity.WtoBean;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class GlobalFirePowerDataService {
    private Set<String> transSet;
    private Map<String, String> transResultMap = new HashMap<>();
    private List<DataHtmlEntity> htmlList = new ArrayList<>();
    private List<DataSortEntity> sortList = new ArrayList<>();
    private List<DataRankOldEntity> rankOldList = new ArrayList<>();
    Pattern upperCasePattern = Pattern.compile("^[A-Z]+$");

    @Test
    public void test() {

        getHtmlData();
        getSortData();
        getRankOdlData();
        transData();
        htmlList.forEach(dataHtmlEntity -> {dataHtmlEntity.setName(transResultMap.get(dataHtmlEntity.getName()).replace("：", ""));});
        sortList.forEach(dataSortEntity -> {dataSortEntity.setCountry(transResultMap.get(dataSortEntity.getCountry()).replace("：", ""));});
        rankOldList.forEach(addRankOldEntity -> {
            addRankOldEntity.setCountry(transResultMap.get(addRankOldEntity.getCountry()).replace("：", ""));
        });

//        //中英文翻译
        FileUtil.writeBytes(JSONObject.toJSONString(transResultMap).getBytes(StandardCharsets.UTF_8), new File("C:\\Users\\cetc15\\Desktop\\trans.json"));
        FileUtil.writeBytes(JSONObject.toJSONString( writeJson2File(htmlList)).getBytes(StandardCharsets.UTF_8), new File("C:\\Users\\cetc15\\Desktop\\html.json"));
        FileUtil.writeBytes(JSONObject.toJSONString( writeJson2File(sortList)).getBytes(StandardCharsets.UTF_8), new File("C:\\Users\\cetc15\\Desktop\\sort.json"));
        FileUtil.writeBytes(JSONObject.toJSONString( writeJson2File(rankOldList)).getBytes(StandardCharsets.UTF_8), new File("C:\\Users\\cetc15\\Desktop\\oldRank.json"));

    }

    private void getRankOdlData() {
        File fload = new File("E:\\deveData\\global\\rankOdl.html");
        String html = TextIOStreamUtils.readerFile(fload.getAbsolutePath());
        analysisRankOdlData(html);
    }

    JSONObject writeJson2File(List<?> list) {
        Class<?> aClass = list.get(0).getClass();
        String className = aClass.getName();
        JSONObject treeJsonData = new JSONObject();
        switch (className) {
            case "com.idc.dao.entity.DataHtmlEntity":
                List<DataHtmlEntity> tmpList = (List<DataHtmlEntity>) list;
                Map<String, List<DataHtmlEntity>> collect = tmpList.stream().collect(Collectors.groupingBy(DataHtmlEntity::getCountryName));
                for (String indicator : collect.keySet()) {
                    List<DataHtmlEntity> groupByBeans = collect.get(indicator);
                    // TODO: 2022/7/29 修改为中文
                    JSONObject dataA = new JSONObject();
                    JSONObject attrA = new JSONObject();
                    for (DataHtmlEntity bean : groupByBeans) {
                        JSONObject attrB = new JSONObject();
                        attrB.put("unit", bean.getUnit());
                        attrB.put("itemName", bean.getName());
                        attrB.put("countryName", bean.getCountryName());
                        attrB.put("type", bean.getType());
                        JSONObject resultB = UtilHandle.setNodeInfo(attrB, UtilHandle.createDataByYYYYMM(new Date(), bean.getNum()));
                        resultB.put("IDCType", 1);
                        dataA.put(bean.getName(), resultB);
                    }
                    attrA.put("itemName", indicator);
                    treeJsonData.put(indicator, UtilHandle.setNodeData(attrA, dataA));
                }
                log.info("treeJsonData: \n" + treeJsonData + "\n");
                break;
            case "com.idc.dao.entity.DataSortEntity":
                List<DataSortEntity> tmpSortList = (List<DataSortEntity>) list;
                Map<String, List<DataSortEntity>> collect1 = tmpSortList.stream().collect(Collectors.groupingBy(DataSortEntity::getCountry));
                for (String indicator : collect1.keySet()) {
                    List<DataSortEntity> groupByBeans = collect1.get(indicator);
                    // TODO: 2022/7/29 修改为中文
                    JSONObject dataA = new JSONObject();
                    JSONObject attrA = new JSONObject();
                    for (DataSortEntity bean : groupByBeans) {
                        JSONObject attrB = new JSONObject();
                        attrB.put("unit", bean.getUnit());
                        attrB.put("countryName", bean.getCountry());
                        attrB.put("desc", bean.getDescription());
                        attrB.put("title", bean.getTitle());
                        JSONObject resultB = UtilHandle.setNodeInfo(attrB, UtilHandle.createDataByYYYYMM(new Date(), bean.getRank()));
                        resultB.put("IDCType", 1);
                        dataA.put(bean.getTitle(), resultB);
                    }
                    attrA.put("itemName", indicator);
                    treeJsonData.put(indicator, UtilHandle.setNodeData(attrA, dataA));
                }
                log.info("treeJsonData: \n" + treeJsonData + "\n");
                break;
            case "com.idc.dao.entity.DataRankOldEntity":
                List<DataRankOldEntity> tmpRankList = (List<DataRankOldEntity>) list;
                Map<String, List<DataRankOldEntity>> collect2 = tmpRankList.stream().collect(Collectors.groupingBy(DataRankOldEntity::getCountry));
                for (String indicator : collect2.keySet()) {
                    List<DataRankOldEntity> groupByBeans = collect2.get(indicator);
                    // TODO: 2022/7/29 修改为中文
                    JSONObject dataA = new JSONObject();
                    JSONObject attrA = new JSONObject();
                    for (DataRankOldEntity bean : groupByBeans) {
                        dataA.put(bean.getYear(), bean.getRank());
                    }
                    attrA.put("itemName", indicator);
                    attrA.put("src", "GlobalFirepower.com Ranks");
                    treeJsonData.put(indicator, UtilHandle.setNodeDataAndType(attrA, dataA, 1));
                }
                log.info("treeJsonData: \n" + treeJsonData + "\n");
            default:
                break;
        }
        return treeJsonData;
    }

    private void analysisRankOdlData(String html) {
        Document doc = Jsoup.parse(html);
        Elements eles = doc.select("div[class^=mainLists");
        for (Element ele : eles) {
            String year = ele.select("span[class$=textLarger textBold]").text();
            Elements ranks = ele.select("div[class^=rnkNum");
            Elements countries = ele.select("div[class^=countryName").select("span[class$=textNormal]");
////            System.out.printf("%s   ------   %s--------%s", year, ranks.text(), countries.text());
            for (int i = 0; i < countries.size(); i++) {
                String country = countries.get(i).text();
                country = getEndStr(country);
                addTransData(country);
                addRankOldEntity(ranks.get(i).text(), country, year);
            }
        }
    }

    /**
     * 屏蔽国家缩写
     *
     * @param country
     * @return
     */
    private String getEndStr(String country) {
        String[] split = country.split(" ");
        String split_2 = split[split.length - 1];
        if (matchUpperCase(split_2)) {
            country = country.replace(split_2, "").trim();
        }
        return country;
    }

    private void addRankOldEntity(String rank, String country, String year) {

        rankOldList.add(new DataRankOldEntity(rank, country, year));
    }

    public void getSortData() {
        File fload = new File("E:\\deveData\\global\\sort");
        for (File file : fload.listFiles()) {
//            System.out.println("=====================================current File:  " + file.getName());
            String html = TextIOStreamUtils.readerFile(file.getAbsolutePath());
            analysisSortData(html);
        }
    }

    /**
     * 仅匹配大写字母 排除国家缩写
     *
     * @param str
     * @return
     */
    public boolean matchUpperCase(String str) {
        Matcher matcher = upperCasePattern.matcher(str);
        return matcher.find();
    }

    public void getHtmlData() {
        File fload = new File("E:\\deveData\\global\\html");
        for (File htmlFile : fload.listFiles()) {
//            System.out.println("=====================================current File:  " + htmlFile.getName());
            String html = TextIOStreamUtils.readerFile(htmlFile.getAbsolutePath());
            String countyName = htmlFile.getName().replace(".html", "");
            countyName = getEndStr(countyName);
            addTransData(countyName);
            Element mianEl = getHtmlMainEl(html);
            analysisHtmlData(countyName, mianEl);
        }

//        File[] files = new File("E:\\deveData\\global\\sort").listFiles();
//        List<String> fileNames = new ArrayList<>();
//        for (File file : files) {
//            fileNames.add(file.getName());
//        }
//        for (String str : sortUrlList) {
//            String fileName = str.replace("/", "").replace(".php", "") + ".html";
//            if (!fileNames.contains(fileName)) {
//                String html = HttpWormUtils.getHtml( "https://www.globalfirepower.com/"+ str);
//                TextIOStreamUtils.writeByFileWrite("E:\\deveData\\global\\sort\\" + fileName, html);
////                System.out.println(str);
//            }
//        }

    }

    private void analysisSortData(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("a[style^=text-decoration:none]");
        String title = doc.getElementsByTag("title").get(0).text();
        String description1 = doc.select("meta[name^=description]").get(0).attr("content") + "---";
        String description2 = doc.select("span[class=textLarge textDkGray]").text();

        for (Element element : elements) {
            String rank = element.select("span[class=textWhite textLarge]").text();
            String country = element.select("span[class=textWhite textLarge textShadow]").text();
            country = getEndStr(country);
            addTransData(country);
            String numStr = element.select("span[style^=background-color]").text();
            addSortEntity(title, description1 + description2, rank, country, numStr);
        }
    }

    private void addSortEntity(String title, String desc, String rank, String country, String num) {
        String[] s1 = rank.replace(",", "").split(" ");
        String unit = null;
        num = num.replace(",", "");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(num);
        if (matcher.find()) {
            unit = num.substring(matcher.end()).trim();
        }
        sortList.add(new DataSortEntity(title, desc, s1[0], country, s1[1], unit));
    }

    private void analysisHtmlData(String countyName, Element doc) {
        Elements children = doc.children();
        String tag = "";
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            String tagName = child.tagName();
            if (tagName.equals("button")) {
                tag = child.text().split("\\[")[0].trim();
                addTransData(tag);
            } else if (tag != null && !"".equals(tag)) {
                analysisHtmlType(countyName, child, tag);
                tag = "";
            }

        }
    }

    private void analysisHtmlType(String countryName, Element el, String type) {
        switch (type) {
            case "OVERVIEW":
                Elements alist = el.select("a[class=picTrans]");
                if (alist.size() > 0) {
                    Elements children = alist.get(0).parent().children();
                    for (int i = 0; i < children.size(); i++) {
                        Elements textShadowEls = children.get(i).select("span[class=textBold textNormal textShadow]");
                        Elements overviewRankHolderEl = children.get(i).select("div[class=overviewRankHolder]");
                        String textShadow = textShadowEls.text();
                        textShadow = getEndStr(textShadow);
                        addTransData(textShadow);
                        String overviewRankHolder = overviewRankHolderEl.text().replace("Rnk", "").trim();
                        addNum(countryName, type, textShadow, overviewRankHolder);
                    }
                }
                break;
            case "MANPOWER":
            case "AIRPOWER":
            case "LAND FORCES":
            case "NATURAL RESOURCES":
            case "LOGISTICS":
            case "FINANCIALS":
            case "GEOGRAPHY":
            case "NAVAL FORCES":
                Elements eles = el.select("div[class^=specsGenContainers");
                for (Element ele : eles) {
                    String text = ele.select("span[class$=textBold textShadow]").text();
                    String num = ele.select("span[class$=textWhite textShadow]").text();
//                    System.out.println(String.format("%s   ------   %s", text, num));
                    text = getEndStr(text);
                    addTransData(text);
                    //数字型数据处理
                    addNum(countryName, type, text, num);
                }
                break;
            default:
                break;
        }
    }

    private void addNum(String country, String type, String text, String num) {
        num = num.replace(",", "");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(num);
        if (matcher.find()) {
            String unit = num.substring(matcher.end()).trim();
            htmlList.add(new DataHtmlEntity(country, type, text, matcher.group(), unit));
        }
    }

    private Element getHtmlMainEl(String html) {
        Document doc = Jsoup.parse(html);
        Elements mianElm = doc.select("div[class=contentStripInner]");
        for (int i = 0; i < mianElm.size(); i++) {
            Element el = mianElm.get(i);
            Elements h2Datas = el.select("h2");
            for (int j = 0; j < h2Datas.size(); j++) {
                if ("Breakdown".equals(h2Datas.get(j).text())) {
                    return el;
                }
            }
        }
        return null;
    }

    public void addTransData(String str) {
        if (transSet == null) {
            transSet = new HashSet<>();
        }
        transSet.add(str);
    }

    public void transData() {
        if (transSet == null || transSet.size() <= 0) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        for (String str : transSet) {
            sb.append(str).append("\n");
        }

        String APP_ID = "20220310001118799";
        String SECURITY_KEY = "_I40DqQDM5E0quByUAlu";
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String transResult = api.getTransResult(sb.toString(), "auto", "zh");
        List<TransVo.Trans_result> resultList = JSONObject.parseObject(transResult, TransVo.class).getTrans_result();
        for (TransVo.Trans_result trans_result : resultList) {
            transResultMap.put(trans_result.getSrc(), trans_result.getDst());
        }
    }

    public void getoldHtml() {
        String html = HttpWormUtils.getHtml("https://www.globalfirepower.com/global-ranks-previous.php");
        TextIOStreamUtils.writeByFileWrite("E:\\deveData\\global\\html\\rankOdl.html", html);
        Document doc = Jsoup.parse(html);
        Elements dataList = doc.select("div[class=mainLists");
        for (int i = 0; i < dataList.size(); i++) {
            Element yearEle = dataList.get(i);
            Elements yearSpanEle = yearEle.select("span[class=textLarger textBold]");
//            System.out.println(yearSpanEle.text());
            Elements countrs = yearEle.select("div[class=countryHolder");
            for (int j = 0; j < countrs.size(); j++) {
                Element country = countrs.get(j);
                Elements rnkNum = country.select("div[class=rnkNum]");
                Elements countryName = country.select("div[class=countryName]");
//                System.out.println("   " + rnkNum.text() + " : " + countryName.text());
            }

        }

    }

    public void getPwoIndex() {
        String html = HttpWormUtils.getHtml("https://www.globalfirepower.com/countries-listing.php");
        Document doc = Jsoup.parse(html);
        Elements dataList = doc.select("a[title~=^Military strength values of]");
        for (int i = 0; i < dataList.size(); i++) {
            getDataByCountry(dataList.get(i));
//            String imgStr = imgEel.attr("style").split(";")[0].replace("background:url(","").replace(")","");
//            HttpWormUtils.downloadFromUrl("https://www.globalfirepower.com/" + imgStr, "E:\\deveData\\global\\img\\", name.text()+".jpg");

        }

    }


    public void getDataByCountry(Element countryEle) {
        Elements indexEel = countryEle.select("span[class=textWhite textLarge]");
        Elements nameEel = countryEle.select("div[class=longFormName]");
        Elements spanScoreEel = countryEle.select("span[class=textDkGray]");
        Elements imgEel = countryEle.select("div[class=flagContainer]");
        String href = countryEle.attr("href");
        String index = indexEel.text();  // 排名
        String name = nameEel.text();   // 名称
        String score = spanScoreEel.text().replace("PwrIndx Score: ", "");  // 得分


        String html = HttpWormUtils.getHtml("https://www.globalfirepower.com/" + href);
//        Document doc = Jsoup.parse(html);
        TextIOStreamUtils.writeByFileWrite("E:\\deveData\\global\\html\\" + name + ".html", html);


    }
}
