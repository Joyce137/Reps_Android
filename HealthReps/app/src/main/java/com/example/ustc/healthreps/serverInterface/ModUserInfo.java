package com.example.ustc.healthreps.serverInterface;

//ModInfo  ---修改个人信息
public class ModUserInfo {
	byte[] sex = new byte[5];
	byte[] age = new byte[5];
	byte[] zhicheng = new byte[20]; // 职称
	byte[] keshi = new byte[100]; // 科室
	int cw; // 中西药 true:中药，false:西药
	byte[] address = new byte[100];
	byte[] shopName = new byte[100];
	byte[] doc_pha_address = new byte[100]; // 医生药剂师所属单位
	int vip;
	int off; // false：不能接受离线文件
	byte[] phone = new byte[20]; // 电话
	// byte[] pharmacist = new byte[12]; //关联的药剂师名称
	// CHAR dianzhang[15]; //店长
	// CHAR chaozuoyuan[15];
}