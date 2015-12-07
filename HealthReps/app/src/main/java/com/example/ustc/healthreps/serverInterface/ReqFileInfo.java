package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//REQ_FILE_INFO	 ---请求文件信息
public class ReqFileInfo {
	byte[] username = new byte[20];
	boolean send_recv; // 判断username为接接收者还是发送者 true为发送者
	int type; // 文件类型

	public static int SIZE = 20+1+3+4;

	public byte[] getReqFileInfoBytes() {
		byte[] buf = new byte[SIZE];
		byte[] temp_int = null;

		//username
		System.arraycopy(username, 0, buf, 0, username.length);
		//send_recv
		byte yesnobyte = (byte) (send_recv == true ? 0x01 : 0x00);
		buf[20] = yesnobyte;
		//type
		temp_int = Utils.toLH(type);
		System.arraycopy(temp_int, 0, buf, 24, temp_int.length);

		return buf;
	}

	public ReqFileInfo getReqFileInfo(byte[] buf) {
		ReqFileInfo p = new ReqFileInfo();
		byte[] temp = null;
		byte[] temp_int = new byte[4];

		//username
		System.arraycopy(buf, 0, temp, 0, 20);
		p.username = temp;
		//send_recv
		byte yesnoByte = buf[20];
		p.send_recv = (yesnoByte == 0x00) ? false : true;

		//type
		System.arraycopy(buf, 24, temp_int, 0, 4);
		p.type = Utils.vtolh(temp_int);

		return p;
	}
}
