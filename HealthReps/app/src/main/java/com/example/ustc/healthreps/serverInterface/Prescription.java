package com.example.ustc.healthreps.serverInterface;

//ChuFang  ---处方内容
public class Prescription {
	byte[] patient = new byte[12]; // 病人姓名
	byte[] sex = new byte[5];
	byte[] age = new byte[5];
	byte[] ID = new byte[5];
	byte[] feibie = new byte[10];
	byte[] address = new byte[100];
	byte[] content = new byte[800];
	byte[] shop = new byte[12];
	byte[] doctor = new byte[12];
	byte[] pharmacist = new byte[12];
	byte[] phone = new byte[15];
	byte[] selfreport = new byte[50]; // 患者自述
	byte[] filename = new byte[100];
	byte[] date = new byte[10];

	byte[] title = new byte[100];
	byte[] bianhao = new byte[100];
}
