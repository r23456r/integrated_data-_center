package com.idc.common.utils;

import java.io.*;

public class TextIOStreamUtils {

    public static void writeByFileWrite(String path, String string) {
        PrintStream stream = null;


        File file = new File(path);
        if (!file.exists()) {
            File father = file.getParentFile();
            if (!father.exists()) {
                father.mkdirs();
            }
//            file.mkdir();
        }
        try {
            stream = new PrintStream(path);//写入的文件path

            stream.print(string);//写入的字符串
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } finally {
            stream.close();
        }
    }

    public static void writeByFileWrite2(String path, String string) {
        FileOutputStream fis= null;
        OutputStreamWriter osw = null;
        try {
            fis = new FileOutputStream(path);
             osw=new OutputStreamWriter(fis,"GBK");
            osw.write(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                osw.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static String readerFile(String path) {
        String jsonStr = "";
        try {
            File jsonFile = new File(path);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
