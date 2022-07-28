package com.idc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.TextIOStreamUtils;
import com.idc.common.utils.UUIDUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BpsServiceImpl {


    public static void main(String[] args) {
        // JSONArray jsonArray = getBpsList();
      //  getFile();
        JSONArray jsonArray = getBpsList();
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String html = HttpWormUtils.getHtml(jsonObject.getString("url"));

            Document doc = Jsoup.parse(html);
            Elements header = doc.select("[class=article-header]");
            doc.select("[class=article-content p-t]").select("[id=wxsimgbox]").remove();
            doc.select("[class=article-content p-t]").select("[id=cm-player]").remove();
            Elements content = doc.select("[class=article-content p-t]");
            StringBuffer allCotent = new StringBuffer();
            for (Element element : header) {
                jsonObject.put("ly", element.select("span").get(0).text().trim());
                jsonObject.put("title", element.select("h1").text().trim());
            }
            //处理分页
            int number = 0;
            Elements numberlist = doc.select("[id=displaypagenum]").select("a");

            if (numberlist.size() > 0) {
                number = Integer.valueOf(numberlist.get(numberlist.size()-1).attr("href").split("_")[2].split("\\.")[0]);

            }
            if (number>0){
                for (int j = 2; j <number ; j++) {
                   String [] urls= jsonObject.getString("url").split("\\.htm");
                   String url=urls[0]+"_"+j+".htm";
                   String numberCotent= getCotent(url);
                    allCotent.append(numberCotent);
                }
            }else{
                String strCotent = content.html();
                Elements imgs = content.select("img[src]");
                if (imgs.size() > 0) {
                    for (Element img : imgs) {
                        String imgAdd = img.attr("src");
                        String fileName = HttpWormUtils.getFileNameFromUrl(imgAdd);
                        if (img.attr("src").contains("../../")){
                            String type=jsonObject.getString("url").split("cn")[1].split("\\/")[1];

                            String address= img.attr("src").replace("../../","http://www.mod.gov.cn/"+type+"/");

                          // String address= img.attr("src").replace("../../","http://www.mod.gov.cn/regulatory/");
                            HttpWormUtils.downloadFromUrl(address,"D:\\img\\", fileName);
                        }else{
                            HttpWormUtils.downloadFromUrl("http://www.mod.gov.cn/regulatory/attachement/jpg/" + img.attr("src"), "D:\\img\\", fileName);
                        }

                        strCotent = strCotent.replace(imgAdd, "#fjhead#" + fileName);
                    }
                }
                Elements videos = content.select("video[src]");
                if (videos.size() > 0) {
                    for (Element video : videos) {
                        String videoAdd = video.attr("src");
                        String fileName = UUIDUtils.getFullUUID() + HttpWormUtils.getFileNameFromUrl(videoAdd);
                        HttpWormUtils.downloadFromUrl(video.attr("http://www.mod.gov.cn/regulatory/attachement/jpg/" + "src"), "D:\\img\\", fileName);
                        strCotent = strCotent.replace(videoAdd, "#fjhead#" + fileName);
                    }
                }
                allCotent.append(strCotent);
            }
            jsonObject.put("content", allCotent);
            list.add(jsonObject);
        }
      //  生成sql
        for (JSONObject node:list ) {

            System.out.println("INSERT INTO `kno_news` VALUES ('" + UUIDUtils.getFullUUID() + "', '10001', '" + node.getString("title") + "', '" + node.getString("content") + "', '" + node.getString("ly") +
                    "', '" + node.getString("time") + "', 'dlj', '" + node.getString("time") + "', 'ewct/sjml', '', '0', '0', '" + node.getString("time") + "', '0', '" + node.getString("url") + "');");
            //  TextIOStreamUtils.writeByFileWrite("D:\\dd\\" + node.getString("title"), fieldList.toString());
           // System.out.println(list);
        }

    }
    private static String getCotent(String url) {
        String html = HttpWormUtils.getHtml(url);

        Document doc = Jsoup.parse(html);
        doc.select("[class=article-content p-t]").select("[id=wxsimgbox]").remove();
        doc.select("[class=article-content p-t]").select("[id=cm-player]").remove();
        doc.select("[class=article-content p-t]").select("[id=displaypagenum]").remove();
        doc.select("[class=article-content p-t]").select("HTMLBOX").remove();
        Elements content = doc.select("[class=article-content p-t]");
        String strCotent = content.html();
        Elements imgs = content.select("img[src]");
        if (imgs.size() > 0) {
            for (Element img : imgs) {
                String imgAdd = img.attr("src");
                String fileName = HttpWormUtils.getFileNameFromUrl(imgAdd);
                if (img.attr("src").contains("../../")){
                  String type=url.split("cn")[1].split("\\/")[1];

                    String address= img.attr("src").replace("../../","http://www.mod.gov.cn/"+type+"/");
                    HttpWormUtils.downloadFromUrl(address,"D:\\img\\", fileName);
                }else{
                    HttpWormUtils.downloadFromUrl("http://www.mod.gov.cn/regulatory/attachement/jpg/" + img.attr("src"), "D:\\img\\", fileName);
                }
                strCotent = strCotent.replace(imgAdd, "#fjhead#" + fileName);
            }
        }
        Elements videos = content.select("video[src]");
        if (videos.size() > 0) {
            for (Element video : videos) {
                String videoAdd = video.attr("src");
                String fileName = UUIDUtils.getFullUUID() + HttpWormUtils.getFileNameFromUrl(videoAdd);
                HttpWormUtils.downloadFromUrl(video.attr("http://www.mod.gov.cn/regulatory/attachement/jpg/" + "src"), "D:\\img\\", fileName);
                strCotent = strCotent.replace(videoAdd, "#fjhead#" + fileName);
            }
        }
        return  strCotent;
    }

    private static JSONArray getBpsList() {
        JSONArray list = new JSONArray();
        String html = HttpWormUtils.getHtml("http://www.mod.gov.cn/regulatory/node_47121.htm");
        Document doc = Jsoup.parse(html);
        Elements dataList = doc.select("[class=list-unstyled article-list]");
        for (Element elements : dataList) {
            Elements liList = elements.select("li");
            for (Element element : liList) {
                Elements countryUrlData = element.select("p");
                if (countryUrlData.size() > 0) {
                    String name = element.text().trim();
                    String time = element.select("small").text().trim();
                    JSONObject attribute = new JSONObject();
                    attribute.put("name", name);
                    attribute.put("time", time);
                    String url = element.select("a").attr("href").trim();
                    if (url.contains("../")) {
                        url = url.replace("../", "http://www.mod.gov.cn/");
                    } else {
                        url = "http://www.mod.gov.cn/regulatory/" + url;
                    }
                    attribute.put("url", url.trim());
                    list.add(attribute);
                }
            }
        }
        return list;

    }


    private static void getFile() {


        String html = HttpWormUtils.getHtml("http://www.mod.gov.cn/topnews/2021-01/10/content_4876879.htm");
        Document doc = Jsoup.parse(html);
        doc.select("[class=article-content p-t]").select("[id=wxsimgbox]").remove();
        doc.select("[class=article-content p-t]").select("[id=cm-player]").remove();

//        Elements content = doc.select("[class=article-content p-t]");
//        String strCotent = content.html();
//
//
//        Elements imgs = content.select("img[src]");
//        if (imgs.size() > 0) {
//            for (Element img : imgs) {
//                String imgAdd = img.attr("src");
//                String fileName = HttpWormUtils.getFileNameFromUrl(imgAdd);
//                HttpWormUtils.downloadFromUrl("http://www.mod.gov.cn/regulatory/attachement/jpg/"+img.attr("src"), "D:\\img\\", fileName);
//                strCotent = strCotent.replace(imgAdd, "#bpsCoent#" + fileName);
//            }
//        }
//        Elements videos = content.select("video[src]");
//        if (videos.size() > 0) {
//            for (Element video : videos) {
//                String videoAdd = video.attr("src");
//                String fileName = UUIDUtils.getFullUUID() + HttpWormUtils.getFileNameFromUrl(videoAdd);
//                HttpWormUtils.downloadFromUrl(video.attr("http://www.mod.gov.cn/regulatory/attachement/jpg/"+"src"), "D:\\img\\", fileName);
//                strCotent = strCotent.replace(videoAdd, "#bpsCoent#" + fileName);
//            }
//        }

////                System.out.println(htmlStr);
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(node.getLong("show_time") * 1000));
//        htmlStr = htmlStr.replace("'", "\\'");
//        System.out.println("INSERT INTO `kno_news` VALUES ('" + UUIDUtils.getFullUUID() + "', '10001', '" + node.getString("title") + "', '" + htmlStr + "', '" + node.getString("source") +
//                "', '" + time + "', 'dlj', '" + time + "', 'ewct/sjml', '', '0', '0', '" + time + "', '0', '" + node.getString("url") + "');");
////                TextIOStreamUtils.writeByFileWrite("D:\\dd\\" + node.getString("title"), fieldList.toString());
    }


    public void selenium(String url) {
        // 设置 chromedirver 的存放位置
        System.getProperties().setProperty("webdriver.chrome.driver", "chromedriver.exe");
        // 设置无头浏览器，这样就不会弹出浏览器窗口
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

        WebDriver webDriver = new ChromeDriver(chromeOptions);
        webDriver.get(url);
        // 获取到要闻新闻列表
        List<WebElement> webElements = webDriver.findElements(By.xpath("//div[@class='news_title']/h3/a"));
        for (WebElement webElement : webElements) {
            // 提取新闻连接
            String article_url = webElement.getAttribute("href");
            // 提取新闻标题
            String title = webElement.getText();
            if (article_url.contains("https://news.163.com/")) {
                System.out.println("文章标题：" + title + " ,文章链接：" + article_url);
            }
        }
        webDriver.getTitle();
        webDriver.close();
    }


//    public static void main(String[] args) {
//
//        // getFile();
//        JSONArray jsonArray = getBpsList();
//        List<JSONObject> list = new ArrayList<>();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//            // 设置 chromedirver 的存放位置
//            System.getProperties().setProperty("webdriver.chrome.driver", "chromedriver.exe");
//            // 设置无头浏览器，这样就不会弹出浏览器窗口
//            ChromeOptions chromeOptions = new ChromeOptions();
//            chromeOptions.addArguments("--headless");
//
//            WebDriver webDriver = new ChromeDriver(chromeOptions);
//            webDriver.get(jsonObject.getString("url"));
//
//          //  String html = HttpWormUtils.getHtml(jsonObject.getString("url"));
//
//          //  Document doc = Jsoup.parse(html);
//         //   List<WebElement> header = webDriver.findElements(By.xpath("//div[@class='article-header']/span/")).get(0).getText();
//           // doc.select("[class=article-content p-t]").select("[id=wxsimgbox]").remove();
//           // doc.select("[class=article-content p-t]").select("[id=cm-player]").remove();
//
//
//
//                jsonObject.put("ly", webDriver.findElements(By.xpath("//div[@class='info']/span")).get(0).getText().trim());
//                jsonObject.put("title", webDriver.findElement(By.xpath("//div[@class='article-header']/h1")).getText().trim());
//            WebElement content =  webDriver.findElement(By.xpath("//div[@class='article-content p-t']"));
//            String strCotent = content.getText();
//            List<WebElement> imgs = webDriver.findElements(By.xpath("//div[@class='article-content p-t']/img"));
//            if (imgs.size() > 0) {
//                for (WebElement img : imgs) {
//                    String imgAdd = img.getAttribute("src");
//                    String fileName = HttpWormUtils.getFileNameFromUrl(imgAdd);
//                    HttpWormUtils.downloadFromUrl("http://www.mod.gov.cn/regulatory/attachement/jpg/"+img.getAttribute("src"), "D:\\img\\", fileName);
//                    strCotent = strCotent.replace(imgAdd, "#bpsCoent#" + fileName);
//                }
//            }
//            List<WebElement>  videos = content.findElements(By.xpath("video[src]"));
//            if (videos.size() > 0) {
//                for (WebElement video : videos) {
//                    String videoAdd = video.getAttribute("src");
//                    String fileName = UUIDUtils.getFullUUID() + HttpWormUtils.getFileNameFromUrl(videoAdd);
//                    HttpWormUtils.downloadFromUrl(video.getAttribute("http://www.mod.gov.cn/regulatory/attachement/jpg/"+"src"), "D:\\img\\", fileName);
//                    strCotent = strCotent.replace(videoAdd, "#bpsCoent#" + fileName);
//                }
//            }
//            jsonObject.put("content", strCotent);
//            list.add(jsonObject);
//        }
//        System.out.println(list);
//    }

}
