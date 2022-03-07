package com.idc.controller;

import com.alibaba.fastjson.JSONObject;
import com.idc.service.DataIntegrateService;
import com.idc.service.DataService;
import com.idc.service.impl.WTODataServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wto")
public class WTOController {

    @Autowired
    private DataIntegrateService dataIntegrateService;

    @Autowired
    @Qualifier(value = "WTODataServiceImpl")
    private DataService dataService;

    @ResponseBody
    @GetMapping("/getData")
    public String getData() {
        JSONObject jsonObject = dataService.getDataInfo();
        dataIntegrateService.insertData("wto", jsonObject);
        return jsonObject.toJSONString();
    }

}
