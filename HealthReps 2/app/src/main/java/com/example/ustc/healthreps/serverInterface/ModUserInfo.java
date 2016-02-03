package com.example.ustc.healthreps.serverInterface;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import com.example.ustc.healthreps.utils.Utils;

//ModInfo  ---修改个人信息
public class ModUserInfo {
	public byte[] sex = new byte[5];
	public byte[] age = new byte[5];
	public byte[] zhicheng = new byte[20]; // 职称
	public byte[] keshi = new byte[100]; // 科室
	public int cw; // 中西药 true:中药，false:西药
	public byte[] address = new byte[100];
	public byte[] shopName = new byte[100];
	public byte[] doc_pha_address = new byte[100]; // 医生药剂师所属单位
	public int vip;
	public int off; // false：不能接受离线文件
	public byte[] phone = new byte[20]; // 电话
	public byte[] pharmacist = new byte[12]; //关联的药剂师名称
	public byte[] dianzhang = new byte[15];	//店长
	public byte[] caozuoyuan = new byte[15];	//操作员

	public static int SIZE = 5+5+20+100+2+4+100+100+100+4+4+20+12+15+15+2;

	//ModUserInfo->byte[]
	public byte[] getModUserInfoBytes(){
		byte[] buf = new byte[SIZE];
		//sex
		System.arraycopy(sex,0,buf,0,sex.length);

		//age
		System.arraycopy(age,0,buf,5,age.length);

		//zhicheng
		System.arraycopy(zhicheng,0,buf,10,zhicheng.length);

		//keshi
		System.arraycopy(keshi,0,buf,30,keshi.length);

		//cw
		byte[] temp_int = new byte[4];
		temp_int = Utils.toLH(cw);
		System.arraycopy(temp_int,0,buf,132,temp_int.length);

		//address
		System.arraycopy(address,0,buf,136,address.length);

		//shopName
		System.arraycopy(shopName,0,buf,236,shopName.length);

		//doc_pha_address
		System.arraycopy(doc_pha_address,0,buf,336,doc_pha_address.length);

		//vip
		temp_int = Utils.toLH(vip);
		System.arraycopy(temp_int,0,buf,436,temp_int.length);

		//off
		temp_int = Utils.toLH(off);
		System.arraycopy(temp_int,0,buf,440,temp_int.length);

		//phone
		System.arraycopy(phone,0,buf,444,phone.length);

		//pharmacist
		System.arraycopy(pharmacist,0,buf,464,pharmacist.length);

		//dianzhang
		System.arraycopy(dianzhang,0,buf,476,dianzhang.length);

		//caozuoyuan
		System.arraycopy(caozuoyuan,0,buf,491,caozuoyuan.length);

		return buf;
	}

	//byte[]->ModUserInfo
	public static ModUserInfo getModUserInfo(byte[] buf){
		ModUserInfo p = new ModUserInfo();
		byte[] temp = null;
		byte[] temp_int = new byte[4];

		//sex
		System.arraycopy(buf,0,temp,0,5);
		p.sex = temp;

		//age
		System.arraycopy(buf,5,temp,0,5);
		p.age = temp;

		//zhicheng
		System.arraycopy(buf,10,temp,0,20);
		p.zhicheng = temp;

		//keshi
		System.arraycopy(buf,30,temp,0,100);
		p.keshi = temp;

		//cw
		System.arraycopy(buf,132,temp_int,0,4);
		p.cw = Utils.vtolh(temp_int);

		//address
		System.arraycopy(buf,136,temp,0,100);
		p.address = temp;

		//shopName
		System.arraycopy(buf,236,temp,0,100);
		p.shopName = temp;

		//doc_pha_address
		System.arraycopy(buf,336,temp,0,100);
		p.doc_pha_address = temp;

		//vip
		System.arraycopy(buf,436,temp_int,0,4);
		p.vip = Utils.vtolh(temp_int);

		//off
		System.arraycopy(buf,440,temp_int,0,4);
		p.off = Utils.vtolh(temp_int);

		//phone
		System.arraycopy(buf,444,temp,0,20);
		p.phone = temp;

		//pharmacist
		System.arraycopy(buf,464,temp,0,12);
		p.pharmacist = temp;

		//dianzhang
		System.arraycopy(buf,476,temp,0,15);
		p.dianzhang = temp;

		//caozuoyuan
		System.arraycopy(buf,491,temp,0,15);
		p.caozuoyuan = temp;

		return p;
	}
}