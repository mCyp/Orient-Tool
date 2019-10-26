package com.orient.base.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间的辅助工具类
 * Author WangJie
 * Created on 2018/9/12.
 */
@SuppressWarnings("unused")
@SuppressLint("SimpleDateFormat")
public class DateUtils {

    public static SimpleDateFormat y2sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat M2mFormat = new SimpleDateFormat("MM-dd HH:mm");
    public static SimpleDateFormat y2dFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat H2sFormat = new SimpleDateFormat("HH:mm:ss");

    public static SimpleDateFormat M2dFormat_cn = new SimpleDateFormat("MM月dd日");
    public static SimpleDateFormat y2dFormat_cn = new SimpleDateFormat("yyyy年MM月dd日");
    public static SimpleDateFormat y2sFormat_cn = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");


    /**
     * 时间转字符串
     * @param format 时间格式
     * @param date 日期
     * @return String
     */
    public static String date2Str(SimpleDateFormat format,Date date){
        return format.format(date);
    }

    /**
     * 字符串转具体的时间
     * @param format 时间格式
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date str2Date(SimpleDateFormat format,String dateStr){
        Date date;
        try {
            date = format.parse(dateStr);
        }catch (ParseException e){
            date = null;
        }
        return date;
    }


    /**
     * 加上一天的时间
     *
     * @param date 开始的时间
     * @return 加上一天后的时间
     */
    public static Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 不清楚一天该用那个field，故用Hour
        calendar.add(Calendar.HOUR, 24);
        return calendar.getTime();
    }

    /**
     * 获取Key
     */
    public static String getKey(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "" + (month < 10 ? "0" + month : month) + "" + (day < 10 ? "0" + day : day);
    }

    /**
     * 得到一个月的第一天
     *
     * @param year  年份
     * @param month 月份
     * @return 日期
     */
    public static Date getMonthBeginDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 得到一个月的最后一天
     *
     * @param year  年份
     * @param month 月份
     * @return 日期
     */
    public static Date getMonthEndDay(int year, int month) {
        boolean isLeapYear = year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                calendar.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                calendar.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 2:
                int day = isLeapYear ? 29 : 28;
                calendar.set(Calendar.DAY_OF_MONTH, day);
                break;
        }
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
