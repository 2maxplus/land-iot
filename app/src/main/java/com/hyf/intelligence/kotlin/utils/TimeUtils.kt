package com.hyf.intelligence.kotlin.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date
import java.util.Calendar

object TimeUtils {
    fun getFormatedTime(time: Long): Array<String> {
        val formatter = SimpleDateFormat("HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        val hms = formatter.format(time)
        return hms.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    fun AddTime(begain: String, end: Int): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = null
        try {
            date = sdf.parse(begain)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val ca = Calendar.getInstance()
        ca.time = date
        ca.add(Calendar.HOUR_OF_DAY, end)
        println(sdf.format(ca.time))
        return sdf.format(ca.time)
    }

    fun gettime(): String {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return sdf.format(date)
    }

    fun convert(time: String): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val lcc = java.lang.Long.valueOf(time)
        val i = Integer.parseInt(time)
        return sdr.format(Date(i * 1000L))

    }

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

        val diffs: Float
        val nhs = (1000 * 60 * 60).toFloat()// 一小时的毫秒数
        var hours = 0f
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).time - sd.parse(startTime).time
            diffs = (sd.parse(endTime).time - sd.parse(startTime).time).toFloat()
            day = diff / nd// 计算差多少天
            hour = diff % nd / nh + day * 24// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60// 计算差多少分钟
            sec = diff % nd % nh % nm / ns// 计算差多少秒
            hours = diffs / nhs
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
                hour.toString()
            } else {
                min.toString()
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return if (str.equals("h", ignoreCase = true)) {
            hour.toString()
        } else {
            min.toString()
        }
    }

    fun getDateHours(time: String, str: String): String {
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
            diff = sd.parse(time).time.toFloat()
            day = diff / nd// 计算差多少天
            hour = diff % nd / nh + day * 24// 计算差多少小时
            min = diff % nd % nh / nm + day * 24f * 60f// 计算差多少分钟
            return if (str.equals("h", ignoreCase = true)) {
                hour.toString()
            } else {
                min.toString()
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return if (str.equals("h", ignoreCase = true)) {
            hour.toString()
        } else {
            min.toString()
        }
    }
}
