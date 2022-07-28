package com.idc.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 封装HttpClient工具，方便爬取网页内容
 */
public abstract class HttpWormUtils {
    private static PoolingHttpClientConnectionManager cm; // 声明httpclient管理对象（HttpClient连接池）
    private static SSLConnectionSocketFactory sslConnectionSocketFactory;
    private static List<String> userAgentList = null;
    private static RequestConfig config = null;
    private static BasicCookieStore basicCookieStore = null;

    // 静态代码块会在类被加载的时候执行
    static {
        // 1.创建httpclient连接管理器
        cm = new PoolingHttpClientConnectionManager();
        // 2.设置参数
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new org.apache.http.conn.ssl.TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);


        config = RequestConfig.custom()
                .setSocketTimeout(100000) // 设置连接超时时间
                .setConnectTimeout(100000) // 设置创建连接超时时间
                .setConnectionRequestTimeout(100000) // 设置请求超时时间
                .build();
        userAgentList = new ArrayList<String>();
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
////        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");                             //Mac Chrome
////        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:65.0) Gecko/20100101 Firefox/65.0");                                                                   //Mac Firefox
////        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0.3 Safari/605.1.15");                              //Mac Safari
////        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");                                  //Windows Chrome
////        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763");                    //Windows Edge
////        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");                                                                                //Windows IE
////        userAgentList.add("Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) CriOS/31.0.1650.18 Mobile/11B554a Safari/8536.25");    //iOS Chrome
////        userAgentList.add("Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12F70 Safari/600.1.4");                //iOS Safari
////        userAgentList.add("Mozilla/5.0 (Linux; Android 4.2.1; M040 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36");                //Android Chrome
//        userAgentList.add("Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; M351 Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");              //Android Webkit

    }

    public static String getString(String url) {
        // 1.从连接池中获取httpClient对象
//        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setConnectionManager(cm).build();

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        // 2.创建HttpGet对象
        HttpGet httpGet = new HttpGet(url);
        // 3.设置请求配置对象跟请求头

        httpGet.setHeader("Cookie", "PHPSESSID=6cf2a9km677l7672dnqi0186v2; _hjSessionUser_1543609=eyJpZCI6ImIxY2M1OGI1LTYyY2QtNTYzZS1iNzBkLWExZjVkMzU0ZWM4NyIsImNyZWF0ZWQiOjE2NTA4NDkwMTMyNjQsImV4aXN0aW5nIjpmYWxzZX0=; _hjFirstSeen=1; _hjSession_1543609=eyJpZCI6IjllNWRiZjAyLTkyMzUtNDk0OS05MGM4LTBmODA4MmIwYjI2ZCIsImNyZWF0ZWQiOjE2NTA4NDkwMTMyNzUsImluU2FtcGxlIjp0cnVlfQ==; _hjIncludedInSessionSample=1; _hjAbsoluteSessionInProgress=0; _ga=GA1.2.387825206.1650849064; _gid=GA1.2.1030049007.1650849064; _gat=1");
        httpGet.setHeader("authorization", "Bearer 098b0352001095eca3ad477674e09cac");

        // httpGet.setHeader("user-agent", userAgentList.get(new Random().nextInt(userAgentList.size())));
        // httpGet.setHeader("Cookie", "_trs_uv=ksl2iwpq_6_84ch; u=6; JSESSIONID=acqLSUiO5AQP48rKtUoCAChPJi1zxI32IqGFaYSYSJtdpjlBDFrf!1396203093");
        // 4.发起请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);    // 4.获取相应数据
            if (response.getStatusLine().getStatusCode() == 200 && response.getEntity() != null) { // 200表示响应成功
                String html = EntityUtils.toString(response.getEntity(), "UTF-8");
                return html;
            } else {
                System.out.println("error2");
            }

        } catch (IOException e) {
            System.out.println("error2");
            e.printStackTrace();
        } finally {
            try {
                response.close();
                //httpClient.close(); // 注意这里的httpclient是从连接池获取的 不需要关闭
            } catch (IOException e) {
                System.out.println("error2");
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String downloadFromUrl(String url, String dir, String fileName) {

        try {
            URL httpurl = new URL(url);

            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return "Successful!";
    }


    public static String getFileNameFromUrl(String url) {
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (name.contains("?")) {
                index = name.lastIndexOf("?");
                name = name.substring(0, index);
                return name;
            } else if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }

    public static String getHtml(String url) {
        return getHtml(url, "UTF-8");
    }

    public static String getHtml(String url, String charset) {
        // 1.从连接池中获取httpClient对象
//        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setConnectionManager(cm).build();

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        // 2.创建HttpGet对象
        HttpGet httpGet = new HttpGet(url);
        // 3.设置请求配置对象跟请求头
        httpGet.setConfig(config);
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36");
        httpGet.setHeader("Cookie", "_trs_uv=ksl2iwpq_6_84ch; u=1; JSESSIONID=tmr5oN7JykkbfjkUhH-I3O-QNgbVB8aKb19k0Wo-lJCPGuoKrYHv!2136956367");
        // 4.发起请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);    // 4.获取相应数据
            if (response.getStatusLine().getStatusCode() == 200 && response.getEntity() != null) { // 200表示响应成功
                String html = "";
                if (charset == null) {
                    html = EntityUtils.toString(response.getEntity());
                } else {
                    html = EntityUtils.toString(response.getEntity(), charset);
                }
                return html;
            } else {
                Thread.sleep(500);
                response = httpClient.execute(httpGet);    // 4.获取相应数据
                if (response.getStatusLine().getStatusCode() == 200 && response.getEntity() != null) { // 200表示响应成功
                    String html = "";
                    if (charset == null) {
                        html = EntityUtils.toString(response.getEntity());
                    } else {
                        html = EntityUtils.toString(response.getEntity(), charset);
                    }
                    return html;
                } else {
                    System.out.println("error2" + " :: " + httpGet);
                }
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("error2" + e);
            e.printStackTrace();
        } finally {
            try {
                response.close();
                //httpClient.close(); // 注意这里的httpclient是从连接池获取的 不需要关闭
            } catch (IOException e) {
                System.out.println("error2");
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getHtmlGet(String url, Map<String, String> paramData) {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        sb.append(url);
        for (String key : paramData.keySet()) {
            if (first) {
                sb.append("?");
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(key).append("=").append(paramData.get(key));
        }
        String html = getHtml(sb.toString());
        try {
            JSONObject.parseObject(html);
            int time = new Random().nextInt(1500) % (1500 - 1000 + 1) + 2000;
            Thread.sleep(time);
        } catch (Exception e) {
            if (html.contains(",\"sort")) {
                html = html.replace(",\"sort", "}]}]}}");
                try {
                    JSONObject.parseObject(html);
                    int time = new Random().nextInt(1500) % (1500 - 1000 + 1) + 2000;
                    Thread.sleep(time);
                } catch (Exception e2) {

                    System.out.println("error1" + html);
                    return new JSONObject().toJSONString();
                }
            } else if (html.contains("\"nodesort\":\"1")) {
                html = html + "\"}]}]}}";
                try {
                    JSONObject.parseObject(html);
                    int time = new Random().nextInt(1500) % (1500 - 1000 + 1) + 2000;
                    Thread.sleep(time);
                } catch (Exception e2) {

                    System.out.println("error1" + html);
                    return new JSONObject().toJSONString();
                }
            } else {
                System.out.println("error1" + html);
                return new JSONObject().toJSONString();
            }
            return html;
        }


        return html;
    }

    public static String getHtmlPost(String url, Map<String, String> paramData) {
        // 1.从连接池中获取httpClient对象
//        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setConnectionManager(cm).build();
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        // 2.创建HttpGet对象
        HttpPost httpPost = new HttpPost(url);
        // 3.设置请求配置对象跟请求头
        httpPost.setConfig(config);
        // 4.发起请求
        CloseableHttpResponse response = null;
        try {
            // 准备集合用来存储请求参数
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (String key : paramData.keySet()) {
                params.add(new BasicNameValuePair(key, paramData.get(key)));
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(entity); // 设置请求体/参数
            httpPost.setHeader("Cookie", "_trs_uv=ksl2iwpq_6_84ch; u=6; JSESSIONID=tmr5oN7JykkbfjkUhH-I3O-QNgbVB8aKb19k0Wo-lJCPGuoKrYHv!2136956367");

            httpPost.setHeader("user-agent", userAgentList.get(new Random().nextInt(userAgentList.size())));
            response = httpClient.execute(httpPost);    // 4.获取相应数据
            if (response.getStatusLine().getStatusCode() == 200 && response.getEntity() != null) { // 200表示响应成功
                String html = EntityUtils.toString(response.getEntity(), "UTF-8");
                Thread.sleep(1000);
                return html;
            } else if (response.getStatusLine().getStatusCode() == 307) {
                Thread.sleep(2000);
                Header header = response.getFirstHeader("location");
                String newuri = header.getValue();
                httpPost = new HttpPost(url);
                httpPost.setHeader("user-agent", userAgentList.get(new Random().nextInt(userAgentList.size())));
                httpPost.setHeader("Cookie", "_trs_uv=ksl2iwpq_6_84ch; u=1; JSESSIONID=yaXxTD05FZGTl8jtW6088-zd4h2I1H0NN6QrwisKKy2j6NBV9V5h!2136956367");
                httpPost.setEntity(entity);

                response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() == 200 && response.getEntity() != null) { // 200表示响应成功
                    String html = EntityUtils.toString(response.getEntity(), "UTF-8");
                    return html;
                } else {
                    System.out.println("error3");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                response.close();

                //httpClient.close(); // 注意这里的httpclient是从连接池获取的 不需要关闭
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
