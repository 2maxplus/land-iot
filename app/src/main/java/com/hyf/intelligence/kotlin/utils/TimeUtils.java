package com.hyf.intelligence.kotlin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.util.Calendar;

public class TimeUtils {
    public static String[] getFormatedTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String hms = formatter.format(time);
        return hms.split(":");
    }

    public static String AddTime(String begain, int end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(begain);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, end);
        System.out.println(sdf.format(ca.getTime()));
        return sdf.format(ca.getTime());
    }

    public static String gettime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = sdf.format(date);
        return dateString;
    }
    public static String convert(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static Long dateDiff(String startTime, String endTime, String str) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;

        float diffs;
        float nhs = 1000 * 60 * 60;// 一小时的毫秒数
        float hours = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            diffs = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
            sec = diff % nd % nh % nm / ns;// 计算差多少秒
            hours = diffs / nhs;
            // 输出结果
            System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"
                    + (min - day * 24 * 60) + "分钟" + sec + "秒。");
            System.out.println("diff="+diff+",hours=" + hour + ",min=" + min);
            if (str.equalsIgnoreCase("h")) {
                return hour;
            } else {
                return min;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (str.equalsIgnoreCase("h")) {
            return hour;
        } else {
            return min;
        }
    }

    public static String dateDiffs(String startTime, String endTime, String str) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        float nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        float nh = 1000 * 60 * 60;// 一小时的毫秒数
        float nm = 1000 * 60;// 一分钟的毫秒数
        float diff;
        float day = 0;
        float hour = 0;
        float min = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
            if (str.equalsIgnoreCase("h")) {
                return String.valueOf(hour);
            } else {
                return String.valueOf(min);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (str.equalsIgnoreCase("h")) {
            return String.valueOf(hour);
        } else {
            return String.valueOf(min);
        }
    }

    public static String getDateHours(String time, String str) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        float nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        float nh = 1000 * 60 * 60;// 一小时的毫秒数
        float nm = 1000 * 60;// 一分钟的毫秒数
        float diff;
        float day = 0;
        float hour = 0;
        float min = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(time).getTime();
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
            if (str.equalsIgnoreCase("h")) {
                return String.valueOf(hour);
            } else {
                return String.valueOf(min);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (str.equalsIgnoreCase("h")) {
            return String.valueOf(hour);
        } else {
            return String.valueOf(min);
        }
    }
}
