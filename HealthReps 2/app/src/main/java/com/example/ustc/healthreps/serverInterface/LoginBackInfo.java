package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

//struct Login_Back_Info
public class LoginBackInfo {
	public int getRecon() {
		return recon;
	}

	public void setRecon(int recon) {
		this.recon = recon;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPharmacist() {
		return pharmacist;
	}

	public void setPharmacist(String pharmacist) {
		this.pharmacist = pharmacist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isYesno() {
		return yesno;
	}

	public void setYesno(boolean yesno) {
		this.yesno = yesno;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private String username; // char[15];
	private String pharmacist; // char[15];
	private String title; // char[100];
	private boolean yesno; // 成功还是失败
	private int type; // 登陆的时候 错误类型
	// type = 1 密码和账号错误；
	// type = 2 已经在线;
	// type = 3 使用错了客户端;
	// type = 4 审核未过
	private int recon;

	public LoginBackInfo() {
		username = "";
		pharmacist = "";
		title = "";
		yesno = false;
		type = 0;
		recon = -1;
	}

	public static int size = 15 + 15 + 100 + 2 + 4 + 4;
	private byte[] buf = new byte[size];

	// 构造并转化
	public LoginBackInfo(String username, String pharmacist, String title,
			boolean yesno, int type, int recon) {
		this.username = username;
		this.pharmacist = pharmacist;
		this.title = title;
		this.yesno = yesno;
		this.type = type;
		this.recon = recon;

		byte temp[];
		try {
			// username
			temp = username.getBytes("GBK");
			System.arraycopy(temp, 0, buf, 0, temp.length);
			// pharmacist
			temp = pharmacist.getBytes("GBK");
			System.arraycopy(temp, 0, buf, 15, temp.length);
			// title
			temp = title.getBytes("GBK");
			System.arraycopy(temp, 0, buf, 30, temp.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// yesno
		byte yesnobyte = (byte) (yesno == true ? 0x01 : 0x00);
		buf[130] = yesnobyte;
		// type
		temp = Utils.toLH(type);
		System.arraycopy(temp, 0, buf, 132, temp.length);
		// recon
		temp = Utils.toLH(recon);
		System.arraycopy(temp, 0, buf, 136, temp.length);
	}

	// byte数组转化为类对象
	public static LoginBackInfo getLogin_Back_Info(byte[] buf) {
		String username = "";
		String pharmacist = "";
		String title = "";
		boolean yesno = false;
		int type = 0;
		int recon = 0;

		try {
			// username
			byte[] tempStr15 = new byte[15];
			System.arraycopy(buf, 0, tempStr15, 0, 15);
			username = new String(tempStr15, "GBK");

			// pharmacist
			System.arraycopy(buf, 15, tempStr15, 0, 15);
			pharmacist = new String(tempStr15, "GBK");

			// title
			byte[] tempStr100 = new byte[100];
			System.arraycopy(buf, 30, tempStr100, 0, 100);
			pharmacist = new String(tempStr100, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// yesno
		byte yesnoByte = buf[130];
		yesno = (yesnoByte == 0x00) ? false : true;

		byte[] temp = new byte[4];
		// type
		System.arraycopy(buf, 132, temp, 0, 4);
		type = Utils.vtolh(temp);

		// recon
		System.arraycopy(buf, 136, temp, 0, 4);
		recon = Utils.vtolh(temp);

		return new LoginBackInfo(username, pharmacist, title, yesno, type,
				recon);
	}

	// 返回要发送的byte数组
	public byte[] getBuf() {
		return buf;
	}
}
