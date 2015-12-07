package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//ReqSingleUserInfo-----请求时发的包
public class ReqSingleUserInfo {
	int type;
	byte[] username = new byte[12];
	boolean isPicExist;

	public static int SIZE = 4+12+1+3;

	public byte[] getReqSingleUserInfoBytes() {
		byte[] buf = new byte[SIZE];
		byte[] temp_int = null;

		//type
		temp_int = Utils.toLH(type);
		System.arraycopy(temp_int, 0, buf, 0, temp_int.length);
		//username
		System.arraycopy(username, 0, buf, 4, username.length);
		//isPicExist
		byte yesnobyte = (byte) (isPicExist == true ? 0x01 : 0x00);
		buf[16] = yesnobyte;

		return buf;
	}

	public ReqSingleUserInfo getReqSingleUserInfo(byte[] buf) {
		ReqSingleUserInfo p = new ReqSingleUserInfo();
		byte[] temp = null;
		byte[] temp_int = new byte[4];

		//type
		System.arraycopy(buf, 0, temp_int, 0, 4);
		p.type = Utils.vtolh(temp_int);
		//username
		System.arraycopy(buf, 4, temp, 0, 12);
		p.username = temp;
		//isPicExist
		byte yesnoByte = buf[16];
		p.isPicExist = (yesnoByte == 0x00) ? false : true;

		return p;
	}
}
