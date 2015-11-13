package com.joyce.reps.serverInterface;

//REQ_FILE_INFO	 ---请求文件信息
public class ReqFileInfo {
	byte[] username = new byte[20];
	boolean send_recv; // 判断username为接接收者还是发送者 true为发送者
	int type; // 文件类型
}
