package com.idc.common.utils;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import org.springframework.http.*;

import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.util.UriComponentsBuilder;
/**
 * @author: Seven.wk
 * @description: 辅助工具类
 * @create: 2018/07/04
 */
public class HttpUtils {
    private static final String PROXY_SERVER_HOST = "127.0.0.1";
    final static int PROXY_SERVER_PORT = 7891;

    public static String toGet(String url, Map<String, Object> map) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, PROXY_SERVER_PORT));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (String s : map.keySet()) {
            builder.queryParam(s, map.get(s));
        }
        String urlTemplate = builder
                .encode()
                .toUriString();

        Map<String, ?> params = new HashMap<>();
//        params.put("msisdn", msisdn);

        String body = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                null,
                String.class,
                params
        ).getBody();
        return body;
    }

    /**
     * 国内无法强制走代理
     *
     * @return
     */
    public String hutoolGet() {
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7891");
        System.out.println("success" + HttpUtil.get("www.youtube.com"));
//        String url = "https://api.wto.org/timeseries/v1/data?i=BAT_BV_M&r=156&max=1000000&fmt=json&mode=full&lang=1&meta=false&subscription-key=1dc531027a3b48a588e167c449bdb739";
        String url = "https://api.wto.org/timeseries/v1/data?i=TP_A_0010&r=156&fmt=json&mode=full&lang=1&meta=false&subscription-key=1dc531027a3b48a588e167c449bdb739";
        return HttpUtil.get(url);


    }
    /**
     * 向目的URL发送post请求
     * @param url       目的url
     * @param params    发送的参数
     * @return  ResultVO
     */
   public static JSONObject sendPostRequest(String url, MultiValueMap<String, String> params){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交

        headers.set("Cookie","AMCVS_1E7B833554B8360D0A4C98A5@AdobeOrg=1; s_cc=true; _gcl_au=1.1.609164218.1650418894; databank.cookie=769702154.20992.0000; BIGipServerdatabank-https.sfarm=1621209098.47873.0000; ASP.NET_SessionId=glmvk3qmulrvtwk05eudx1nc; preview=N; cebs=1; _ce.s=v~f79025314d960e9a1e4692bd9de9f700af4e5d44~vpv~0; wb_note_beta=1; QSI_SI_1Ba18fDURaD3du5_intercept=true; TS018f8378=01359ee9762ffa09a3856fa5e790691b9ff4a7aa1fb7611791a8840e9f68db52772ab0195de64c6deb9f4c8716711727740dc4770b8ad7b0d3c9eb52afb7a61d7cf275dbadbc0bd1ad6ce379a95cc516cfc0bf6592; AMCV_1E7B833554B8360D0A4C98A5@AdobeOrg=-2121179033|MCIDTS|19103|MCMID|19883571724883849931776303187052788684|MCAAMLH-1651046731|11|MCAAMB-1651046731|RKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y|MCOPTOUT-1650449131s|NONE|MCAID|NONE|vVersion|5.3.0; s_vnum=1681954312160&vn=3; s_invisit=true; s_lv_s=Less than 1 day; s_ppn=en:wb:developmentdata:/source/millennium-development-goals; s_ppvl=en%3Awb%3Adevelopmentdata%3A/source/millennium-development-goals,95,95,690,640,690,1536,864,1.25,P; s_ptc=0.18^^0.02^^0.03^^1.21^^0.69^^0.58^^48.50^^0.03^^50.74; uvts=9662ec98-8c27-4eae-61ae-b2e41afcbb6a; TS01b02907=01fbb1b501c74bf08dd9c9c20dc901962ddd6a61bf55738bd0ad488ac4ae3b7a963b569a3dad5e4f11d040b838da0ef686962ebaabf4cf5b9d1abf534b21137e8174c5b84182d83fa9db497e0b8b793b06932f4c44; TS015f8521=017189f947722cb7282e2af8bd88c509a7002bd8e587cbeee075fcf9b79c19c39eaefb5f1e61926ec232f17421653846bce11b5c72f0e8572f84388aec70ad00794e24342ac4ba2f275d6b008be83153195548b5f751316a75eb2e219677ca0863c9827b24; s_ppv=en%3Awb%3Adevelopmentdata%3A/source/millennium-development-goals,78,89,690,756,690,1536,864,1.25,L; s_sq=wbgglobalprod=%26c.%26a.%26activitymap.%26page%3Den%253Awb%253Adevelopmentdata%253A%252Fsource%252Fmillennium-development-goals%26link%3DDatabase%26region%3DnonMetadataReportVariables%26pageIDType%3D1%26.activitymap%26.a%26.c%26pid%3Den%253Awb%253Adevelopmentdata%253A%252Fsource%252Fmillennium-development-goals%26pidt%3D1%26oid%3Dhttps%253A%252F%252Fdatabank.worldbank.org%252Fsource%252Fmillennium-development-goals%2523selectedDimension_DBList%26ot%3DA; s_nr=1650442317597-Repeat; s_lv=1650442317597; ADRUM=s=1650442318889&r=https://databank.worldbank.org/source/millennium-development-goals?0");
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<JSONObject> response = client.exchange(url, method, requestEntity, JSONObject.class);

        return response.getBody();
    }

    public static String sendPostRequest111(String url, MultiValueMap<String, Object> form) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();

        //设置content-type
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);

        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        RestTemplate restTemplate = new RestTemplate();
        String reponseData = restTemplate.postForObject(url, files, String.class);


        return new RestTemplate().postForObject(url, files, String.class);
    }

    public static String sendPostRequest123(String url, Map<String, Object> form,Map<String, String> header) {

        return  HttpUtil.createPost(url).addHeaders(header).body(form.toString()).execute().body();
    }
    public static String sendPostRequest(String url, Map<String, Object> form,Map<String, String> header) {

        return  HttpUtil.createPost(url).addHeaders(header).body(com.alibaba.fastjson.JSONObject.toJSONString(form),"application/json").execute().body();
    }



}
