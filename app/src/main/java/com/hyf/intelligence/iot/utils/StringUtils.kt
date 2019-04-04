package com.hyf.intelligence.iot.utils

import java.util.regex.Pattern
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView

object StringUtils {
    private val emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    fun isEmail(email: String?): Boolean {
        return if (email == null || email.trim { it <= ' ' }.length == 0) false else emailer.matcher(email).matches()
    }

    /**
     * 判断是否是手机
     *
     * @param phone
     * @return
     */
    fun isPhone(phone: String): Boolean {
        val regExp = "^[1]\\d{10}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phone)
        return m.find()
    }

    /**
     * 判断控件中的content是否为空
     *
     * @param v
     * @return
     */
    fun isEmpty(v: TextView): Boolean {
        return TextUtils.isEmpty(v.text) || "" == v.text.toString().trim { it <= ' ' }
    }

    /**
     * 获取控件内容
     *
     * @param v
     * @return
     */
    fun toString(v: TextView): String {
        return v.text.toString().trim { it <= ' ' }
    }

    /**
     * 手机号匿名显示
     *
     * @param phone
     * @return
     */
    fun jiaMIPhone(phone: String): String {

        return if (isPhone(phone)) {
            phone.substring(0, 3) + "****" + phone.substring(7, 11)
        } else phone

    }

    /**
     * 获取密码
     *
     * @param v
     * @return
     */
    fun getPwd(v: EditText): String {
        return v.text.toString()
    }

    /**
     * 密码为6-16位的字符串
     *
     * @param pwd
     * @return
     */

    fun isPwd(pwd: String): Boolean {

        return pwd.length < 17 && pwd.length > 5

    }


    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        return pattern.matcher(str).matches()
    }

    /**
     * 处理作者name,字数太长
     *
     * @param nick
     * @return
     */
    fun AuthorFormat(nick: String): String {
        val len = calculateWeiboLength(nick)
        return if (len > 12) {// 如果长度大于12个汉字或24个英文字符，截取
            nick.substring(0, 9) + "..."

        } else nick
    }

    /**
     * 计算微博内容的长度 1个汉字 == 两个英文字母所占的长度 标点符号区分英文和中文
     *
     * @param str
     * 所要统计的字符序列
     * @return 返回字符序列计算的长度
     */
    fun calculateWeiboLength(str: String): Int {

        var len = 0.0
        for (i in 0 until str.length) {
            val temp = str[i].toInt()
            if (temp > 0 && temp < 127) {
                len += 0.5
            } else {
                len++
            }
        }
        return Math.round(len).toInt()
    }
}
