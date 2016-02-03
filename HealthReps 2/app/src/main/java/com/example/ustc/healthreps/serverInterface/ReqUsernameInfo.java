package com.example.ustc.healthreps.serverInterface;

//Req_Info_Username
public class ReqUsernameInfo {
	public byte[] username = new byte[12];

	public static int SIZE = 12;

	public byte[] getReqUsernameInfoBytes() {
		byte[] buf = new byte[SIZE];

		//username
		System.arraycopy(username, 0, buf, 0, username.length);

		return buf;
	}

	public ReqUserInfo getReqUsernameInfo(byte[] buf) {
		ReqUserInfo p = new ReqUserInfo();
		byte[] temp = null;

		//username
		System.arraycopy(buf, 0, temp, 0, 12);
		p.username = temp;

		return p;
	}
}