
package com.idc.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.utils.DateUtils;
import com.idc.dao.entity.TVEntity;
import com.idc.dao.entity.TVInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.Proxy;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class TVService {

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
        Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8889));
        HttpRequest httpRequest = HttpRequest.get(format1);
        String result = httpRequest.setProxy(proxy).execute().body();
        Document document = Jsoup.parse(result);
        TVEntity entity = new TVEntity();
        JSONArray tvInfoArr = new JSONArray();
        entity.setTvName("i-cable");
        String root = document.select("tbody").get(6).select("tr").text();
        String[] allDay = root.split("深夜");
        String[] morning = allDay[0].split("下午");
        String[] split2 = morning[0].split("上午");
        String part1 = split2[1];
        String part2 = morning[1];
        String part3 = allDay[1];
        //早上
        List<String> list = assemblData(part1, false, dateStr);
        //中午
        list.addAll(assemblData(part2, false, dateStr));
        //深夜
        list.addAll(assemblData(part3, true, dateStr));
        System.out.println(JSONArray.toJSONString(list));
        for (int i = 0; i < list.size();) {
            String item = list.get(i);
            TVInfo info = new TVInfo();
            System.out.println("current:  " + item);
            info.setStartTimeBJ(item);
            info.setStartTimePST(DateUtils.BJ2PST(item));
            info.setProgramName(item);
            if (i + 2 < list.size() - 1) {
                info.setEndTimeBJ(list.get(i + 2));
                info.setEndTimePST(DateUtils.BJ2PST(list.get(i + 2)));
            } else {
                info.setEndTimeBJ("");
                info.setEndTimePST("");
            }
            tvInfoArr.add(info);
            i = i + 2;
        }
//        Elements select = root.select("td").get(0).select("div");
//        Elements elements = select.attr("class", "entry");
//        for (int i = 1; i < elements.size(); i++) {
//            String text = elements.get(i).text();
//            TVInfo info = new TVInfo();
//            if (text.contains(":")) {
////                System.out.println("time " + text);
//                String startTimeBJ = dateStr + " " + text;
//                if (i + 2 < elements.size() - 1) {
//                    String endTimeBJ = dateStr + " " + elements.get(i + 2).text();
//                    info.setStartTimeBJ(startTimeBJ);
//                    info.setEndTimeBJ(endTimeBJ);
//                    info.setProgramName(elements.get(i + 1).text());
//                    tvInfoArr.add(info);
//                }
//            }
//        }
        entity.setTvInfo(tvInfoArr);
        System.out.println(JSONObject.toJSONString(entity));
    }

    private static List<String> assemblData(String part, Boolean isCrossNight, String dateStr) {
        int tmpNameIndex = 0;
        List<String> list = new ArrayList<>();
        char[] charArray = part.toCharArray();
        for (int i = 0; i < charArray.length; ) {
            if (Pattern.matches("^[0-9]*$", String.valueOf(charArray[i])) && charArray[i + 2] == ':') {
                String time = part.substring(i, i + 5);
                if (tmpNameIndex != 0) {
                    String name = part.substring(tmpNameIndex, i);
//                    System.out.println("Name:  " + name);
                    list.add(name.trim());

                }
                i = i + 6;
                tmpNameIndex = i;
//                System.out.println("time:  " + time);
                if (isCrossNight) {
                    list.add(DateUtils.plusOneDay(dateStr + " " + time));
                } else {
                    list.add(dateStr + " " + time.trim());
                }
            } else {
                i++;
                if (i == charArray.length - 1) {
                    String name = part.substring(tmpNameIndex, i);
//                    System.out.println("Name：  " + name);
                    list.add(name.trim());
                }
            }
        }
        return list;
    }
}
