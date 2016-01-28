package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.database.entity.User;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

//MSG_USER_INFO	 ---用户信息
public class UserInfo {
	public byte[] sex = new byte[5];
	public byte[] age = new byte[5];
	public int status; // 登陆状态 0 离线；1 在线；3 忙碌
	public byte[] zhicheng = new byte[20]; // 职称
	public byte[] realName = new byte[25]; // 真实姓名
	public byte[] loginName = new byte[25];// 登录名 必须唯一
	public byte[] keshi = new byte[50]; // 科室
	public int type; // 用户类型 1.医生，2.药监局，3.患者，4.药剂师
	public int cw; // 中西药 true:中药，false:西药
	public byte[] address = new byte[100];
	public byte[] shopName = new byte[100];
	public byte[] doc_pha_address = new byte[100]; // 医生药剂师所属单位
	public int vip;
	public int off; // false：不能接受离线文件
	public byte[] phone = new byte[20]; // 电话
	public byte[] defaultStore = new byte[12]; // 关联的药店名
	public byte[] password = new byte[15];
	public byte[] dianzhang = new byte[15];
	public byte[] caozuoyuan = new byte[15];
	public byte[] ID_Num = new byte[20];
	public byte[] yibao_Num = new byte[20];
	public byte[] pastDiseaseHistory = new byte[200];
	public String imagePath = "";
	public String email;

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
		System.arraycopy(defaultStore, 0, buf, 472, defaultStore.length);
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
		// sex
		System.arraycopy(buf, 0, p.sex, 0, 5);

		// age
		System.arraycopy(buf, 5, p.age, 0, 5);

		// status
		byte[] temp_int = new byte[4];
		System.arraycopy(buf, 12, temp_int, 0, 4);
		p.status = Utils.vtolh(temp_int);

		byte temp20[] = new byte[20];
		// zhicheng
		System.arraycopy(buf, 16, p.zhicheng, 0, 20);

		// realName
		System.arraycopy(buf, 36, p.realName, 0, 25);
		// loginName
		System.arraycopy(buf, 61, p.loginName, 0, 25);

		// keshi
		System.arraycopy(buf, 86, p.keshi, 0, 50);

		// type
		temp_int = new byte[4];
		System.arraycopy(buf, 136, temp_int, 0, 4);
		p.type = Utils.vtolh(temp_int);
		// cw
		temp_int = new byte[4];
		System.arraycopy(buf, 140, temp_int, 0, 4);
		p.cw = Utils.vtolh(temp_int);

		// address
		System.arraycopy(buf, 144, p.address, 0, 100);

		// shopName
		System.arraycopy(buf, 244, p.shopName, 0, 100);

		// doc_pha_address
		System.arraycopy(buf, 344, p.doc_pha_address, 0, 100);

		// vip
		temp_int = new byte[4];
		System.arraycopy(buf, 444, temp_int, 0, 4);
		p.vip = Utils.vtolh(temp_int);
		// off
		temp_int = new byte[4];
		System.arraycopy(buf, 448, temp_int, 0, 4);
		p.off = Utils.vtolh(temp_int);
		// phone
		System.arraycopy(buf, 452, p.phone, 0, 20);

		// pharmacist
		System.arraycopy(buf, 472, p.defaultStore, 0, 12);


		// password
		System.arraycopy(buf, 484, p.password, 0, 15);

		// dianzhang
		System.arraycopy(buf, 499, p.dianzhang, 0, 15);

 		// caozuoyuan
		System.arraycopy(buf, 514, p.caozuoyuan, 0, 15);

		// ID_Num
		System.arraycopy(buf, 529, p.ID_Num, 0, 20);

		// yibao_Num
		System.arraycopy(buf, 549, p.yibao_Num, 0, 20);

		// pastDiseaseHistory
		System.arraycopy(buf, 569, p.pastDiseaseHistory, 0, 200);

		return p;
	}

	//将UserInfo转化为User
	public User changeUserInfoToUser(){
		User user = new User();
		try {
			user.username = new String(loginName,"GBK");
			user.sex = new String(sex,"GBK");
			user.age = new String(age,"GBK");
			switch (status){
				case 0:
					user.status = "离线";
					break;
				case 1:
					user.status = "在线";
					break;
				case 3:
					user.status = "忙碌";
					break;
				default:
					user.status = "未知";
					break;
			}
			user.zhicheng = new String(zhicheng,"GBK");
			user.realname = new String(realName,"GBK");
			user.keshi = new String(keshi,"GBK");
			user.type = Utils.changeTypeToString(type);
			switch (cw){
				case 0:
					user.cw = "中药";
					break;
				case 1:
					user.cw = "西药";
					break;
				default:
					user.cw = "未知";
					break;
			}
			user.address = new String(address,"GBK");
			user.shopname = new String(shopName,"GBK");
			user.docpha_address = new String(doc_pha_address,"GBK");
			switch (vip){
				case 0:
					user.vip = "是";
					break;
				case 1:
					user.vip = "否";
					break;
				default:
					user.vip = "未知";
					break;
			}
			switch (off){
				case 0:
					user.off = "不接收离线文件";
					break;
				case 1:
					user.off = "接收离线文件";
					break;
				default:
					user.off = "未知";
					break;
			}
			user.phone = new String(phone,"GBK");
			user.defaultstore = new String(defaultStore,"GBK");
			user.password = new String(password,"GBK");
			user.dianzhang = new String(dianzhang,"GBK");
			user.caozuoyuan = new String(caozuoyuan,"GBK");
			user.ID_num = new String(ID_Num,"GBK");
			user.yibao_num = new String(yibao_Num,"GBK");
			user.selfintroduction = new String(pastDiseaseHistory,"GBK");
			user.imagepath = imagePath;
			user.email = email;
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return user;
	}
}
