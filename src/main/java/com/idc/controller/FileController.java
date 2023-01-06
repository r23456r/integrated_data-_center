package com.idc.controller;

import com.alibaba.fastjson.JSONObject;
import com.idc.service.DataIntegrateService;
import com.idc.service.DataService;
import com.idc.service.NewSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/wto")
public class FileController {

    @Autowired
    private NewSService service;

    @ResponseBody
    @GetMapping("/getData/id")
    public String getData(@PathVariable String id) {
        return null;
    }

    @ResponseBody
    @GetMapping("/getAllCompanys")
    public String getAllCompanys() {

        return null;
    }
}
