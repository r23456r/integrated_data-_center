package com.idc.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.idc.vo.NewsDomain;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewSService {


    @Test
    public void test() {
         String str = "C:\\Users\\PandaIP\\Desktop\\data.zip";
        File unzip = ZipUtil.unzip(str);
        File imgFile = null;
        File jsonFile = null;
        for (File file : Objects.requireNonNull(unzip.listFiles())) {
            if (file.getName().equals("img")) {
                imgFile = file;
            }
            if (file.isFile() & file.getName().contains("json")) {
                jsonFile = file;
            }
        }
        Map<String,String> map = new HashMap<>();

        if (imgFile != null) {
            for (File img : imgFile.listFiles()) {
                //upload
                String imgPath = img.getAbsolutePath();
                map.put(img.getName(), imgPath);
            }
        }
        if (jsonFile != null) {
            FileReader reader = new FileReader(jsonFile.getAbsolutePath());
            NewsDomain newsDomain = JSONUtil.toBean(reader.readString(), NewsDomain.class);
            String contents = newsDomain.getContents();
            for (String s : map.keySet()) {
                contents = contents.replace(s, map.get(s));
            }
            //mapper 入库
            System.out.println(contents);
        }
        System.out.println(unzip.getTotalSpace());
    }
    @Test
    public  void test2() {
        System.out.println("1111".replace("1", "2"));
    }
}
