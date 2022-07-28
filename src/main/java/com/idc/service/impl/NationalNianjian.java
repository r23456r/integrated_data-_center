package com.idc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.idc.common.constants.URLConstants;
import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.TextIOStreamUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NationalNianjian {
    @Test
    public void test() {
//        String html = HttpWormUtils.getHtml("http://www.stats.gov.cn/tjsj/ndsj/");
//        Document doc = Jsoup.parse(html);
//        Elements fieldList = doc.select("table[name=ztzw_tab]");
//        Elements nianDataUrls = fieldList.get(0).select("td[align]");
//        Map<String, String> urlMap = new HashMap<>();
//        for (int i = 0; i < nianDataUrls.size(); i++) {
//            Element nianDataUrl = nianDataUrls.get(i);
//            String nian = nianDataUrl.text();
//            String url = nianDataUrl.select("a").get(0).attr("href");
//
//            if (!nian.equals("")) {
//                urlMap.put(nian, url);
//            }
//        }
//        for (String nian : urlMap.keySet()) {
//            setNianData(nian, urlMap.get(nian));
//        }
        int i = 2013;
//        for (int i = 2008; i < 2009; i++) {
        String nian = i + "年";
        String url = "http://www.stats.gov.cn/tjsj/ndsj/" + i + "/left.htm";
        setNianData(nian, url);
//        }


    }

    private void setNianData(String name, String url) {
        String html = HttpWormUtils.getHtml(url, "gb2312");
        Document doc = Jsoup.parse(html);
        Element fieldList = doc.select("table").get(0);
        Element th = fieldList.select("th").get(0);
        Elements children = th.children();
        File file = new File("D:\\年鉴\\" + name);
        if (!file.exists()) {
            file.mkdirs();
        }
        Element ul = null;
        System.out.println(name);
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            if (child.tagName().equals("ul")) {
                ul = child;
                break;
            }
        }
        sss(file, ul, url);

    }

    private void sss(File file, Element ul, String url) {
        Elements children = ul.children();
        String cagra = "";
        JSONObject json = new JSONObject();
        boolean flg = false;
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);

            if (child.tagName().equals("a")) {
                cagra = child.text();
                File f = new File(file.getPath() + "\\" + cagra);
                if (!f.exists()) {
                    f.mkdirs();
                }
            } else if (child.tagName().equals("li")) {
                cagra = child.text();
                File f = new File(file.getPath() + "\\" + cagra);
                if (!f.exists()) {
                    f.mkdirs();
                }

            } else if (child.tagName().equals("ul")) {


                JSONObject item = new JSONObject();
                for (int j = 0; j < child.children().size(); j++) {
                    File f = new File(file.getPath() + "\\" + cagra + "\\" + child.children().get(j).text());
                    if (!f.exists()) {
                        f.mkdirs();
                    }

                    if ("11-7 按收入等级分城镇居民家庭平均每人全年现金消费支出(2012年)".equals(child.children().get(j).text())) {
                        flg = true;
                    } else {
                        flg = false;
                    }


                    Element li = child.children().get(j);
                    if (li.select("a").size() > 0) {
                        Element a = li.select("a").get(0);
                        String itemUl = a.attr("href");
                        item.put(a.text(), itemUl);
                        if (flg == false) {
                            continue;
                        }

                        System.out.println(a.text() + ":" + itemUl);
                        if (itemUl.endsWith("htm")) {
                            try {
                                String html = HttpWormUtils.getHtml(url.replace("/left.htm", "") + "/" + itemUl, "GBK");

                                if (html.length() < 1000) {

                                    Document doc = Jsoup.parse(html);
                                    Elements img = doc.select("p>img");
                                    if (img.size() == 1) {
                                        itemUl = img.get(0).attr("src");
                                        HttpWormUtils.downloadFromUrl(url.replace("/left.htm", "") + "/html/" + itemUl, f.getPath() + "\\", itemUl);
                                    } else {
                                        System.out.println(img.size());
                                    }

                                } else {
                                    String[] ss = itemUl.split("/");
                                    TextIOStreamUtils.writeByFileWrite2(f.getPath() + "//" + ss[ss.length - 1], html);
                                }


                            } catch (Exception e) {
                                System.out.println("error->data");
                            }
                        } else if (itemUl.endsWith("jpg")) {
                            String[] ss = itemUl.split("/");
                            HttpWormUtils.downloadFromUrl(url.replace("/left.htm", "") + "/" + itemUl, f.getPath() + "\\", ss[ss.length - 1]);
                        } else {
                            System.out.println(a.text() + ":" + a.attr("href"));
                        }

                    }
                }
                json.put(cagra, item);
            }

        }


        TextIOStreamUtils.writeByFileWrite(file.getPath() + "//data.json", json.toJSONString());
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }
}
