package com.yiyun.lockcontroller.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_2 = "yyyyMMddHHmmss";

    /**
     * 将yyyyMMddHHmmss转为yyyy-MM-dd HH:mm:ss
     */
    public static String convertTimeWithConnector(String time) {

        String date = time;
        String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
        return date.replaceAll(reg, "$1-$2-$3 $4:$5:$6");

    }

    /**
     * 将yyyy-MM-dd HH:mm:ss转为yyyyMMddHHmmss
     */
    public static String convertTime(String time) {

        String date = time;
        String reg = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})";
        return date.replaceAll(reg, "$1$2$3$4$5$6");

    }

    /**
     * 字符串格式为 yyyy年MM月dd日
     *
     * @param str 日期的字符串
     * @return Date
     */
    public static Date str2Date(String str) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            date = sdf.parse(str);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        return date;
    }

    /**
     * 计算传入日期离现在的时间差,传入的日期比现在小则return0
     *
     * @param endTime
     * @return
     */
    public static int daysBetween(long endTime) {
        long nowTime = Calendar.getInstance().getTimeInMillis();
        if (nowTime >= endTime)
            return 0;
        else {
            long between_days = (endTime - nowTime) / (1000 * 3600 * 24);
            return (int) between_days;
        }
    }

    public static String getFormatTime(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date currentTime = new Date(timestamp);
        return formatter.format(currentTime);
    }

    //获取当前时间，格式由format指定。
    public static String pickupCurTime(String format) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date currentTime = new Date();
        return formatter.format(currentTime);

    }

    //获取当前时间，缺省为DATE_FORMAT格式。
    public static String pickupCurTime() {

        return pickupCurTime(DATE_FORMAT);

    }

    //将毫秒数数据转为format格式的时间字符串
    public static String millisToTimeFormat(long timestampInMillis, String format) {

        return new SimpleDateFormat(format)
                .format(new Date(timestampInMillis * 1000));

    }

    public static String dateToString(Date date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);

    }

}
