//package com.idc.service;
//
//import cn.hutool.http.HttpUtil;
//import cn.hutool.poi.excel.ExcelUtil;
//import cn.hutool.poi.excel.ExcelWriter;
//import lombok.extern.slf4j.Slf4j;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//@Slf4j
//public class TVServiceBak {
////    public static void main(String[] args) {
////        String url = "http://epg.51zmt.top:8000/api/i/?ch=%s&date=2023-01-05";
////        String[] split = menu.split("\n");
////        Map<String, Integer> map = new LinkedHashMap();
////        // 通过工具类创建write
////        // 此种方式最后会通过response设置导出
////        ExcelWriter writer = ExcelUtil.getWriter("./MySheet.xlsx", "节目单");
//////        // 设置合并项
//////        writer.merge(0, 1, 0, 0, "序号", false);
//////        writer.merge(0, 1, 1, 1, "电视台名称", false);
//////        writer.merge(0, 0, 2, 4, "开始时间 PST", false);
//////        writer.merge(0, 0, 2, 4, "结束时间 PST", false);
//////        writer.merge(0, 0, 2, 4, "开始时间 BJ", false);
//////        writer.merge(0, 0, 2, 4, "结束时间 BJ", false);
//////        writer.merge(0, 0, 2, 4, "节目（中文）", false);
//////        writer.merge(0, 0, 2, 4, "备注", false);
////
////        // 填充标题
////        List<List<String>> rows = new ArrayList<>();
////        List<String> row = new ArrayList<>();
////        row.add("序号");
////        row.add("电视台名称");
////        row.add("开始时间");
////        row.add("结束时间");
////        row.add("节目");
////        rows.add(row);
////        // 此种方式最后会通过response设置导出
////        for (String tagName : split) {
////            String html = HttpUtil.get(String.format(url, tagName));
////            System.out.println(html);
////            Document doc = Jsoup.parse(html);
////            Elements select = doc.select("ul").select("li");
////            int index = 0;
////            int size = select.size();
////            map.put(tagName, size);
////            for (Element element : select) {
////                List<String> rowData = new ArrayList<>();
////                String text = element.text();
////                String[] split1 = text.split("  ");
////                String time = split1[0];
////                String programName = split1[1];
////                rowData.add(index++ + "");
////                rowData.add(tagName);
////                rowData.add(time);
////                rowData.add(time);
////                rowData.add(programName);
////                rows.add(rowData);
////
////            }
////            if (index == 10) {
////                break;
////            }
////        }
////        for (String s : map.keySet()) {
////            writer.merge(0, map.get(s), 2, 4, s, false);
////        }
////        writer.write(rows, true);
////        // 关闭writer，释放内存
////        writer.close();
////    }
//
//    public static void main(String[] args) {
////        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
////        String date = format.format(dateStr);
//        String html = HttpUtil.get("https://schedule.tvbs.com.tw/tvbs/2023-01-06/taiwan/zh-cn");
//        Document doc = Jsoup.parse(html);
//        Elements select = doc.select("table[class=\"schedule_box\"]");
//        for (Element element : select) {
//            element.text();
//        }
//    }
////    public void tvbs(String dateStr) {
////        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
////        String date = format.format(dateStr);
////        String html = HttpUtil.get(String.format("https://schedule.tvbs.com.tw/tvbs/%s/taiwan/zh-cn", date));
////        Document doc = Jsoup.parse(html);
////        Elements select = doc.select("table[class=\"schedule_box\"]");
////
////    }
////    public static void main(String[] args) {
////        ExcelWriter writer = ExcelUtil.getWriter("d:/writeBeanTest.xlsx", "sheet页名称");
////        // 设置合并项
////        writer.merge(0, 1, 0, 0, "序号", false);
////        writer.merge(0, 1, 1, 1, "频道", false);
////
////        writer.merge(0, 0, 2, 4, "1月填报数据", false);
////
////        // 跳过当前行,否则设置合并会出问题
////        writer.passCurrentRow();
////
////        // 填充标题
////        List<List<String>> rows = new ArrayList<>();
////        List<String> row = new ArrayList<>();
////        row.add("");
////        row.add("");
////        row.add("填报状态");
////        row.add("未填报数量");
////        row.add("未填报市级");
////        rows.add(row);
////
////        // 填充内容
////        int index = 0;
////        for (int i = 0; i < 3; i++) {
////            List<String> rowData = new ArrayList<>();
////            index = i + 1;
////            rowData.add(index + "");
////            rowData.add("名称");
////            rowData.add("完成了");
////            rowData.add("20");
////            rowData.add("无");
////            rows.add(rowData);
////        }
////        writer.write(rows, true);
////        // 关闭writer，释放内存
////        writer.close();
////    }
//
//    //    public void create() {
////
////    }
//    private static String menu =
//            "CCTV1\n" +
//                    "CCTV2\n" +
//                    "CCTV3\n" +
//                    "CCTV4\n" +
//                    "CCTV5\n" +
//                    "CCTV5+\n" +
//                    "CCTV6\n" +
//                    "CCTV7\n" +
//                    "CCTV8\n" +
//                    "CCTV9\n" +
//                    "CCTV10\n" +
//                    "CCTV11\n" +
//                    "CCTV12\n" +
//                    "CCTV13\n" +
//                    "CCTV14\n" +
//                    "CCTV15\n" +
//                    "CCTV17\n" +
//                    "CGTN\n" +
//                    "CCTV4EUO\n" +
//                    "CCTV4AME\n" +
//                    "湖南卫视\n" +
//                    "浙江卫视\n" +
//                    "江苏卫视\n" +
//                    "北京卫视\n" +
//                    "东方卫视\n" +
//                    "安徽卫视\n" +
//                    "广东卫视\n" +
//                    "深圳卫视\n" +
//                    "辽宁卫视\n" +
//                    "旅游卫视\n" +
//                    "山东卫视\n" +
//                    "天津卫视\n" +
//                    "重庆卫视\n" +
//                    "东南卫视\n" +
//                    "甘肃卫视\n" +
//                    "广西卫视\n" +
//                    "贵州卫视\n" +
//                    "河北卫视\n" +
//                    "黑龙江卫视\n" +
//                    "河南卫视\n" +
//                    "湖北卫视\n" +
//                    "江西卫视\n" +
//                    "吉林卫视\n" +
//                    "内蒙古卫视\n" +
//                    "宁夏卫视\n" +
//                    "山西卫视\n" +
//                    "陕西卫视\n" +
//                    "四川卫视\n" +
//                    "新疆卫视\n" +
//                    "云南卫视\n" +
//                    "青海卫视\n" +
//                    "兵团卫视\n" +
//                    "哈哈炫动\n" +
//                    "延边卫视\n" +
//                    "黄河卫视\n" +
//                    "卡酷动画\n" +
//                    "厦门卫视\n" +
//                    "金鹰卡通\n" +
//                    "康巴卫视\n" +
//                    "西藏卫视\n" +
//                    "三沙卫视\n" +
//                    "中国教育1台\n" +
//                    "中国教育2台\n" +
//                    "中国教育3台\n" +
//                    "3D电视试验频道\n" +
//                    "外汇理财\n" +
//                    "电竞天堂\n" +
//                    "IPTV5+\n" +
//                    "IPTV6+\n" +
//                    "IPTV经典电影\n" +
//                    "IPTV热播剧场\n" +
//                    "IPTV少儿动画\n" +
//                    "IPTV魅力时尚\n" +
//                    "CCTV4K\n" +
//                    "翡翠台\n" +
//                    "明珠台\n" +
//                    "TVB经典台\n" +
//                    "无线新闻\n" +
//                    "TVB星河频道\n" +
//                    "无线财经\n" +
//                    "凤凰中文\n" +
//                    "凤凰资讯\n" +
//                    "凤凰香港\n" +
//                    "阳光卫视\n" +
//                    "美亚高清电影台\n" +
//                    "NHKWorld\n" +
//                    "ViuTV\n" +
//                    "HKS\n" +
//                    "J2\n" +
//                    "国家地理野生频道\n" +
//                    "MEZZOLIVEHD\n" +
//                    "Lifetime\n" +
//                    "星卫HD电影\n" +
//                    "东森亚洲新闻台\n" +
//                    "东森亚洲卫视\n" +
//                    "公视2\n" +
//                    "GoodTV2\n" +
//                    "壹电视综合台\n" +
//                    "壹电视电影台\n" +
//                    "BBCWorldNews\n" +
//                    "CN卡通频道\n" +
//                    "CNBCHongKong\n" +
//                    "国家地理频道\n" +
//                    "FashionTV\n" +
//                    "BabyTV\n" +
//                    "FXHD\n" +
//                    "DW\n" +
//                    "TV5Monde\n" +
//                    "france24\n" +
//                    "SkyNews\n" +
//                    "cinemaworld\n" +
//                    "CGTNDocumentary\n" +
//                    "TraceSports\n" +
//                    "Outdoor\n" +
//                    "HITS\n" +
//                    "优漫卡通\n" +
//                    "GTV游戏竞技\n" +
//                    "网络棋牌\n" +
//                    "新动漫\n" +
//                    "山东齐鲁\n" +
//                    "山东体育\n" +
//                    "山东农科\n" +
//                    "山东公共\n" +
//                    "山东少儿\n" +
//                    "山东影视\n" +
//                    "山东综艺\n" +
//                    "山东生活\n" +
//                    "环宇电影\n" +
//                    "湖北综合频道\n" +
//                    "湖北影视频道\n" +
//                    "湖北教育频道\n" +
//                    "湖北生活频道\n" +
//                    "湖北公共·新闻\n" +
//                    "湖北经济频道\n" +
//                    "湖北垄上频道\n" +
//                    "武汉新闻综合频道\n" +
//                    "武汉电视剧频道\n" +
//                    "武汉科技生活频道\n" +
//                    "武汉经济频道\n" +
//                    "武汉文体频道\n" +
//                    "武汉外语频道\n" +
//                    "武汉少儿频道\n" +
//                    "武汉教育电视台\n" +
//                    "北京纪实\n" +
//                    "BTV文艺\n" +
//                    "BTV科教\n" +
//                    "BTV影视\n" +
//                    "BTV财经\n" +
//                    "BTV生活\n" +
//                    "BTV青年\n" +
//                    "BTV新闻\n" +
//                    "DOX雅趣\n" +
//                    "七彩戏剧\n" +
//                    "新视觉\n" +
//                    "劲爆体育\n" +
//                    "DOX英伦\n" +
//                    "DOX剧场\n" +
//                    "MAX极速汽车\n" +
//                    "全纪实\n" +
//                    "欢笑剧场\n" +
//                    "幸福彩\n" +
//                    "生活时尚\n" +
//                    "游戏风云\n" +
//                    "上视新闻频道\n" +
//                    "第一财经\n" +
//                    "东方影视\n" +
//                    "五星体育频道\n" +
//                    "上海纪实\n" +
//                    "上海都市频道\n" +
//                    "上视外语频道\n" +
//                    "中国交通频道\n" +
//                    "金鹰纪实\n" +
//                    "SCTV2\n" +
//                    "SCTV3\n" +
//                    "SCTV4\n" +
//                    "SCTV5\n" +
//                    "SCTV7\n" +
//                    "峨嵋电影\n" +
//                    "SCTV9\n" +
//                    "SCTV8\n" +
//                    "CDTV1\n" +
//                    "CDTV2\n" +
//                    "CDTV3\n" +
//                    "CDTV4\n" +
//                    "CDTV5\n" +
//                    "CDTV6\n" +
//                    "风云足球\n" +
//                    "辽宁都市\n" +
//                    "辽宁影视剧\n" +
//                    "辽宁青少\n" +
//                    "辽宁生活\n" +
//                    "辽宁公共\n" +
//                    "辽宁北方\n" +
//                    "辽宁体育\n" +
//                    "辽宁经济\n" +
//                    "沈阳新闻\n" +
//                    "嘉佳卡通\n" +
//                    "广东体育\n" +
//                    "广东公共\n" +
//                    "珠江海外\n" +
//                    "广东新闻\n" +
//                    "广东国际\n" +
//                    "公视三台\n" +
//                    "CNN\n" +
//                    "民视无线台\n" +
//                    "台视\n" +
//                    "大爱一台\n" +
//                    "中视\n" +
//                    "人间卫视\n" +
//                    "华视\n" +
//                    "公视\n" +
//                    "GoodTV\n" +
//                    "原住民频道\n" +
//                    "客家电视台\n" +
//                    "国家地理频道(台湾)\n" +
//                    "Discovery\n" +
//                    "TLC旅游生活频道\n" +
//                    "动物星球\n" +
//                    "Disney\n" +
//                    "MOMO亲子台\n" +
//                    "东森幼幼台\n" +
//                    "纬来综合台\n" +
//                    "八大第一台\n" +
//                    "八大综合台\n" +
//                    "三立台湾台\n" +
//                    "三立都会台\n" +
//                    "卫视中文台\n" +
//                    "东森综合台\n" +
//                    "东森超视\n" +
//                    "中天综合台\n" +
//                    "东风卫视\n" +
//                    "年代MUCH\n" +
//                    "中天娱乐台\n" +
//                    "东森戏剧台\n" +
//                    "八大戏剧台\n" +
//                    "TVBS欢乐台\n" +
//                    "纬来戏剧台\n" +
//                    "高点综合台\n" +
//                    "JET综合台\n" +
//                    "壹电视新闻台\n" +
//                    "年代新闻台\n" +
//                    "东森新闻台\n" +
//                    "民视新闻台\n" +
//                    "三立新闻台\n" +
//                    "TVBS新闻台\n" +
//                    "TVBS\n" +
//                    "东森财经新闻台\n" +
//                    "非凡新闻台\n" +
//                    "卫视电影台\n" +
//                    "东森电影台\n" +
//                    "纬来电影台\n" +
//                    "LSTime电影台\n" +
//                    "HBO\n" +
//                    "东森洋片台\n" +
//                    "AXN\n" +
//                    "好莱坞电影台\n" +
//                    "FOXMOVIES\n" +
//                    "纬来育乐台\n" +
//                    "CINEMAX\n" +
//                    "纬来体育台\n" +
//                    "FOXSports\n" +
//                    "ELEVEN体育一台\n" +
//                    "纬来日本台\n" +
//                    "国兴卫视\n" +
//                    "FOX\n" +
//                    "靖天综合台\n" +
//                    "靖天资讯台\n" +
//                    "信吉电视台\n" +
//                    "靖洋戏剧台\n" +
//                    "台湾艺术台\n" +
//                    "全大电视台\n" +
//                    "非凡商业台\n" +
//                    "三立财经新闻台\n" +
//                    "运通财经\n" +
//                    "SBN全球财经台\n" +
//                    "诚心电视台\n" +
//                    "NHK\n" +
//                    "MTV\n" +
//                    "靖天映画\n" +
//                    "海豚综合台\n" +
//                    "霹雳台湾台\n" +
//                    "十方法界\n" +
//                    "信大频道\n" +
//                    "华藏卫视\n" +
//                    "Z频道\n" +
//                    "佛卫慈悲台\n" +
//                    "生命频道\n" +
//                    "天良综合台\n" +
//                    "正德电视台\n" +
//                    "高点育乐台\n" +
//                    "冠军梦想台\n" +
//                    "八大娱乐台\n" +
//                    "大立电视台\n" +
//                    "幸福空间居家台\n" +
//                    "ChannelNewsAsia\n" +
//                    "BloombergTV\n" +
//                    "ArirangTV\n" +
//                    "大爱二台\n" +
//                    "台视新闻台\n" +
//                    "台视财经台\n" +
//                    "台视综合台\n" +
//                    "BBCLifestyle\n" +
//                    "DREAMWORKS\n" +
//                    "WarnerTV\n" +
//                    "靖天欢乐台\n" +
//                    "靖天育乐台\n" +
//                    "HBOHD\n" +
//                    "HBOHits\n" +
//                    "HBOSignature\n" +
//                    "HBOFamily\n" +
//                    "BlueAntEntertainment\n" +
//                    "靖天日本台\n" +
//                    "FoodNetwork美食台\n" +
//                    "tvN\n" +
//                    "韩国娱乐台KMTV\n" +
//                    "EVE\n" +
//                    "Discovery科学\n" +
//                    "DiscoveryAsia\n" +
//                    "HGTV居家乐活\n" +
//                    "TravelChannel\n" +
//                    "亚洲美食频道\n" +
//                    "DMAX\n" +
//                    "寰宇新闻\n" +
//                    "亚洲旅游台\n" +
//                    "梅迪奇艺术\n" +
//                    "博斯运动二台\n" +
//                    "博斯网球台\n" +
//                    "博斯无限台\n" +
//                    "博斯高球1台\n" +
//                    "博斯高球2台\n" +
//                    "博斯魅力网\n" +
//                    "博斯运动一台\n" +
//                    "博斯无限二台\n" +
//                    "BlueAntExtreme\n" +
//                    "Euronews\n" +
//                    "Nickelodeon\n" +
//                    "NickJr.\n" +
//                    "达文西频道\n" +
//                    "CBeebies\n" +
//                    "Boomerang\n" +
//                    "BBCEarth\n" +
//                    "靖洋卡通台\n" +
//                    "靖天卡通台\n" +
//                    "三立综合台\n" +
//                    "求索科学\n" +
//                    "CHC高清电影\n" +
//                    "求索动物\n" +
//                    "求索记录\n" +
//                    "CHC动作电影\n" +
//                    "CHC家庭电影\n" +
//                    "梨园\n" +
//                    "风云音乐\n" +
//                    "第一剧场\n" +
//                    "风云剧场\n" +
//                    "世界地理\n" +
//                    "怀旧剧场\n" +
//                    "兵器科技\n" +
//                    "女性时尚\n" +
//                    "CCTV-娱乐\n" +
//                    "CCTV-戏曲\n" +
//                    "CCTV-电影\n" +
//                    "高尔夫网球\n" +
//                    "央视精品\n" +
//                    "彩民在线\n" +
//                    "法律服务\n" +
//                    "汽摩\n" +
//                    "留学世界\n" +
//                    "青年学苑\n" +
//                    "摄影频道\n" +
//                    "天元围棋\n" +
//                    "现代女性\n" +
//                    "早期教育\n" +
//                    "证券资讯\n" +
//                    "央视台球\n" +
//                    "茶频道\n" +
//                    "武术世界\n" +
//                    "发现之旅\n" +
//                    "环球奇观\n" +
//                    "国学\n" +
//                    "文物宝库\n" +
//                    "新科动漫\n" +
//                    "幼儿教育\n" +
//                    "老故事\n" +
//                    "快乐垂钓\n" +
//                    "书画频道\n" +
//                    "DOXTV\n" +
//                    "先锋乒羽\n" +
//                    "车迷频道\n" +
//                    "优优宝贝\n" +
//                    "四海钓鱼\n" +
//                    "动感音乐\n" +
//                    "环球旅游\n" +
//                    "新娱乐\n" +
//                    "京视剧场\n" +
//                    "弈坛春秋\n" +
//                    "央广健康\n" +
//                    "时代家居\n" +
//                    "时代出行\n" +
//                    "时代风尚\n" +
//                    "财富天下\n" +
//                    "百姓健康\n" +
//                    "精品剧场\n" +
//                    "少儿动漫\n" +
//                    "欧美影院\n" +
//                    "ABCAustralia\n" +
//                    "CNNHeadlineNews\n" +
//                    "NHKWorldPremium\n" +
//                    "香港国际财经台\n" +
//                    "香港开电视\n" +
//                    "有线财经资讯台\n" +
//                    "有线新闻台\n" +
//                    "珠江频道\n" +
//                    "龙华偶像\n" +
//                    "龙华戏剧\n" +
//                    "龙华电影\n" +
//                    "龙华经典\n" +
//                    "龙华影剧\n" +
//                    "龙华洋片\n" +
//                    "民视第一台\n" +
//                    "民视台湾台\n" +
//                    "民视\n" +
//                    "中视菁采台\n" +
//                    "TVBS精采台\n" +
//                    "爱尔达娱乐台\n" +
//                    "BabyFirst\n" +
//                    "民视综艺台\n" +
//                    "华艺MBC\n" +
//                    "TRACEUrban\n" +
//                    "ELEVEN体育二台\n" +
//                    "FashionOne\n" +
//                    "History\n" +
//                    "SMART知识频道\n" +
//                    "龙华日韩台\n" +
//                    "靖天戏剧台\n" +
//                    "靖天电影台\n" +
//                    "AMC\n" +
//                    "中视经典\n" +
//                    "iFun1\n" +
//                    "iFun3\n" +
//                    "中视新闻\n" +
//                    "CI\n" +
//                    "cnex\n" +
//                    "采昌影剧\n" +
//                    "智林体育\n" +
//                    "aljazeera\n" +
//                    "影迷数位纪实\n" +
//                    "影迷数位电影\n" +
//                    "ELTV\n" +
//                    "靖天国际\n" +
//                    "龙华动画\n" +
//                    "MTV综合\n" +
//                    "爱尔达体育2\n" +
//                    "LUXETV\n" +
//                    "rollor\n" +
//                    "亚洲综合\n" +
//                    "寰宇HD综合\n" +
//                    "纬来精采\n" +
//                    "Ettoday\n" +
//                    "八大优\n" +
//                    "台湾戏剧\n" +
//                    "爱尔达影剧\n" +
//                    "MY101综合\n" +
//                    "星卫娱乐\n" +
//                    "寰宇财经\n" +
//                    "CatchPlay电影\n" +
//                    "MyCinemaEurope\n" +
//                    "TFC\n" +
//                    "MY-KIDS\n" +
//                    "爱尔达体育1\n" +
//                    "爱尔达体育3\n" +
//                    "狼谷竞技\n" +
//                    "EUROSPORT\n" +
//                    "美食星球\n" +
//                    "EYE旅游\n" +
//                    "爱尔达综合\n" +
//                    "天天电视\n" +
//                    "三立戏剧\n" +
//                    "EYE戏剧\n" +
//                    "曼迪日本\n" +
//                    "StarMoviesHD\n" +
//                    "华艺影剧\n" +
//                    "唯心电视\n" +
//                    "Animax\n" +
//                    "香港603\n" +
//                    "魅力足球\n" +
//                    "中国教育4台\n" +
//                    "CCTV16\n" +
//                    "广东影视\n" +
//                    "广东综艺\n";
//
//
//}
