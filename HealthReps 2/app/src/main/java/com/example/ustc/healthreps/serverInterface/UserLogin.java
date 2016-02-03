package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

//struct UserLogin
public class UserLogin {
	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRecon() {
		return recon;
	}

	public void setRecon(int recon) {
		this.recon = recon;
	}

	private String username; // char[20];
	private byte[] key = new byte[20]; // char[20];
	private int type;
	private int recon;

	public UserLogin() {
		username = "";
		for (int i = 0; i < key.length; i++) {
			key[i] = 0;
		}
		type = -1;
		recon = 0;
	}

	public static int size = 20 + 20 + 4 + 4;
	private byte[] buf = new byte[size];

	// 构造并转化
	public UserLogin(String username, byte[] key, int type, int recon) {
		this.username = username;
		this.key = key;
		this.type = type;
		this.recon = recon;

		byte temp[];
		try {
			// username
			temp = username.getBytes("GBK");
			System.arraycopy(temp, 0, buf, 0, temp.length);
			// key
			temp = key;
			System.arraycopy(temp, 0, buf, 20, temp.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// type
		temp = Utils.toLH(type);
		System.arraycopy(temp, 0, buf, 40, temp.length);

		// recon
		temp = Utils.toLH(recon);
		System.arraycopy(temp, 0, buf, 44, temp.length);
	}

	// byte数组转化为类对象
	public static UserLogin getUserLoginInfo(byte[] buf) {
		String username = "";
		byte key[] = new byte[20];
		int type = 0;
		int recon = 0;

		try {
			// username
			byte[] tempStr20 = new byte[20];
			System.arraycopy(buf, 0, tempStr20, 0, 20);
			username = new String(tempStr20, "GBK");

			// key
			System.arraycopy(buf, 20, key, 0, 20);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// type
		byte[] temp = new byte[4];
		System.arraycopy(buf, 40, temp, 0, 4);
		type = Utils.vtolh(temp);

		// recon
		System.arraycopy(buf, 44, temp, 0, 4);
		recon = Utils.vtolh(temp);

		return new UserLogin(username, key, type, recon);
	}

	// 返回要发送的byte数组
	public byte[] getBuf() {
		return buf;
	}
};
