package com.example.ustc.healthreps.serverInterface;

//P2PInfo  ---聊天消息
public class P2PInfo {
	public byte[] username = new byte[12];
	public byte[] toUsername = new byte[12];
	public byte[] info = new byte[200];

	public static int SIZE = 12+12+200;
	//P2PInfo->byte[]
	public byte[] getP2PInfoByte(){
		byte[] buf = new byte[SIZE];

		//username
		System.arraycopy(username,0,buf,0,username.length);

		//toUsername
		System.arraycopy(toUsername,0,buf,12,toUsername.length);

		//info
		System.arraycopy(info,0,buf,24,info.length);

		return buf;
	}

	//byte[]->P2PInfo
	public static P2PInfo getP2PInfo(byte[] buf){
		P2PInfo p = new P2PInfo();

		//username
		System.arraycopy(buf,0,p.username,0,12);

		//toUsername
		System.arraycopy(buf,12,p.toUsername,0,12);

		//info
		System.arraycopy(buf,24,p.info,0,200);

		return p;
	}
}
