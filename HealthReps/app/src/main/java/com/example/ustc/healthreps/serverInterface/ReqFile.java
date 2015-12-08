package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//ReqFile 清单  资质图片  处方 等等
public class ReqFile {
	public int type; // 2 资质图片， 1 指纹 ， 3 章 ，4 处方 盖医生章的 5 盖药剂师章和医生章的 6 预购清单
	public byte[] filename = new byte[100];
	public byte[] username = new byte[12];

	public static int SIZE = 4+100+12;

	public byte[] getReqFileBytes() {
		byte[] buf = new byte[SIZE];
		byte[] temp_int = null;

		//type
		temp_int = Utils.toLH(type);
		System.arraycopy(temp_int, 0, buf, 0, temp_int.length);
		//filename
		System.arraycopy(filename, 0, buf, 4, filename.length);
		//username
		System.arraycopy(username, 0, buf, 104, username.length);
		return buf;
	}

	public ReqFile getReqFile(byte[] buf) {
		ReqFile p = new ReqFile();
		byte[] temp = null;
		byte[] temp_int = new byte[4];

		//type
		System.arraycopy(buf, 0, temp_int, 0, 4);
		p.type = Utils.vtolh(temp_int);
		//filename
		System.arraycopy(buf, 4, temp, 0, 100);
		p.filename = temp;
		//username
		System.arraycopy(buf, 104, temp, 0, 12);
		p.username = temp;
		return null;
	}
}