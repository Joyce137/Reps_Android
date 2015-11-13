package com.joyce.reps.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	// 大小端转化问题
	// short转化成byte数组
	public static byte[] shortToLH(int n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		return b;
	}

	// int转化成byte数组
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	// byte数组转化为int
	public static int vtolh(byte[] bArr) {
		byte begin = bArr[0];
		if (begin < 0) {
			short n = 0;
			for (int i = 0; i < bArr.length && i < 4; i++) {
				int left = i * 8;
				n += (bArr[i] << left);
			}
			return n + 256;
		} else {
			short n = 0;
			for (int i = 0; i < bArr.length && i < 4; i++) {
				int left = i * 8;
				n += (bArr[i] << left);
			}
			return n;
		}
	}

	// 交换对象
	public static void SWAP(Object a, Object b) {
		Object c = a;
		a = b;
		b = c;
	}

	// 生成随机字符串
	public static String getRandomStr() {
		int SIZE = 18;
		String CCH = "_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
		char[] ch = new char[SIZE];
		for (int i = 0; i < SIZE - 1; ++i) {
			int x = (int) (Math.random() * 100 % (CCH.length() - 1));
			ch[i] = CCH.charAt(x);
		}
		String str = new String(ch);

		return str;
	}

	// 使用正则表达式过滤非数字的字符串
	public static String filterUnNumber(String str) {
		// 只允数字
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		// 替换与模式匹配的所有字符（即非数字的字符将被""替换）
		return m.replaceAll("").trim();
	}

	// 获取当前日期
	public static String getDate() {
		// 日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(new Date());
	}

	// 获取当前时间
	public static String getTime() {
		// 时间
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		return formatter.format(new Date());

	}
	
	//判断是否仅包括数字、下划线和字母
	public static boolean onlyIncludingNumber_Letter(String str){
		for(int i = 0;i<str.length();i++){
			char ch = str.charAt(i);
			if((ch>='0'&&ch<='9')||(ch>='a'&&ch<='z')||(ch>='a'&&ch<='z')||ch == '_'){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}
}
