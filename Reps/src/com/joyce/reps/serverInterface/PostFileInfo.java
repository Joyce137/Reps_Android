package com.joyce.reps.serverInterface;

import com.joyce.reps.utils.Utils;

//PostFileInfo  ---post基本文件信息到发送文件线程
public class PostFileInfo {
	public byte[] filename = new byte[100];
	public byte[] filepath = new byte[150];
	public int type;

	public static int size = 100 + 150 + 4;
	byte[] buf = new byte[size];

	// 构造函数
	public PostFileInfo(byte[] filename, byte[] filepath, int type) {
		this.filename = filename;
		this.filepath = filepath;
		this.type = type;

		// filename
		System.arraycopy(filename, 0, buf, 0, filename.length);
		// filepath
		System.arraycopy(filepath, 0, buf, 100, filepath.length);
		// type
		byte temp[] = Utils.toLH(type);
		System.arraycopy(temp, 0, buf, 250, temp.length);
	}

	// 将byte数组转化为类对象
	public static PostFileInfo getPostThread(byte[] buf) {
		byte[] filename = null, filepath = null;
		int type;

		// filename
		System.arraycopy(buf, 0, filename, 0, 100);
		// filepath
		System.arraycopy(buf, 100, filepath, 0, 150);
		// type
		byte[] temp_int = new byte[4];
		System.arraycopy(buf, 250, temp_int, 0, 4);
		type = Utils.vtolh(temp_int);

		return new PostFileInfo(filename, filepath, type);
	}

	public byte[] getBuf() {
		return buf;
	}
}