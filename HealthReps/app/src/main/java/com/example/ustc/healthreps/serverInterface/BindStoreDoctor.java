package com.example.ustc.healthreps.serverInterface;

import java.io.UnsupportedEncodingException;

//绑定药店
public class BindStoreDoctor {
	public String username; // char[15];
	public String pha; // char[15];
	public boolean yesno;	//yes-Store,no-Doctor

	public BindStoreDoctor() {
		username = "";
		pha = "";
		yesno = false;
	}

	// 与byte数组间的转换
	public static int size = 15 + 15 + 1;
	private byte buf[] = new byte[size];

	// 构造并转换为byte数组
	public BindStoreDoctor(String username, String pha, boolean yesno) {
		this.username = username;
		this.pha = pha;
		this.yesno = yesno;

		byte[] temp;
		try {
			// username
			temp = username.getBytes("GBK");
			System.arraycopy(temp, 0, buf, 0, temp.length);
			// pha
			temp = username.getBytes();
			System.arraycopy(temp, 0, buf, 0, temp.length);
			// yesno
			byte bool_byte = (byte) (yesno == true ? 0x01 : 0x00);
			buf[20] = bool_byte;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// 通过byte数组获取相应的对象参数
	public static BindStoreDoctor getBangDingPhaInfo(byte[] buf) {
		String username = "";
		String pha = "";
		boolean yesno = false;

		try {
			// username
			byte[] tempStr = new byte[15];
			System.arraycopy(buf, 0, tempStr, 0, 15);
			username = new String(tempStr, "GBK").trim();

			// pha
			byte[] tempStr1 = new byte[15];
			System.arraycopy(buf, 15, tempStr1, 0, 15);
			pha = new String(tempStr, "GBK").trim();

			// yesno
			byte yesnoByte = buf[30];
			yesno = (yesnoByte == 0x00) ? false : true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new BindStoreDoctor(username, pha, yesno);
	}

	// 返回要发送的byte数组
	public byte[] getBuf() {
		return buf;
	}
}
