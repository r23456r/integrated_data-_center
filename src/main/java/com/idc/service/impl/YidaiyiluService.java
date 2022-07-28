package com.idc.service.impl;

import com.idc.common.utils.HttpWormUtils;
import com.idc.common.utils.TextIOStreamUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class YidaiyiluService {
    @Test
    public void test() {
        List<String> urls = new ArrayList<>();
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data-json2.json");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data-json3.json");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data-json1.json");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data3.json");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data1.json");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data2.json");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data-json2-1.json?01");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data-json2-2.json?01");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data-json2-3.json?01");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/data-json2-4.json?01");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/commonData.json");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/foreign_trade.json");
        urls.add("https://www.yidaiyilu.gov.cn/js/SJ/exploitData.json");

        for (String url : urls) {
            String html = HttpWormUtils.getHtml(url);
            String name = url.replace("https://www.yidaiyilu.gov.cn/js/SJ/", "").replace("?01", "");
            TextIOStreamUtils.writeByFileWrite("E:\\deveData\\ydyl\\" + name, html);
        }
    }
}
