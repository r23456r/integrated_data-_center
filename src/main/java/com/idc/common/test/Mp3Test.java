package com.idc.common.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Mp3Test {
    public static void main(String[] args) {
        Mp3Test ktf = new Mp3Test();
    /*String tempPath = "D:/KuGou/mp3/2fad259e357078e89404be12e1fd7ae3.kgtemp";
    String krcPath ="D:/KuGou/Lyric/周杰伦、袁咏琳 - 怎么了-2fad259e357078e89404be12e1fd7ae3.krc";
    ktf.Change(tempPath,krcPath);*/
        String tempDir ="G:\\KuGou\\Temp";
        String krcDir="G:\\KuGou\\Lyric";
        ktf.ChangeByDir(tempDir, krcDir);
    }
    public static String KGTEMP = ".kgtemp";
    public static String KRC = "krc";
    public void Change(String tempPath, String krcPath) {
        File temp = new File(tempPath);
        File krc = new File(krcPath);
        if (temp.exists() && temp.getName().endsWith(KGTEMP)) {
            String filename = temp.getName();
            String filemd5 = filename
                    .substring(0, filename.lastIndexOf(KGTEMP));
            if (!krc.exists())
                return;
            String krcname = krc.getName();
            String krcmd5 = krcname.substring(krcname.lastIndexOf("-") + 1,
                    krcname.lastIndexOf(KRC) - 1);
            String mp3name = krcname.substring(0, krcname.lastIndexOf("-"));
            if (krcmd5.equals(filemd5)) {
                String path = temp.getPath().substring(0,
                        temp.getPath().lastIndexOf("\\"));
                File mp3File = new File(path + "\\" + mp3name + ".mp3");
                temp.renameTo(mp3File);
            }
            System.out.println(filename + " " + filemd5);
            System.out.println(krcname + " " + mp3name + " " + krcmd5);
        }
    }
    public void ChangeByDir(String tempPath,String krcPath){
        Map<String,File> temps = fileMd5Map(tempPath);
        Map<String,String> mp3Names = krcNameMd5Map(krcPath);
        for(String key :temps.keySet()){
            File f = temps.get(key);
            if(f.exists()){
                String path = f.getPath().substring(0,
                        f.getPath().lastIndexOf("\\"));
                String mp3Name = mp3Names.get(key);
                File mp3File = new File(path + "\\" + mp3Name + ".mp3");
                if(f.renameTo(mp3File)){
                    System.out.println(f.getName()+" to "+mp3File.getName());
                    System.err.print("  SUCCESS");
                }
            }
        }
    }
    public Map<String, File> fileMd5Map(String path) {
        File dirFile = new File(path);
        Map<String, File> map = null;
        if (dirFile.isDirectory()) {
            map = new HashMap<String, File>();
            for (File f : dirFile.listFiles()) {
                if (f.exists()&&f.isFile()&& f.getName().endsWith(KGTEMP)) {
                    String filename = f.getName();
                    String filemd5 = filename.substring(0,
                            filename.lastIndexOf(KGTEMP));
                    map.put(filemd5, f);
                }
            }
        }
        return map;
    }
    public Map<String,String> krcNameMd5Map(String path){
        File dirFile = new File(path);
        Map<String, String> map = null;
        if (dirFile.isDirectory()) {
            map = new HashMap<String, String>();
            for (File f : dirFile.listFiles()) {
                if (f.exists()&&f.isFile()&& f.getName().endsWith(KRC)) {
                    String krcname = f.getName();
                    if(!krcname.contains("-"))continue;
                    String krcmd5 = krcname.substring(krcname.lastIndexOf("-") + 1,
                            krcname.lastIndexOf(KRC) - 1);
                    String mp3name = krcname.substring(0, krcname.lastIndexOf("-"));
                    map.put(krcmd5, mp3name);
                }
            }
        }
        return map;
    }
}
