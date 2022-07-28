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
import java.util.Date;
import java.util.List;

public class JmqServiceImpl {


    public static void main(String[] args) {
        JmqServiceImpl dgbyService = new JmqServiceImpl();
    JSONArray jsonArray = dgbyService.getList();
    List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String content = dgbyService.getCotent(jsonObject.getString("url"));
        jsonObject.put("content", content);
        list.add(jsonObject);
    }


        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());


    //  生成sql
        for (JSONObject node : list) {

          String  originTime= node.getString("time").trim();
            originTime = "2022-0"+originTime.split("月")[0].trim()+"-"+originTime.split("月")[1].trim().split("日")[0]+originTime.split("月")[1].split("日")[1];

        System.out.println("INSERT INTO `kno_news` VALUES ('" + UUIDUtils.getFullUUID() + "', '10004', '" + node.getString("title") + "', '" + node.getString("content") + "', '" + node.getString("ly") +
                "', '" + originTime + "', 'dlj', '" + time + "', 'ewct/jmq', '', '0', '0', '" + time + "', '0', '" + node.getString("url") + "');");
        //  TextIOStreamUtils.writeByFileWrite("D:\\dd\\" + node.getString("title"), fieldList.toString());
        // System.out.println(list);
    }

}

    private String getCotent(String url) {
        String html = HttpWormUtils.getHtml(url);

        Document doc = Jsoup.parse(html);
        Elements content = doc.select("[class=article]");
        String strCotent = content.html();
        Elements imgs = content.select("img[src]");
        if (imgs.size() > 0) {
            for (Element img : imgs) {
                String imgAdd = img.attr("src");
                String fileName = HttpWormUtils.getFileNameFromUrl(imgAdd);

              //  HttpWormUtils.downloadFromUrl("https:" + img.attr("src"), "D:\\jmq\\", fileName);

                strCotent = strCotent.replace(imgAdd, "#fjhead#" + fileName);
            }
        }
        Elements videos = content.select("video[src]");
        if (videos.size() > 0) {
            for (Element video : videos) {
                String videoAdd = video.attr("src");
                String fileName = UUIDUtils.getFullUUID() + HttpWormUtils.getFileNameFromUrl(videoAdd);
                HttpWormUtils.downloadFromUrl(video.attr("https:" + "src"), "D:\\jmq\\", fileName);
                strCotent = strCotent.replace(videoAdd, "#fjhead#" + fileName);
            }
        }
        return strCotent;
    }

    private JSONArray getList() {
        JSONArray list = new JSONArray();
        String urlfirst = "https://mil.news.sina.com.cn/zhengming/";
        String html = HttpWormUtils.getHtml(urlfirst);
        Document doc = Jsoup.parse(html);
        addList(doc, list);
        return list;

    }


    private void addList(Document doc, JSONArray list) {
        Elements dataList = doc.select("[class=blk_tw clearfix]");
        for (Element elements : dataList) {
            Elements data = elements.select("a");
            String name = data.get(0).text().trim();
            String time = elements.select("span").text().trim();
            JSONObject attribute = new JSONObject();
            attribute.put("name", name);
            attribute.put("time", time);
            attribute.put("url", data.get(0).attr("href").trim());
            attribute.put("ly", "新浪网");
            attribute.put("title", name);
            list.add(attribute);
        }

    }
}
