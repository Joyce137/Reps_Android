package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//MSG_FILE_INFO_SINGLE  ---单个文件信息
public class SingleFileInfo {
	int type; // 文件类型
	boolean flag; // false表示对方未接受，true表示已接受
	byte[] filename = new byte[100];

	public static int SIZE = 4+1+3+100;

	public byte[] getSingleFileInfoBytes() {
		byte[] buf = new byte[SIZE];
		byte[] temp_int = null;

		//type
		temp_int = Utils.toLH(type);
		System.arraycopy(temp_int, 0, buf, 0, temp_int.length);
		//flag
		byte yesnobyte = (byte) (flag == true ? 0x01 : 0x00);
		buf[4] = yesnobyte;
		//filename
		System.arraycopy(filename, 0, buf, 8, filename.length);

		return buf;
	}

	public SingleFileInfo getSingleFileInfo(byte[] buf) {
		SingleFileInfo p = new SingleFileInfo();
		byte[] temp = null;
		byte[] temp_int = new byte[4];

		//type
		System.arraycopy(buf, 0, temp_int, 0, 4);
		p.type = Utils.vtolh(temp_int);
		//flag
		byte yesnoByte = buf[4];
		p.flag = (yesnoByte == 0x00) ? false : true;
		//filename
		System.arraycopy(buf, 8, temp, 0, 100);
		p.filename = temp;

		return p;
	}
}
