package com.idc.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author ZYH
 */
public class DateUtils {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String  BJ2PST(String dateStr) {
        //获取当前时间的16小时前
        LocalDateTime parse = LocalDateTime.parse(dateStr, dateTimeFormatter);
        LocalDateTime localDateTime = parse.minusHours(16);
        return dateTimeFormatter.format(localDateTime);
    }
    public static String  BJ2PSTWithPattern(String dateStr,String pattern) {
        //获取当前时间的16小时前
        LocalDateTime parse = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        LocalDateTime localDateTime = parse.minusHours(16);
        return dateTimeFormatter.format(localDateTime);
    }
    public static String PST2BJ(String dateStr,String pattern) {
        //获取当前时间的16小时后
        LocalDateTime parse = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        LocalDateTime localDateTime = parse.plusHours(10);
        return dateTimeFormatter.format(localDateTime);
    }
    public static String PST2BJWithPattern(String dateStr,String pattern) {
        //获取当前时间的16小时后
        LocalDateTime parse = LocalDateTime.parse(dateStr, dateTimeFormatter);
        LocalDateTime localDateTime = parse.plusHours(10);
        return dateTimeFormatter.format(localDateTime);
    }
    public static String nextMidNight(String dateStr, String pattern) {
        //获取第二天00:00分
        LocalDateTime today_start = LocalDateTime.of(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern)).plusDays(1), LocalTime.MIN);
        return dateTimeFormatter.format(today_start);
    }
    public static String nextMidNight(String dateStr) {
        //获取第二天00:00分
        LocalDateTime today_start = LocalDateTime.of(LocalDate.parse(dateStr, dateTimeFormatter).plusDays(1), LocalTime.MIN);
        return dateTimeFormatter.format(today_start);
    }
}
