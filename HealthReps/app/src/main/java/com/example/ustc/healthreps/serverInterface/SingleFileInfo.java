package com.example.ustc.healthreps.serverInterface;

//MSG_FILE_INFO_SINGLE  ---单个文件信息
public class SingleFileInfo {
	int type; // 文件类型
	boolean flag; // false表示对方未接受，true表示已接受
	byte[] filename = new byte[100];
}
