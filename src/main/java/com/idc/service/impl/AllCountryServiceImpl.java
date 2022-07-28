package com.idc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.idc.common.constants.URLConstants;
import com.idc.common.translate.TransApi;
import com.idc.common.translate.TransVo;
import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.TextIOStreamUtils;
import com.idc.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("AllCountryServiceImpl")
@Slf4j
public class AllCountryServiceImpl implements DataService {

    @Override
    public JSONObject getDataInfo(String id) throws InterruptedException {
        return null;
    }

    @Override
    public JSONObject getDataInfo() {
        return null;
    }

    @Test
    public void test() {
//        getCountryInfo(getCountryMap());
//        getAreaMap();
//        getCountryInfo(getAreaMap());
//        Map<String, String> transMap = new HashMap<>();
//        getMapData(transMap);
//        getMap(getMapData(transMap), transMap);
        getCountryInfo(sss());

//        System.out.println(deleteYY("西班牙王国（西班牙语：Reino de España；英语：The Kingdom of Spain），简称西班牙，位于欧洲西南部的伊比利亚半岛 [1]  ，地处欧洲与非洲的交界处，西邻葡萄牙，北濒比斯开湾，东北部与法国及安道尔接壤，南隔直布罗陀海峡与非洲的摩洛哥相望。领土还包括地中海中的巴利阿里群岛，大西洋的加那利群岛及非洲的休达和梅利利亚。该国是一个多山国家，总面积505925平方公里，其海岸线长约7800公里。 [1]  以西班牙语作为官方语言的国家数量世界第二，仅次于英语。 [1]  中世纪时，境内有多个国家并立，1492年西班牙光复运动胜利后，建立统一的西班牙王国。16世纪起 [1]  ，成为大航海时代中重要的海上强国，在欧洲、美洲、亚洲和非洲建立起大量殖民地。 [1]  1494年和葡萄牙共同签署了《托尔德西里亚斯条约》，意图将世界瓜分为二。在文艺复兴时期，西班牙帝国成为当时欧洲最强大的国家和影响全球的日不落帝国。三十年战争后开始衰落。在经历两个共和国和一个君主国后，于1978年宣布实行君主立宪制。 [1]  西班牙是一个发达的资本主义国家，也是欧盟和北约成员国。截至2020年10月，经济总量居欧盟第四位、世界第十四位 [2]  。主要工业有纺织、钢铁、水泥、造船、汽车制造、电力等。农业现代化水平较高，橄榄油产量和葡萄种植面积均居世界第一。 [1]"));
    }

    public Map<String, Map<String, String>> sss() {
        Map<String, Map<String, String>> chapterMap = new HashMap<>();
        Map<String, String> ddd = new HashMap<>();
//        ddd.put("亚洲", "/item/亚洲/133681");
//        ddd.put("欧洲", "/item/欧洲/145550");
//        ddd.put("北美洲", "/item/北美洲/135465");
//        ddd.put("南美洲", "/item/南美洲/138913");
//        ddd.put("非洲", "/item/非洲/81619");
//        ddd.put("大洋洲", "/item/大洋洲/195695");
//        ddd.put("欧盟", "/item/欧洲联盟/786749");
//        ddd.put("联合国", "/item/联合国/135426");



//        ddd.put("海峡群岛", "/item/海峡群岛/10907308");
//        ddd.put("直布罗陀", "/item/直布罗陀海峡/317363");
//        ddd.put("马恩岛", "/item/马恩岛/697534");
//        ddd.put("圣马丁岛", "/item/圣马丁岛/11050701");
//        ddd.put("库拉索", "/item/库拉索/3476979");
//        ddd.put("萨巴", "/item/萨巴/23375479");
//        ddd.put("博奈尔岛", "/item/博奈尔岛/10879490");
//        ddd.put("圣皮埃尔和密克隆群岛", "/item/圣皮埃尔和密克隆群岛/10542903");
//        ddd.put("加勒比海", "/item/加勒比海/257070");
//        ddd.put("马约特岛", "/item/马约特岛/5412653");
//        ddd.put("梅利利亚", "/item/梅利利亚/3490883");
//        ddd.put("塞卜泰", "/item/塞卜泰/7750770");
//        ddd.put("土阿莫土群岛", "/item/土阿莫土群岛/5354433");
//        ddd.put("社会群岛", "/item/社会群岛/2010688");
//        ddd.put("库普里亚诺夫群岛", "/item/库普里亚诺夫群岛/14437001");
//        ddd.put("马克萨斯群岛", "/item/马克萨斯群岛/5417648");
//        ddd.put("甘比尔群岛", "/item/甘比尔群岛/10636374");
//        ddd.put("南方群岛", "/item/南方群岛/52164");
//        ddd.put("约旦河西岸地区", "/item/约旦河西岸地区/3654489");

        ddd.put("香港", "/item/香港/128775");
        ddd.put("澳门", "/item/澳门/24335");
        ddd.put("台湾", "/item/台湾/122340");
        ddd.put("东南亚国家联盟", "/item/东南亚国家联盟/1059562");

        chapterMap.put("ssss", ddd);
        return chapterMap;


    }

    public void tess() {
        File file = new File("D:\\img");

        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                File[] fcs = f.listFiles();
                for (File fc : fcs) {
                    String name = fc.getName();
                    if (name.endsWith(".json")) {
                        System.out.println(name.replace(".json", ""));
                    }
                }
            }
        }
    }

    public Map<String, Map<String, String>> getMapData(Map<String, String> transMap) {
        Map<String, Map<String, String>> chapterMap = new HashMap<>();
        String html = HttpWormUtils.getHtml("http://img.hcharts.cn/mapdata/");
        Document doc = Jsoup.parse(html);
        Element maindiv = doc.select("article[class=row]").get(0);
        String mapType = "";
        StringBuffer sb = new StringBuffer();


        for (int i = 0; i < maindiv.children().size(); i++) {
            Element child = maindiv.child(i);
            String tagName = child.tagName();
            String className = child.className();
            if ("h4".equals(tagName)) {
                mapType = child.text();
                if (mapType.contains("（")) {
                    mapType = mapType.split("（")[0];
                }
                if (mapType.contains(" ")) {
                    mapType = mapType.split(" ")[0];
                }

            } else if ("ul".equals(tagName) && "maps".equals(className)) {
                System.out.println(mapType);
                Map<String, String> countryMap = new HashMap<>();

                for (int j = 0; j < child.children().size(); j++) {
                    Element mapChild = child.child(j);
                    Element svgs = mapChild.select("a[class=SVG]").get(0);

                    sb.append(mapChild.ownText()).append("\n");
                    countryMap.put(mapChild.ownText(), svgs.attr("href"));
//
                }
                chapterMap.put(mapType, countryMap);
            }

        }

        String APP_ID = "20220310001118799";
        String SECURITY_KEY = "_I40DqQDM5E0quByUAlu";
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String transResult = api.getTransResult(sb.toString(), "auto", "zh");
        List<TransVo.Trans_result> resultList = JSONObject.parseObject(transResult, TransVo.class).getTrans_result();

        for (TransVo.Trans_result result : resultList) {
            transMap.put(result.getSrc(), result.getDst());
        }
        return chapterMap;
    }

    public void getMap(Map<String, Map<String, String>> mapS, Map<String, String> transMap) {

        for (String key : mapS.keySet()) {
            for (String name : mapS.get(key).keySet()) {
                String url = "http://img.hcharts.cn/mapdata/" + mapS.get(key).get(name);
                if (transMap.containsKey(name)) {
                    name = transMap.get(name);
                }
                HttpWormUtils.downloadFromUrl(url, "D:\\svg\\" + key + "\\", name + ".svg");
            }
        }


    }

    public Map<String, Map<String, String>> getAreaMap() {
        Map<String, Map<String, String>> chapterMap = new HashMap<>();
        String html = HttpWormUtils.getHtml("https://baike.baidu.com/item/%E5%9C%B0%E5%8C%BA/13841495#viewPageContent");
        Document doc = Jsoup.parse(html);
        Element allDiv = doc.select("div[class=main-content J-content]").get(0);
        Element ol = allDiv.select("ol").get(0);
        Map<String, String> countryMap = new HashMap<>();
        for (int i = 0; i < ol.children().size(); i++) {
            Element li = ol.child(i);
            Element aDiv = li.select("a[target=_blank]").get(0);
            String name = aDiv.text();
            String url = aDiv.attr("href");
            if ("阿鲁巴".equals(name)) {
                url = "/item/%E9%98%BF%E9%B2%81%E5%B7%B4/36524";
            } else if ("百慕大".equals(name)) {
                url = url + "/1732391";
            }


            countryMap.put(name, url);
            System.out.println((i + 1) + "  " + aDiv.text() + " : " + aDiv.attr("href"));
        }
        chapterMap.put("地区", countryMap);
        return chapterMap;
    }

    public Map<String, Map<String, String>> getCountryMap() {
        String html = HttpWormUtils.getHtml("https://baike.baidu.com/item/%E4%B8%96%E7%95%8C%E5%90%84%E5%9B%BD/10915534?fr=aladdin");
        Document doc = Jsoup.parse(html);
        Element allDiv = doc.select("div[class=main-content J-content]").get(0);
        Elements chapters = allDiv.children();
        Map<String, Map<String, String>> chapterMap = new HashMap<>();
        String charterName = "";
        for (int i = 0; i < chapters.size(); i++) {
            Element ele = chapters.get(i);
            String tag = ele.tag().getName();
            String className = ele.className();
            if ("div".equals(tag) && "para-title level-2  J-chapter".equals(className)) {
                charterName = ele.children().get(0).text().replace("世界各国", "");
            } else if ("table".equals(tag)) {
                Map<String, String> countryMap = new HashMap<>();
                Elements countries = ele.select("td > div > a[target=_blank]");
                Elements countries2 = ele.select("td > div > span> a[target=_blank]");
                for (int j = 0; j < countries.size(); j++) {
                    Element country = countries.get(j);
                    countryMap.put(country.text(), country.attr("href"));
                }
                if (countries2.size() > 0) {
                    for (int j = 0; j < countries2.size(); j++) {
                        Element country = countries2.get(j);
                        countryMap.put(country.text(), country.attr("href"));
                    }
                }
                chapterMap.put(charterName, countryMap);
            }
        }
        return chapterMap;
    }

    public void getCountryInfo(Map<String, Map<String, String>> chapterMap) {
        for (String key : chapterMap.keySet()) {
            Map<String, String> countryMap = chapterMap.get(key);
            for (String countryName : countryMap.keySet()) {
                getCountryInfo(key, countryName, countryMap.get(countryName));
            }
        }
    }

    public void getCountryInfo(String chapter, String countryName, String url) {

        String html = HttpWormUtils.getHtml("https://baike.baidu.com" + url);
        Document doc = Jsoup.parse(html);
        Elements imageDiv = doc.select("div[class=side-content] > div[class=summary-pic]");

        if (imageDiv.size() == 0) {
            contrysTool(chapter, countryName, doc);
            return;
        }
        Elements img = imageDiv.get(0).select("img[src]");
        HttpWormUtils.downloadFromUrl(img.get(0).attr("src"), "D:\\img\\" + chapter + "\\", countryName + ".jpg");


        Element mianDiv = doc.select("div[class=main-content J-content]").get(0);

//        Elements imgs = mianDiv.select("img[src]");
//        for (int i = 0; i < imgs.size(); i++) {
//            Element im = imgs.get(i);
//
//
//            String str = im.attr("src");
//            if (!str.contains(";base64,")) {
//
//                if (str.endsWith(".png")) {
//                    HttpWormUtils.downloadFromUrl(im.attr("src"), "D:\\img\\" + chapter + "\\" + countryName + "\\", countryName + "_" + i + ".png");
//                } else if (str.endsWith(".jpeg")) {
//                    HttpWormUtils.downloadFromUrl(im.attr("src"), "D:\\img\\" + chapter + "\\" + countryName + "\\", countryName + "_" + i + ".jpeg");
//                } else if (str.endsWith("format,f_auto")) {
//                    System.out.println(str);
//                    HttpWormUtils.downloadFromUrl(im.attr("src"), "D:\\img\\" + chapter + "\\" + countryName + "\\", countryName + "_" + i + ".webp");
//                } else {
//                    HttpWormUtils.downloadFromUrl(im.attr("src"), "D:\\img\\" + chapter + "\\" + countryName + "\\", countryName + "_" + i + ".png");
//                }
//
//            }
//
//        }

        JSONObject countryInfo = new JSONObject();
        Elements lemmaDiv = mianDiv.select("div[class=lemma-summary]");
        Elements basicDiv = mianDiv.select("div[class=basic-info J-basic-info cmn-clearfix] > dl");
        countryInfo.put("lemma", deleteYY(lemmaDiv.text()));
        System.out.println(countryName);
        for (int i = 0; i < basicDiv.size(); i++) {
            Element dl = basicDiv.get(i);
            String dt = "";
            for (int j = 0; j < dl.children().size(); j++) {
                Element d = dl.child(j);
                String tagName = d.tagName();

                if ("dt".equals(tagName)) {
                    dt = deleteYY(d.text());
                } else if ("dd".equals(tagName)) {
                    countryInfo.put(dt, deleteYY(d.text()));
                }
            }
        }

        TextIOStreamUtils.writeByFileWrite("D://img//" + chapter + "//" + countryName + ".json", countryInfo.toJSONString());

    }

    private void contrysTool(String chapter, String countryName, Document doc) {
        Elements atags = doc.select("div[class=main-content J-content]>ul>li>div>a");

        for (int i = 0; i < atags.size(); i++) {
            Element aTag = atags.get(i);
            String data = aTag.text();
            if (data.endsWith("国") || data.endsWith("国家")) {
                getCountryInfo(chapter, countryName, aTag.attr("href"));
                return;
            }
        }
    }


    private String deleteYY(String str) {
        if (str.contains("[") && str.contains("]")) {
            int sindex = str.indexOf("[");
            int eindex = str.indexOf("]");
            String charSplit = str.substring(sindex, eindex + 1);
            str = str.replace(charSplit, "");
            return deleteYY(str);
        } else {
            return str.trim();
        }

    }
}
