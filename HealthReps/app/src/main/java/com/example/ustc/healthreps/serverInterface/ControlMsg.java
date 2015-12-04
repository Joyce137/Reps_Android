package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

//struct Control_Msg  //一些确认信息
public class ControlMsg {
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
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

	private String filename; // char[100]
	private String username; // char[15]:请求链接的用户:当type为true是请求，type为false的时候是返回消息
								// yesno是同意否
	private int flag; // WORD Flag:什么类型的确定消息
	private boolean yesno; // 成功还是失败
	private int type; // 错误类型

	// 1.登陆的时候 type = 1 密码和账号错误；2 已经在线; 3 使用错了客户端; 4 审核未过
	// 2.注册的时候 type = 1 用户名已经存在；2 相关图片传送失败
	// 3.链接的时候 type = 0 代表返回消息; 1 代表请求消息; 2 代码服务器同意两者链接

	public ControlMsg() {
		username = "";
		username = "";
		flag = 0;
		yesno = false;
		type = 0;
	}

	public static int size = 100 + 16 + 2 + 2 + 4;
	private byte[] buf = new byte[size];

	// 构造并转化
	public ControlMsg(String filename, String username, int flag,
			boolean yesno, int type) {
		this.filename = filename;
		this.username = username;
		this.flag = flag;
		this.yesno = yesno;
		this.type = type;

		byte temp[];
		try {
			// filename
			temp = filename.getBytes("GBK");
			System.arraycopy(temp, 0, buf, 0, temp.length);
			// username
			temp = username.getBytes("GBK");
			System.arraycopy(temp, 0, buf, 100, temp.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// flag
		temp = Utils.shortToLH(flag);
		System.arraycopy(temp, 0, buf, 116, temp.length);

		// yesno
		byte yesnobyte = (byte) (yesno == true ? 0x01 : 0x00);
		buf[118] = yesnobyte;

		// type
		temp = Utils.toLH(type);
		System.arraycopy(temp, 0, buf, 120, temp.length);
	}

	// byte数组转化为类对象
	public static ControlMsg getControl_MsgInfo(byte[] buf) {
		String filename = "";
		String username = "";
		int flag = 0;
		boolean yesno = false;
		int type = 0;

		try {
			// filename
			byte[] tempStr100 = new byte[100];
			System.arraycopy(buf, 0, tempStr100, 0, 100);
			filename = new String(tempStr100, "GBK");

			// username
			byte[] tempStr15 = new byte[15];
			System.arraycopy(buf, 100, tempStr15, 0, 15);
			username = new String(tempStr15, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] temp = new byte[4];
		// flag
		System.arraycopy(buf, 116, temp, 0, 2);
		flag = Utils.vtolh(temp);

		// yesno
		byte yesnoByte = buf[118];
		yesno = (yesnoByte == 0x00) ? false : true;

		// type
		System.arraycopy(buf, 120, temp, 0, 4);
		type = Utils.vtolh(temp);

		return new ControlMsg(filename, username, flag, yesno, type);
	}

	// 返回要发送的byte数组
	public byte[] getBuf() {
		return buf;
	}
};
