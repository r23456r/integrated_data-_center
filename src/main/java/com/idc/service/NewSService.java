package com.idc.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.utils.HttpWormUtils;
import com.idc.vo.NewsDomain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NewSService {
    @Test
    public void test() {
        String str = "C:\\Users\\PandaIP\\Desktop\\data.zip";
        File unzip = ZipUtil.unzip(str);
        File imgFile = null;
        File jsonFile = null;
        for (File file : Objects.requireNonNull(unzip.listFiles())) {
            if (file.getName().equals("img")) {
                imgFile = file;
            }
            if (file.isFile() & file.getName().contains("json")) {
                jsonFile = file;
            }
        }
        Map<String, String> map = new HashMap<>();

        if (imgFile != null) {
            for (File img : imgFile.listFiles()) {
                //upload
                String imgPath = img.getAbsolutePath();
                map.put(img.getName(), imgPath);
            }
        }
        if (jsonFile != null) {
            FileReader reader = new FileReader(jsonFile.getAbsolutePath());
            NewsDomain newsDomain = JSONUtil.toBean(reader.readString(), NewsDomain.class);
            String contents = newsDomain.getContents();
            for (String s : map.keySet()) {
                contents = contents.replace(s, map.get(s));
            }
            //mapper 入库
            System.out.println(contents);
        }
        System.out.println(unzip.getTotalSpace());
    }

    @Test
    public void test2() {
        System.out.println("1111".replace("1", "2"));
    }

    @Test
    @Async
    public void testCompany() {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 4651; i++) {
            String url = "http://femhzs.mofcom.gov.cn/fecpmvc_zj/pages/fem/CorpJWList_nav.pageNoLink.html?session=T&sp=" + i + "&sp=S+_t1.CORP_CDE%2C+_t1.id&sp=T&sp=S";
            urls.add(url);
        }
        Map<String, Set<String>> map = new HashMap<>();
        for (String url : urls) {
            String html = HttpWormUtils.getHtmlForCompany(url);
            Document doc = Jsoup.parse(html);
            doc.select("thead").remove();
            Elements select = doc.select("table[class=\"m-table\"]").select("tr");
            for (org.jsoup.nodes.Element element : select) {
                Elements tds = element.select("td");
                for (int i = 0; i < tds.size(); i++) {
                    try {
                        String country = tds.get(2).text();
                        String company = tds.get(0).text();
                        if (map.containsKey(country)) {
                            Set<String> set = map.get(country);
                            set.add(company);
                        } else {
                            Set<String> set = new HashSet<>();
                            set.add(company);
                            map.put(country, set);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            System.out.println("---" + "\n" + url);
            String result = JSONObject.toJSONString(map);
            System.out.println(result);
            FileUtil.writeBytes(result.getBytes(StandardCharsets.UTF_8), new File("C:\\Users\\PandaIP\\Desktop\\20221025.json"));
        }
    }

    @Test
    public void testExplicitThread() {

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 4651; i++) {
            String url = "http://femhzs.mofcom.gov.cn/fecpmvc_zj/pages/fem/CorpJWList_nav.pageNoLink.html?session=T&sp=" + i + "&sp=S+_t1.CORP_CDE%2C+_t1.id&sp=T&sp=S";
            urls.add(url);
        }
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Map<String, Set<String>> map = new HashMap<>();
        AtomicInteger counter = new AtomicInteger(0);
        for (String url : urls) {
            Thread thread = new Thread(() -> {
                String html = HttpWormUtils.getHtmlForCompany(url);
                Document doc = Jsoup.parse(html);
                doc.select("thead").remove();
                Elements select = doc.select("table[class=\"m-table\"]").select("tr");
                for (Element element : select) {
                    Elements tds = element.select("td");
                    for (int i = 0; i < tds.size(); i++) {
                        try {
                            String country = tds.get(2).text();
                            String company = tds.get(0).text();
                            if (map.containsKey(country)) {
                                Set<String> set = map.get(country);
                                set.add(company);
                            } else {
                                Set<String> set = new HashSet<>();
                                set.add(company);
                                map.put(country, set);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                System.out.println("---" + "\n" + url + "--- " + counter.getAndIncrement());
                String result = JSONObject.toJSONString(map);
                System.out.println(result);
                FileUtil.writeBytes(result.getBytes(StandardCharsets.UTF_8), new File("C:\\Users\\PandaIP\\Desktop\\20221025.json"));
            });
            executor.submit(thread);

        }
    }
}
