package com.example.ustc.healthreps.serverInterface;

//ReqUserInfo
public class ReqUserInfo {
	int type;
	byte[] keshi = new byte[100];
	byte[] username = new byte[12];
	boolean doc_pha; // 医生还是药剂师
}