package com.idc.html;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.idc.common.utils.HttpWormUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class GlobalFireHtmls {

    public final String GLOBAL_FIRE_POWER_COUNTRIES_WEB_URL = "https://www.globalfirepower.com/countries-listing.php";
    private final String pref = "https://www.globalfirepower.com";

    @PostConstruct
    public void generateHtml() {
        JSONObject globalFirePowerData = new JSONObject();
        String html = HttpWormUtils.vpnGet(GLOBAL_FIRE_POWER_COUNTRIES_WEB_URL);
        Document doc = Jsoup.parse(html);
        Element element = doc.select("div[class=contentStripInner]").get(2);
        Elements a1 = element.select("a");
        for (Element element1 : a1) {
            String href = pref + element1.attr("href");
            String countryName = href.split("=")[1];
            String filePath = "D:\\03code\\github\\chrome\\html\\" + countryName + ".html"; // Replace with your desired file path
            FileUtil.writeString(HttpWormUtils.vpnGet(href), filePath, "UTF-8");
        }
    }

}
