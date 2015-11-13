package com.joyce.reps.serverInterface;

import com.joyce.reps.utils.Utils;

//FileInfo   ---文件包信息
public class FileInfo {
	byte[] filename = new byte[100];
	byte[] shop = new byte[12]; // 药店
	byte[] doctor = new byte[12]; // 医生
	byte[] pharmacist = new byte[12]; // 药剂师
	int type; // 文件类型 1.指纹，2.资质图片，3.章，4.盖医生章的处方，5.盖药剂师和医生章的处方
	int id; // 文件包id
	int flag; // 是否是文件尾包
	int start; // 文件转换为char数组好，每个报的数据从数组的那个位置开始写入
	int len; // 文件的总字符数
	int idnum; // 文件总ID数
	int content_len;
	byte[] content = new byte[Types.FILE_MAX_BAG];
	byte[] random_str = new byte[20];

	int size = 100 + 12 + 12 + 12 + 4 + 4 + 4 + 4 + 4 + 4 + 4
			+ Types.FILE_MAX_BAG + 20;

	public FileInfo() {
		content_len = Types.FILE_MAX_BAG;
	}

	// 将FileInfo对象转化为byte数组
	public byte[] getFileInfoBytes() {
		byte[] buf = new byte[size];

		byte[] temp2 = new byte[2];
		byte[] temp4 = new byte[4];

		// filename
		System.arraycopy(filename, 0, buf, 0, filename.length);
		// shop
		System.arraycopy(shop, 0, buf, 100, shop.length);
		// doctor
		System.arraycopy(doctor, 0, buf, 112, doctor.length);
		// pharmacist
		System.arraycopy(pharmacist, 0, buf, 124, pharmacist.length);
		// type(2)
		temp2 = Utils.shortToLH(type);
		System.arraycopy(temp2, 0, buf, 136, temp2.length);
		// id
		temp4 = Utils.toLH(id);
		System.arraycopy(temp4, 0, buf, 140, temp4.length);
		// flag(2)
		temp2 = Utils.shortToLH(flag);
		System.arraycopy(temp2, 0, buf, 144, temp2.length);
		// start
		temp4 = Utils.toLH(start);
		System.arraycopy(temp4, 0, buf, 148, temp4.length);
		// len
		temp4 = Utils.toLH(len);
		System.arraycopy(temp4, 0, buf, 152, temp4.length);
		// idnum
		temp4 = Utils.toLH(idnum);
		System.arraycopy(temp4, 0, buf, 156, temp4.length);
		// content_len(2)
		temp2 = Utils.shortToLH(content_len);
		System.arraycopy(temp2, 0, buf, 160, temp2.length);
		// content
		System.arraycopy(content, 0, buf, 162, Types.FILE_MAX_BAG);
		// random_str
		System.arraycopy(random_str, 0, buf, 162 + Types.FILE_MAX_BAG,
				random_str.length);
		return buf;
	}

	// 将byte数组转化为FileInfo类对象
	public static FileInfo getFileInfo(byte[] buf) {
		FileInfo f = new FileInfo();
		byte[] filename = new byte[100];
		byte[] shop = new byte[12]; // 药店
		byte[] doctor = new byte[12]; // 医生
		byte[] pharmacist = new byte[12]; // 药剂师
		short type; // 文件类型 1.指纹，2.资质图片，3.章，4.盖医生章的处方，5.盖药剂师和医生章的处方
		int id; // 文件包id
		short flag; // 是否是文件尾包
		int start; // 文件转换为char数组好，每个报的数据从数组的那个位置开始写入
		int len; // 文件的总字符数
		int idnum; // 文件总ID数
		short content_len;
		byte[] content = new byte[Types.FILE_MAX_BAG];
		byte[] random_str = new byte[20];

		// filename
		System.arraycopy(buf, 0, filename, 0, 100);
		f.filename = filename;
		// shop
		System.arraycopy(buf, 100, shop, 0, 12);
		f.shop = shop;
		// doctor
		System.arraycopy(buf, 112, doctor, 0, 12);
		f.doctor = doctor;
		// pharmacist
		System.arraycopy(buf, 124, pharmacist, 0, 12);
		f.pharmacist = pharmacist;

		byte[] temp2 = new byte[2];
		byte[] temp4 = new byte[4];
		// type
		System.arraycopy(buf, 136, temp2, 0, 2);
		type = (short) Utils.vtolh(temp2);
		f.type = type;
		// id
		System.arraycopy(buf, 138, temp4, 0, 4);
		id = Utils.vtolh(temp4);
		f.id = id;
		// flag
		System.arraycopy(buf, 142, temp2, 0, 2);
		flag = (short) Utils.vtolh(temp2);
		f.flag = flag;
		// start
		System.arraycopy(buf, 144, temp4, 0, 4);
		start = Utils.vtolh(temp4);
		f.start = start;
		// len
		System.arraycopy(buf, 148, temp4, 0, 4);
		len = Utils.vtolh(temp4);
		f.len = len;
		// idnum
		System.arraycopy(buf, 152, temp4, 0, 4);
		idnum = Utils.vtolh(temp4);
		f.idnum = idnum;
		// content_len
		System.arraycopy(buf, 156, temp2, 0, 2);
		content_len = (short) Utils.vtolh(temp2);
		f.content_len = content_len;
		// content
		System.arraycopy(buf, 158, content, 0, Types.FILE_MAX_BAG);
		f.content = content;
		// random_str
		System.arraycopy(buf, 158 + Types.FILE_MAX_BAG, random_str, 0, 20);
		f.random_str = random_str;

		return f;
	}
};
