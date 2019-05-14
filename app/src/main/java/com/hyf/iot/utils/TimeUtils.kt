package com.hyf.iot.utils

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat

object TimeUtils {
    @SuppressLint("SimpleDateFormat")
    fun dateDiff(startTime: String, endTime: String, str: String): Long? {
        // 按照传入的格式生成一个simpledateformate对象
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val nd = (1000 * 24 * 60 * 60).toLong()// 一天的毫秒数
        val nh = (1000 * 60 * 60).toLong()// 一小时的毫秒数
        val nm = (1000 * 60).toLong()// 一分钟的毫秒数
        val ns: Long = 1000// 一秒钟的毫秒数
        val diff: Long
        var day: Long = 0
        var hour: Long = 0
        var min: Long = 0
        var sec: Long = 0
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).time - sd.parse(startTime).time
            day = diff / nd// 计算差多少天
            hour = diff % nd / nh + day * 24// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60// 计算差多少分钟
            sec = diff % nd % nh % nm / ns// 计算差多少秒
            // 输出结果
            println("时间相差：" + day + "天" + (hour - day * 24) + "小时"
                    + (min - day * 24 * 60) + "分钟" + sec + "秒。")
            println("diff=$diff,hours=$hour,min=$min")
            return if (str.equals("h", ignoreCase = true)) {
                hour
            } else {
                min
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return if (str.equals("h", ignoreCase = true)) {
            hour
        } else {
            min
        }
    }

    fun dateDiff(startTime: String, endTime: String): String? {
        // 按照传入的格式生成一个simpledateformate对象
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val nd = (1000 * 24 * 60 * 60).toLong()// 一天的毫秒数
        val nh = (1000 * 60 * 60).toLong()// 一小时的毫秒数
        val nm = (1000 * 60).toLong()// 一分钟的毫秒数
        val ns: Long = 1000// 一秒钟的毫秒数
        val diff: Long
        var day: Long = 0
        var hour: Long = 0
        var min: Long = 0
        var diffText = ""
        var sec: Long = 0
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).time - sd.parse(startTime).time
            day = diff / nd// 计算差多少天
            hour = diff % nd / nh + day * 24// 计算差多少小时
            min = diff % nd % nh / nm //+ day * 24 * 60// 计算差多少分钟
            sec = diff % nd % nh % nm / ns// 计算差多少秒
            if(hour.toInt() != 0)
                diffText = hour.toString() + "H"
            if(min.toInt() != 0)
                diffText += min.toString() + "M"
            if(hour.toInt() == 0 && min.toInt() == 0)
                diffText = sec.toString() + "s"
            return diffText
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return diffText
    }

    @SuppressLint("SimpleDateFormat")
    fun dateDiffs(startTime: String, endTime: String, str: String): String {
        // 按照传入的格式生成一个simpledateformate对象
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val nd = (1000 * 24 * 60 * 60).toFloat()// 一天的毫秒数
        val nh = (1000 * 60 * 60).toFloat()// 一小时的毫秒数
        val nm = (1000 * 60).toFloat()// 一分钟的毫秒数
        val diff: Float
        var day = 0f
        var hour = 0f
        var min = 0f
        // 获得两个时间的毫秒时间差异
        try {
            diff = (sd.parse(endTime).time - sd.parse(startTime).time).toFloat()
            day = diff / nd// 计算差多少天
            hour = diff % nd / nh + day * 24// 计算差多少小时
            min = diff % nd % nh / nm + day * 24f * 60f// 计算差多少分钟
            return if (str.equals("h", ignoreCase = true)) {
                DecimalFormat("0.00").format(hour).toString()
            } else {
                DecimalFormat("0").format(min).toString()
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return if (str.equals("h", ignoreCase = true)) {
            DecimalFormat("0.00").format(hour).toString()
        } else {
            DecimalFormat("0").format(min).toString()
        }
    }


    /**  
     * 得到现在小时  
     */
    @SuppressLint("SimpleDateFormat")
    fun getHour(date: String): String {
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val hour: String
        val dateString = sd.format(sd.parse(date))
        hour = dateString.substring(11, 13)
        return hour
    }

    /**  
     * 得到现在分钟  
     *  
     * @return  
     */
    @SuppressLint("SimpleDateFormat")
    fun getMin(date: String): String {
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val min: String
        val dateString = sd.format(sd.parse(date))
        min = dateString.substring(14, 16)
        return min
    }

}
