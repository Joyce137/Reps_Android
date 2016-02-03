package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//SingleUserInfo-----接收时的包
public class SingleUserInfo {
	public int type;
	public byte[] phone = new byte[20];
	public byte[] address = new byte[100];
	public byte[] shopName = new byte[100];
	public byte[] dianzhang = new byte[15];
	public byte[] caozuoyuan = new byte[15];
	public byte[] realname = new byte[12];
	public byte[] zhicheng = new byte[20];
	public byte[] Doc_Pha_add = new byte[100];

	public static int SIZE = 4+20+100+100+15+15+12+20+100+2;

	public byte[] getSingleUserInfoBytes() {
		byte[] buf = new byte[SIZE];
		byte[] temp_int = null;

		//type
		temp_int = Utils.toLH(type);
		System.arraycopy(temp_int, 0, buf, 0, temp_int.length);
		//phone
		System.arraycopy(phone, 0, buf, 4, phone.length);
		//address
		System.arraycopy(address, 0, buf, 24, address.length);
		//shopName
		System.arraycopy(shopName, 0, buf, 124, shopName.length);
		//dianzhang
		System.arraycopy(dianzhang, 0, buf, 224, dianzhang.length);
		//caozuoyuan
		System.arraycopy(caozuoyuan, 0, buf, 239, caozuoyuan.length);
		//realname
		System.arraycopy(realname, 0, buf, 254, realname.length);
		//zhicheng
		System.arraycopy(zhicheng, 0, buf, 266, zhicheng.length);
		//Doc_Pha_add
		System.arraycopy(Doc_Pha_add, 0, buf, 286, Doc_Pha_add.length);

		return buf;
	}

	public static SingleUserInfo getSingleUserInfo(byte[] buf) {
		SingleUserInfo p = new SingleUserInfo();
		byte[] temp = null;
		byte[] temp_int = new byte[4];

		//type
		System.arraycopy(buf, 0, temp_int, 0, 4);
		p.type = Utils.vtolh(temp_int);
		//phone
		System.arraycopy(buf, 4, temp, 0, 20);
		p.phone = temp;
		//address
		System.arraycopy(buf, 24, temp, 0, 100);
		p.address = temp;
		//shopName
		System.arraycopy(buf, 124, temp, 0, 100);
		p.shopName = temp;
		//dianzhang
		System.arraycopy(buf, 224, temp, 0, 15);
		p.dianzhang = temp;
		//caozuoyuan
		System.arraycopy(buf, 239, temp, 0, 15);
		p.caozuoyuan = temp;
		//realname
		System.arraycopy(buf, 254, temp, 0, 12);
		p.realname = temp;
		//zhicheng
		System.arraycopy(buf, 266, temp, 0, 20);
		p.zhicheng = temp;
		//Doc_Pha_add
		System.arraycopy(buf, 286, temp, 0, 100);
		p.Doc_Pha_add = temp;

		return p;
	}
}
