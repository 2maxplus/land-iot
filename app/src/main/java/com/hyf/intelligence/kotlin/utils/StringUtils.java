package com.hyf.intelligence.kotlin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	private StringUtils() {
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 判断是否是手机
	 *
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		String regExp = "^[1]\\d{10}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(phone);
		return m.find();
	}

	/**
	 * 判断控件中的content是否为空
	 *
	 * @param v
	 * @return
	 */
	public static boolean isEmpty(TextView v) {
		return TextUtils.isEmpty(v.getText())
				|| "".equals(v.getText().toString().trim());
	}

	/**
	 * 获取控件内容
	 *
	 * @param v
	 * @return
	 */
	public static String toString(TextView v) {
		return v.getText().toString().trim();
	}

	/**
	 * 手机号匿名显示
	 *
	 * @param phone
	 * @return
	 */
	public static String jiaMIPhone(String phone) {

		if (isPhone(phone)) {
			return phone.substring(0, 3) + "****" + phone.substring(7, 11);
		}
		return phone;

	}

	/**
	 * 获取密码
	 *
	 * @param v
	 * @return
	 */
	public static String getPwd(EditText v) {
		return v.getText().toString();
	}

	/**
	 * 密码为6-16位的字符串
	 *
	 * @param pwd
	 * @return
	 */

	public static boolean isPwd(String pwd) {

		return pwd.length() < 17 && pwd.length() > 5;

	}


	/**
	 * 是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 处理作者name,字数太长
	 * 
	 * @param nick
	 * @return
	 */
	public static String AuthorFormat(String nick) {
		int len = calculateWeiboLength(nick);
		if (len > 12) {// 如果长度大于12个汉字或24个英文字符，截取
			return nick.substring(0, 9) + "...";

		}
		return nick;
	}

	/**
	 * 计算微博内容的长度 1个汉字 == 两个英文字母所占的长度 标点符号区分英文和中文
	 * 
	 * @param str
	 *            所要统计的字符序列
	 * @return 返回字符序列计算的长度
	 */
	public static int calculateWeiboLength(String str) {

		double len = 0;
		for (int i = 0; i < str.length(); i++) {
			int temp = (int) str.charAt(i);
			if (temp > 0 && temp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return (int) Math.round(len);
	}
}
