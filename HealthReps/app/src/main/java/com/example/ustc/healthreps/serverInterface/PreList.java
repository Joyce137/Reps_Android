package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//PreList  ---预购清单文件
public class PreList {
	public byte[] patient = new byte[12]; // 病人姓名
	public byte[] sex = new byte[5];
	public byte[] age = new byte[5];
	public byte[] ID = new byte[25];
	public byte[] feibie = new byte[10];
	public byte[] address = new byte[100];
	public byte[] content = new byte[800];
	public byte[] shop = new byte[12];
	public byte[] doctor = new byte[12];
	public byte[] pharmacist = new byte[12];
	public byte[] phone = new byte[15];
	public byte[] selfreport = new byte[50]; // 患者自述
	public byte[] filename = new byte[100];
	public byte[] date = new byte[10];

	public int creatorType;	//创造者类型
	//char creatorName[15];
	public byte[] creatorName = new byte[15];
	public int status;		//清单状态

	public static int size = 12 + 5 + 5 + 25 + 10 + 100 + 800 + 12 + 12 + 12
			+ 15 + 50 + 100 + 10 + 4 + 15 + 1 + 4;

	// 将FileInfo对象转化为byte数组
	public byte[] getPreListBytes() {
		byte[] buf = new byte[size];
		// patient
		System.arraycopy(patient, 0, buf, 0, patient.length);
		// sex
		System.arraycopy(sex, 0, buf, 12, sex.length);
		// age
		System.arraycopy(age, 0, buf, 17, age.length);
		// ID
		System.arraycopy(ID, 0, buf, 22, ID.length);
		// feibie
		System.arraycopy(feibie, 0, buf, 47, feibie.length);
		// address
		System.arraycopy(address, 0, buf, 57, address.length);
		// content
		System.arraycopy(content, 0, buf, 157, content.length);
		// shop
		System.arraycopy(shop, 0, buf, 957, shop.length);
		// doctor
		System.arraycopy(doctor, 0, buf, 969, doctor.length);
		// pharmacist
		System.arraycopy(pharmacist, 0, buf, 981, pharmacist.length);
		// phone
		System.arraycopy(phone, 0, buf, 993, phone.length);
		// selfreport
		System.arraycopy(selfreport, 0, buf, 1008, selfreport.length);
		// filename
		System.arraycopy(filename, 0, buf, 1058, filename.length);
		// date
		System.arraycopy(date, 0, buf, 1158, date.length);
		//creatorType
		byte[] temp4 = new byte[4];
		temp4 = Utils.toLH(creatorType);
		System.arraycopy(temp4,0,buf,1168,temp4.length);

		//creatorName
		System.arraycopy(creatorName,0,buf,1172,creatorName.length);

		//status
		temp4 = Utils.toLH(status);
		System.arraycopy(temp4,0,buf,1188,temp4.length);
		return buf;
	}

	// 将byte数组转化为类对象
	public static PreList getPreList(byte[] buf) {
		PreList p = new PreList();
		byte[] temp = null;
		// patient
		System.arraycopy(buf, 0, temp, 0, 12);
		p.patient = temp;
		// sex
		System.arraycopy(buf, 12, temp, 0, 5);
		p.sex = temp;
		// age
		System.arraycopy(buf, 17, temp, 0, 5);
		p.age = temp;
		// ID
		System.arraycopy(buf, 22, temp, 0, 25);
		p.ID = temp;
		// feibie
		System.arraycopy(buf, 47, temp, 0, 10);
		p.feibie = temp;
		// address
		System.arraycopy(buf, 57, temp, 0, 100);
		p.address = temp;
		// content
		System.arraycopy(buf, 157, temp, 0, 800);
		p.content = temp;
		// shop
		System.arraycopy(buf, 957, temp, 0, 12);
		p.shop = temp;
		// doctor
		System.arraycopy(buf, 969, temp, 0, 12);
		p.doctor = temp;
		// pharmacist
		System.arraycopy(buf, 981, temp, 0, 12);
		p.pharmacist = temp;
		// phone
		System.arraycopy(buf, 993, temp, 0, 15);
		p.phone = temp;
		// selfreport
		System.arraycopy(buf, 1008, temp, 0, 50);
		p.selfreport = temp;
		// filename
		System.arraycopy(buf, 1058, temp, 0, 100);
		p.filename = temp;
		// date
		System.arraycopy(buf, 1158, temp, 0, 10);
		p.date = temp;

		//creatorType
		byte[] temp4 = new byte[4];
		System.arraycopy(buf,1168,temp4,0,4);
		p.creatorType = Utils.vtolh(temp4);

		//creatorName
		System.arraycopy(buf,1172,temp,0,15);
		p.creatorName = temp;

		//status
		System.arraycopy(buf,1188,temp4,0,4);
		p.status = Utils.vtolh(temp4);

		return p;
	}
}
