package com.idc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.TextIOStreamUtils;
import com.idc.common.utils.UUIDUtils;
import com.idc.service.DataService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service("RussiaUkraineWarService")
public class RussiaUkraineWarService implements DataService {

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
        getNews();
    }

    private JSONObject getNews() {
        JSONObject xhCountryData = new JSONObject();
        String html = HttpWormUtils.getHtml("https://so.toutiao.com/search/?from=timeliness_poster&source=share&keyword=%E4%BF%84%E7%BD%97%E6%96%AF%E4%B9%8C%E5%85%8B%E5%85%B0%E4%BA%8B%E4%BB%B6%E6%9D%A5%E9%BE%99%E5%8E%BB%E8%84%89");
        Document doc = Jsoup.parse(html);
        Elements fieldList = doc.select("script[data-for=ala-data]");
        String jsonStr = fieldList.get(0).data();
        JSONObject jsonData = JSONObject.parseObject(jsonStr);
        JSONArray timeAxis = jsonData.getJSONObject("display").getJSONArray("cards").getJSONObject(1)
                .getJSONObject("display").getJSONObject("index_info").getJSONArray("time_axis");
//        TextIOStreamUtils.writeByFileWrite("D:\\wh.html", html);
        getFile(timeAxis);
//        TextIOStreamUtils.writeByFileWrite("D:\\sssh.html", s.toString());
        return null;

    }

    private void getFile(JSONArray timeAxis) {

//        String html = HttpWormUtils.getHtml("https://toutiao.com/group/7067349601979826724/?channel=&in_ogs=&in_tfs=&original_source=&source=timeliness_poster&traffic_source=&utm_medium=wap_search&utm_source=");
//        Document doc = Jsoup.parse(html);
//        Elements fieldList = doc.select("div[class=article-content]>article");
//        System.out.println(html);
        for (int i = 0; i < timeAxis.size(); i++) {
            JSONObject node = timeAxis.getJSONObject(i);
            String html = HttpWormUtils.getHtml(node.getString("url"));
            Document doc = Jsoup.parse(html);
            Elements fieldList = doc.select("div[class=a-con]");
            if (fieldList.size() <= 0) {
                fieldList = doc.select("article");
                if (fieldList.size() <= 0) {


//                    fieldList = doc.select("video[mediatype=video]");
//                    if (fieldList.size() <= 0) {
//                        System.out.println("error" + node.getString("title") + "  " + node.getString("url"));
//                    } else {
//
//                    }

                } else {
                    printData(node, fieldList);
                }

            } else {
                printData(node, fieldList);
//                System.out.println("secess" + node.getString("title") + "  " + node.getString("url"));
//
//                String htmlStr = fieldList.get(0).toString();
//                Elements imgs = fieldList.get(0).select("img[src]");
//                if (imgs.size() > 0) {
//                    for (Element img : imgs) {
//                        String imgAdd = img.attr("src");
//                        String fileName = HttpWormUtils.getFileNameFromUrl(imgAdd);
////                        HttpWormUtils.downloadFromUrl(img.attr("src"), "D:\\dd\\ewct\\sjml\\", fileName);
//                        htmlStr = htmlStr.replace(imgAdd, "#fjhead#" + fileName);
//                    }
//                }
//
//                Elements videos = fieldList.get(0).select("video[src]");
//                if (videos.size() > 0) {
//                    for (Element video : videos) {
//                        String videoAdd = video.attr("src");
//                        String fileName = UUIDUtils.getFullUUID() + HttpWormUtils.getFileNameFromUrl(videoAdd);
//                        HttpWormUtils.downloadFromUrl(video.attr("src"), "D:\\dd\\ewct\\sjml\\", fileName);
//                        htmlStr = htmlStr.replace(videoAdd, "#fjhead#" + fileName);
//                    }
//                }
//
////                System.out.println(htmlStr);
//                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(node.getLong("show_time") * 1000));
//                htmlStr = htmlStr.replace("'", "\\'");
//                System.out.println("INSERT INTO `kno_news` VALUES ('" + UUIDUtils.getFullUUID() + "', '10001', '" + node.getString("title") + "', '" + htmlStr + "', '" + node.getString("source") +
//                        "', '" + time + "', 'dlj', '" + time + "', 'ewct/sjml', '', '0', '0', '" + time + "', '0', '" + node.getString("url") + "');");
////                TextIOStreamUtils.writeByFileWrite("D:\\dd\\" + node.getString("title"), fieldList.toString());
            }


        }
    }


    private void printData(JSONObject node, Elements fieldList) {
        //                System.out.println("secess" + node.getString("title") + "  " + node.getString("url"));

        String htmlStr = fieldList.get(0).toString();
        Elements imgs = fieldList.get(0).select("img[src]");
        if (imgs.size() > 0) {
            for (Element img : imgs) {
                String imgAdd = img.attr("src");
                String fileName = HttpWormUtils.getFileNameFromUrl(imgAdd);
                HttpWormUtils.downloadFromUrl(img.attr("src"), "D:\\dd\\ewct\\sjml\\", fileName);
                htmlStr = htmlStr.replace(imgAdd, "#fjhead#" + fileName);
            }
        }

        Elements videos = fieldList.get(0).select("video[src]");
        if (videos.size() > 0) {
            for (Element video : videos) {
                String videoAdd = video.attr("src");
                String fileName = UUIDUtils.getFullUUID() + HttpWormUtils.getFileNameFromUrl(videoAdd);
                HttpWormUtils.downloadFromUrl(video.attr("src"), "D:\\dd\\ewct\\sjml\\", fileName);
                htmlStr = htmlStr.replace(videoAdd, "#fjhead#" + fileName);
            }
        }

//                System.out.println(htmlStr);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(node.getLong("show_time") * 1000));
        htmlStr = htmlStr.replace("'", "\\'");
        System.out.println("INSERT INTO `kno_news` VALUES ('" + UUIDUtils.getFullUUID() + "', '10001', '" + node.getString("title") + "', '" + htmlStr + "', '" + node.getString("source") +
                "', '" + time + "', 'dlj', '" + time + "', 'ewct/sjml', '', '0', '0', '" + time + "', '0', '" + node.getString("url") + "');");
//                TextIOStreamUtils.writeByFileWrite("D:\\dd\\" + node.getString("title"), fieldList.toString());
    }
}
