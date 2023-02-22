package com.idc.common.translate;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    public static Map<String, String> trans(String srcStr) {
        System.err.println("翻译源" + srcStr);
        Map<String, String> transMap = new HashMap<>();
        String APP_ID = "20220310001118799";
        String SECURITY_KEY = "_I40DqQDM5E0quByUAlu";
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String transResult = api.getTransResult(srcStr, "auto", "zh");
        List<TransVo.Trans_result> resultList = JSONObject.parseObject(transResult, TransVo.class).getTrans_result();
        if (CollectionUtil.isEmpty(resultList)) {
            System.out.println("翻译结果为空");
        }
        for (TransVo.Trans_result result : resultList) {
            transMap.put(result.getSrc(), result.getDst());
        }
        System.err.println("翻译结果" + transMap.toString());
        return transMap;
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

}
