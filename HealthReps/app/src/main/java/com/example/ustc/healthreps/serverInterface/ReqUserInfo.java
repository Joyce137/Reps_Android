package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//ReqUserInfo
public class ReqUserInfo {
	int type;
	byte[] keshi = new byte[100];
	byte[] username = new byte[12];
	boolean doc_pha; // 医生还是药剂师

	public static int SIZE = 4+100+12+1+3;

	public byte[] getReqUserInfoBytes() {
		byte[] buf = new byte[SIZE];
		byte[] temp_int = null;

		//type
		temp_int = Utils.toLH(type);
		System.arraycopy(temp_int, 0, buf, 0, temp_int.length);
		//keshi
		System.arraycopy(keshi, 0, buf, 4, keshi.length);
		//username
		System.arraycopy(username, 0, buf, 104, username.length);
		//doc_pha
		byte yesnobyte = (byte) (doc_pha == true ? 0x01 : 0x00);
		buf[116] = yesnobyte;

		return buf;
	}

	public ReqUserInfo getReqUserInfo(byte[] buf) {
		ReqUserInfo p = new ReqUserInfo();
		byte[] temp = null;
		byte[] temp_int = new byte[4];

		//type
		System.arraycopy(buf, 0, temp_int, 0, 4);
		p.type = Utils.vtolh(temp_int);
		//keshi
		System.arraycopy(buf, 4, temp, 0, 100);
		p.keshi = temp;
		//username
		System.arraycopy(buf, 104, temp, 0, 12);
		p.username = temp;
		//doc_pha
		byte yesnoByte = buf[116];
		p.doc_pha = (yesnoByte == 0x00) ? false : true;

		return p;
	}
}