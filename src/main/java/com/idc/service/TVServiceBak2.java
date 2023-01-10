
package com.idc.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idc.dao.entity.TVEntity;
import com.idc.dao.entity.TVInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class TVServiceBak2 {

    //tvbs
//    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = format.parse("2023-01-06");
//        String dateStr = format.format(date);
//        String format1 = String.format("https://tvbsapp.tvbs.com.tw/pg_api/pg_list/3/%s/1/2", dateStr);
//        String srcStr = HttpUtil.get(format1);
//        JSONObject jsonObject = JSONObject.parseObject(srcStr);
//        jsonObject.put("tvName", "CNN");
//        TVEntity entity = new TVEntity();
//        JSONArray tvInfoArr = new JSONArray();
//        entity.setTvName("TVBS");
//        for (int i = 0; i < jsonObject.size(); i++) {
//            JSONArray data = JSONArray.parseArray(jsonObject.get("data").toString());
//            for (Object datum : data) {
//                JSONObject jsonObject1 = JSONObject.parseObject(datum.toString());
//                Object date1 = jsonObject1.get("date");
//                if (date1.toString().equals(dateStr)) {
//                    JSONArray resultData = JSONArray.parseArray(jsonObject1.get("data").toString());
//                    for (int i1 = 0; i1 < resultData.size(); i1++) {
//                        JSONObject jsonObject2 = JSONObject.parseObject(resultData.get(i1).toString());
//                        TVInfo info = new TVInfo();
//                        if (i1 + 1 < resultData.size()) {
//                            JSONObject nextArr = JSONObject.parseObject(resultData.get(i1 + 1).toString());
//                            String endTime = String.valueOf(nextArr.get("pg_hour"));
//                            String endTimeBJ = date1.toString() + " " + endTime;
//                            info.setEndTimeBJ(endTimeBJ);
//                            info.setEndTimePST(DateUtils.BJ2PST(endTimeBJ));
//                        }
//                        if (i1 == resultData.size() - 1) {
//                            String endTime = String.valueOf(jsonObject2.get("pg_hour"));
//                            String endTimeBJ = date1.toString() + " " + endTime;
//                            info.setEndTimeBJ(DateUtils.nextMidNight(endTimeBJ));
//                            info.setEndTimePST(DateUtils.BJ2PST(DateUtils.nextMidNight(endTimeBJ)));
//                        }
//                        String startTime = String.valueOf(jsonObject2.get("pg_hour"));
//                        String programName = String.valueOf(jsonObject2.get("pg_name"));
//                        jsonObject2.get("pg_name");
//                        String startTimeBJ = date1.toString() + " " + startTime;
//                        info.setStartTimeBJ(startTimeBJ);
//                        info.setStartTimePST(DateUtils.BJ2PST(startTimeBJ));
//                        info.setProgramName(programName);
//                        tvInfoArr.add(info);
//                    }
//                }
//            }
//            entity.setTvInfo(tvInfoArr);
//            System.out.println(JSONObject.toJSONString(entity));
//        }
//    }
    //epg香港
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2023-01-09");
        String dateStr = format.format(date);
        String format1 = "http://epg.i-cable.com/txt_ver/schedule_content.php?lang=&channel=109";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8889));
        HttpRequest httpRequest = HttpRequest.get(format1);
        String result = httpRequest.setProxy(proxy).execute().body();
        Document document = Jsoup.parse(result);
        TVEntity entity = new TVEntity();
        JSONArray tvInfoArr = new JSONArray();
        entity.setTvName("i-cable");
        Element root = document.select("tbody").get(6).select("tr").get(0);
        String[] 全天 = root.text().split("深夜");
        String 上午下午 = 全天[0];
        String[] 上午 = 上午下午.split("下午");
        String[] split2 = 上午[0].split("上午");
        String part1 = split2[1];
        String part2 = 上午[1];//
        String part3 = 全天[1];// 仅深夜

        System.out.println("早上");
        assemblData(part1);
        System.out.println("中午");
        assemblData(part2);
        System.out.println("深夜");
        assemblData(part3);


        Elements select = root.select("td").get(0).select("div");
        Elements elements = select.attr("class", "entry");
        for (int i = 1; i < elements.size(); i++) {
            String text = elements.get(i).text();
            TVInfo info = new TVInfo();
            if (text.contains(":")) {
//                System.out.println("time " + text);
                String startTimeBJ = dateStr + " " + text;
                if (i + 2 < elements.size() - 1) {
                    String endTimeBJ = dateStr + " " + elements.get(i + 2).text();
                    info.setStartTimeBJ(startTimeBJ);
                    info.setEndTimeBJ(endTimeBJ);
                    info.setProgramName(elements.get(i + 1).text());
                    tvInfoArr.add(info);
                }
            }
        }
        entity.setTvInfo(tvInfoArr);
        System.out.println(JSONObject.toJSONString(entity));
    }

    private static void assemblData(String part) {
        int tmpNameIndex = 0;
        char[] charArray = part.toCharArray();
        for (int i = 0; i < charArray.length; ) {
            if (Pattern.matches("^[0-9]*$", String.valueOf(charArray[i])) && charArray[i + 2] == ':') {
                String substring = part.substring(i, i + 5);
                if (tmpNameIndex != 0) {
                    System.out.println("Name:  " + part.substring(tmpNameIndex, i));
                }
                i = i + 6;
                tmpNameIndex = i;
                System.out.println("time:  " + substring);
            } else {
                i++;
                if (i == charArray.length - 1) {
                    System.out.println("Name：  " + part.substring(tmpNameIndex + 1, i));
                }
            }
        }
    }
}
