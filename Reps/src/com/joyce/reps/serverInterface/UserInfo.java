package com.joyce.reps.serverInterface;

import com.joyce.reps.utils.Utils;

//MSG_USER_INFO	 ---用户信息
public class UserInfo {
	byte[] sex = new byte[5];
	byte[] age = new byte[5];
	int status; // 登陆状态 0 离线；1 在线；3 忙碌
	byte[] zhicheng = new byte[20]; // 职称
	byte[] realName = new byte[25]; // 真实姓名
	byte[] loginName = new byte[25];// 登录名 必须唯一
	byte[] keshi = new byte[50]; // 科室
	int type; // 用户类型 1.医生，2.药监局，3.患者，4.药剂师
	int cw; // 中西药 true:中药，false:西药
	byte[] address = new byte[100];
	byte[] shopName = new byte[100];
	byte[] doc_pha_address = new byte[100]; // 医生药剂师所属单位
	int vip;
	int off; // false：不能接受离线文件
	byte[] phone = new byte[20]; // 电话
	byte[] pharmacist = new byte[12]; // 关联的药剂师名称
	byte[] password = new byte[15];
	byte[] dianzhang = new byte[15];
	byte[] caozuoyuan = new byte[15];
	byte[] ID_Num = new byte[20];
	byte[] yibao_Num = new byte[20];
	byte[] pastDiseaseHistory = new byte[200];

	public static int size = 5 + 5 + 2 + 4 + 20 + 25 + 25 + 50 + 4 + 4 + 100
			+ 100 + 100 + 4 + 4 + 20 + 12 + 15 + 15 + 15 + 20 + 20 + 200 + 3;

	// 将MSG_USER_INFO对象转化为byte数组
	public byte[] getMSG_USER_INFOBytes() {
		byte[] buf = new byte[size];
		// sex
		System.arraycopy(sex, 0, buf, 0, sex.length);
		// age
		System.arraycopy(age, 0, buf, 5, age.length);
		// status
		byte temp[] = Utils.toLH(status);
		System.arraycopy(temp, 0, buf, 12, temp.length);
		// zhicheng
		System.arraycopy(zhicheng, 0, buf, 16, zhicheng.length);
		// realName
		System.arraycopy(realName, 0, buf, 36, realName.length);
		// loginName
		System.arraycopy(loginName, 0, buf, 61, loginName.length);
		// keshi
		System.arraycopy(keshi, 0, buf, 86, keshi.length);
		// type
		temp = Utils.toLH(type);
		System.arraycopy(temp, 0, buf, 136, temp.length);
		// cw
		temp = Utils.toLH(cw);
		System.arraycopy(temp, 0, buf, 140, temp.length);
		// address
		System.arraycopy(address, 0, buf, 144, address.length);
		// shopName
		System.arraycopy(shopName, 0, buf, 244, shopName.length);
		// doc_pha_address
		System.arraycopy(doc_pha_address, 0, buf, 344, doc_pha_address.length);
		// vip
		temp = Utils.toLH(vip);
		System.arraycopy(temp, 0, buf, 444, temp.length);
		// off
		temp = Utils.toLH(off);
		System.arraycopy(temp, 0, buf, 448, temp.length);
		// phone
		System.arraycopy(phone, 0, buf, 452, phone.length);
		// pharmacist
		System.arraycopy(pharmacist, 0, buf, 472, pharmacist.length);
		// password
		System.arraycopy(password, 0, buf, 484, password.length);
		// dianzhang
		System.arraycopy(dianzhang, 0, buf, 499, dianzhang.length);
		// caozuoyuan
		System.arraycopy(caozuoyuan, 0, buf, 514, caozuoyuan.length);
		// ID_Num
		System.arraycopy(ID_Num, 0, buf, 529, ID_Num.length);
		// yibao_Num
		System.arraycopy(yibao_Num, 0, buf, 549, yibao_Num.length);
		// pastDiseaseHistory
		System.arraycopy(pastDiseaseHistory, 0, buf, 569,
				pastDiseaseHistory.length);

		return buf;
	}

	// 将byte数组转化为类对象
	public static UserInfo getMSG_USER_INFO(byte[] buf) {
		UserInfo p = new UserInfo();
		byte[] temp = null;
		// sex
		System.arraycopy(buf, 0, temp, 0, 5);
		p.sex = temp;
		// age
		System.arraycopy(buf, 5, temp, 0, 5);
		p.age = temp;
		// status
		byte[] temp_int = new byte[4];
		System.arraycopy(buf, 12, temp_int, 0, 4);
		p.status = Utils.vtolh(temp_int);
		// zhicheng
		System.arraycopy(buf, 16, temp, 0, 20);
		p.zhicheng = temp;
		// realName
		System.arraycopy(buf, 36, temp, 0, 25);
		p.realName = temp;
		// loginName
		System.arraycopy(buf, 61, temp, 0, 25);
		p.loginName = temp;
		// keshi
		System.arraycopy(buf, 86, temp, 0, 50);
		p.keshi = temp;
		// type
		temp_int = new byte[4];
		System.arraycopy(buf, 136, temp_int, 0, 4);
		p.type = Utils.vtolh(temp_int);
		// cw
		temp_int = new byte[4];
		System.arraycopy(buf, 140, temp_int, 0, 4);
		p.cw = Utils.vtolh(temp_int);
		// address
		System.arraycopy(buf, 144, temp, 0, 100);
		p.address = temp;
		// shopName
		System.arraycopy(buf, 244, temp, 0, 100);
		p.shopName = temp;
		// doc_pha_address
		System.arraycopy(buf, 344, temp, 0, 100);
		p.doc_pha_address = temp;
		// vip
		temp_int = new byte[4];
		System.arraycopy(buf, 444, temp_int, 0, 4);
		p.vip = Utils.vtolh(temp_int);
		// off
		temp_int = new byte[4];
		System.arraycopy(buf, 448, temp_int, 0, 4);
		p.off = Utils.vtolh(temp_int);
		// phone
		System.arraycopy(buf, 452, temp, 0, 20);
		p.phone = temp;
		// pharmacist
		System.arraycopy(buf, 472, temp, 0, 12);
		p.pharmacist = temp;
		// password
		System.arraycopy(buf, 484, temp, 0, 15);
		p.password = temp;
		// dianzhang
		System.arraycopy(buf, 499, temp, 0, 15);
		p.dianzhang = temp;
		// caozuoyuan
		System.arraycopy(buf, 514, temp, 0, 15);
		p.caozuoyuan = temp;
		// ID_Num
		System.arraycopy(buf, 529, temp, 0, 20);
		p.ID_Num = temp;
		// yibao_Num
		System.arraycopy(buf, 549, temp, 0, 20);
		p.yibao_Num = temp;
		// pastDiseaseHistory
		System.arraycopy(buf, 569, temp, 0, 200);
		p.pastDiseaseHistory = temp;

		return p;
	}
}
