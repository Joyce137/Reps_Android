package com.example.ustc.healthreps.videoAudio.androiddecoder_log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	// short转换成byte数组
	public static byte[] shortToLH(int n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		return b;
	}

	// int转换成byte数组
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	// byte转换成int
	public static int vtolh(byte[] bArr) {
		byte begin = bArr[0];
		if (begin < 0) {
			int n = 0;
			for (int i = 0; i < bArr.length && i < 4; i++) {
				int left = i * 8;
				n += (bArr[i] << left);
			}
			n += 256;
			if(n < 0)
				n+=65536;
			return n;
		} else {
			int n = 0;
			for (int i = 0; i < bArr.length && i < 4; i++) {
				int left = i * 8;
				n += (bArr[i] << left);
			}
			if(n<0){
				n = n + 65536;
			}
			return n;
		}
	}

	// 浜ゆ崲瀵硅薄
	public static void SWAP(Object a, Object b) {
		Object c = a;
		a = b;
		b = c;
	}

	// 鐢熸垚闅忔満瀛楃涓�
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

	// 浣跨敤姝ｅ垯琛ㄨ揪寮忚繃婊ら潪鏁板瓧鐨勫瓧绗︿覆
	public static String filterUnNumber(String str) {
		// 鍙厑鏁板瓧
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		// 鏇挎崲涓庢ā寮忓尮閰嶇殑鎵�鏈夊瓧绗︼紙鍗抽潪鏁板瓧鐨勫瓧绗﹀皢琚�""鏇挎崲锛�
		return m.replaceAll("").trim();
	}

	// 鑾峰彇褰撳墠鏃ユ湡
	public static String getDate() {
		// 鏃ユ湡
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(new Date());
	}

	// 鑾峰彇褰撳墠鏃堕棿
	public static String getTime() {
		// 鏃堕棿
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		return formatter.format(new Date());

	}
	
	//鍒ゆ柇鏄惁浠呭寘鎷暟瀛椼�佷笅鍒掔嚎鍜屽瓧姣�
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


	//鑾峰彇鐜板湪鏃堕棿
	public static String getFileNameDate(String filename) {
		//鎷嗗垎
		String[] fileNameArray = filename.split("-");
		String dataStr = fileNameArray[4];

		//鍒ゆ柇鏄惁涓烘椂闂翠覆
		if(Utils.filterUnNumber(dataStr)==dataStr && dataStr.length() == 8){
			return dataStr;
		}
		//濡傛灉涓嶆槸锛屽垯浣跨敤鐜板湪鏃堕棿
		return Utils.getDate();
	}
}
