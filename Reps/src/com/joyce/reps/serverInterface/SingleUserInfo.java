package com.joyce.reps.serverInterface;

//SingleUserInfo-----接收时的包
public class SingleUserInfo {
	int type;
	byte[] phone = new byte[20];
	byte[] address = new byte[100];

	byte[] shopName = new byte[100];
	// CHAR dianzhang[15]; //店长
	// CHAR chaozuoyuan[15];

	byte[] realname = new byte[12];
	byte[] zhicheng = new byte[20];
	byte[] Doc_Pha_add = new byte[100];
}
