package com.idc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.UUIDUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GjjqServiceImpl {
    public static void main(String[] args) {

        GjjqServiceImpl gjjqService = new GjjqServiceImpl();
        JSONArray jsonArray = gjjqService.getList();
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String content = gjjqService.getCotent(jsonObject.getString("url"));
            jsonObject.put("content", content);
            list.add(jsonObject);
        }
        //  生成sql
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        for (JSONObject node : list) {
            String  originTime= node.getString("time").split("\\(")[1].split("\\)")[0];
            originTime = "2022-"+originTime.split("月")[0].trim()+"-"+originTime.split("月")[1].trim().split("日")[0]+originTime.split("月")[1].split("日")[1];
            System.out.println("INSERT INTO `kno_news` VALUES ('" + UUIDUtils.getFullUUID() + "', '10002', '" + node.getString("title") + "', '" + node.getString("content") + "', '" + node.getString("ly") +
                    "', '" + originTime + "', 'dlj', '" + time + "', 'ewct/gjjq', '', '0', '0', '" +time + "', '0', '" + node.getString("url") + "');");
            //  TextIOStreamUtils.writeByFileWrite("D:\\dd\\" + node.getString("title"), fieldList.toString());
            // System.out.println(list);
        }

    }

    private String getCotent(String url) {
        String html = HttpWormUtils.getHtml(url);

        Document doc = Jsoup.parse(html);
        doc.select("[class=article]").select("script").remove();
        Elements content = doc.select("[class=article]");
        String strCotent = content.html();
        Elements imgs = content.select("img[src]");
        if (imgs.size() > 0) {
            for (Element img : imgs) {
                String imgAdd = img.attr("src");
                String fileName = HttpWormUtils.getFileNameFromUrl(imgAdd);

              //  HttpWormUtils.downloadFromUrl("https:" + img.attr("src"), "D:\\guojijunqing\\", fileName);
                strCotent = strCotent.replace(imgAdd, "#fjhead#" + fileName);
            }
        }
        Elements videos = content.select("video[src]");
        if (videos.size() > 0) {
            for (Element video : videos) {
                String videoAdd = video.attr("src");
                String fileName = UUIDUtils.getFullUUID() + HttpWormUtils.getFileNameFromUrl(videoAdd);
             //   HttpWormUtils.downloadFromUrl(video.attr("https:" + "src"), "D:\\guojijunqing\\", fileName);
                strCotent = strCotent.replace(videoAdd, "#fjhead#" + fileName);
            }
        }
        return strCotent;
    }

    private JSONArray getList() {
        JSONArray list = new JSONArray();
        String urlfirst = "https://mil.news.sina.com.cn/roll/index.d.html?cid=57919&page=1";
        String html = HttpWormUtils.getHtml(urlfirst);
        Document doc = Jsoup.parse(html);
        addList(doc, list);
        Elements page = doc.select("[class=pagebox page]").select("span");
        int number = 0;
        if (page.size() > 0) {
            number = Integer.valueOf(page.get(page.size() - 1).select("a").attr("href").split("=")[2]);
        }
        if (number > 0) {
            for (int j = 2; j <= number; j++) {
                String url = "https://mil.news.sina.com.cn/roll/index.d.html?cid=57919&page=" + j;
                String htmls = HttpWormUtils.getHtml(url);
                doc = Jsoup.parse(htmls);
                addList(doc, list);
            }
        }

        return list;

    }


    private void addList(Document doc, JSONArray list) {
        Elements dataList = doc.select("[class=fixList]");
        for (Element elements : dataList) {
            Elements liList = elements.select("li");
            for (Element element : liList) {
                Elements data = element.select("a");
                String name = data.text().trim();
                String time = element.select("span").text().trim();
                JSONObject attribute = new JSONObject();
                attribute.put("name", name);
                attribute.put("title", name);
                attribute.put("time", time);
                attribute.put("url", data.attr("href").trim());
                attribute.put("ly", "新浪网");
                list.add(attribute);
            }
        }

    }

    private static void getFile() {


        String html = HttpWormUtils.getHtml("http://www.mod.gov.cn/topnews/2021-01/10/content_4876879.htm");
        Document doc = Jsoup.parse(html);
        doc.select("[class=article-content p-t]").select("[id=wxsimgbox]").remove();
        doc.select("[class=article-content p-t]").select("[id=cm-player]").remove();
    }


}
