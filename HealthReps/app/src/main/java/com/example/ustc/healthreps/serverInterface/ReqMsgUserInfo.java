package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//Req_MSG_USER_INFO	---刷新状态的时候显示的用户信息
public class ReqMsgUserInfo {
	public byte[] sex = new byte[3];
	public int status; // 登陆状态 0 离线；1 在线；3 忙碌
	public byte[] zhicheng = new byte[20]; // 职称
	public byte[] realName = new byte[25]; // 真实姓名
	public byte[] loginName = new byte[25];// 登录名 必须唯一

	public static int SIZE = 3+1+4+20+25+25+2;

	//ReqMsgUserInfo->byte[]
	public byte[] getReqMsgUserInfoBytes() {
		byte[] buf = new byte[SIZE];
		byte[] temp_int = null;

		//sex
		System.arraycopy(sex, 0, buf, 0, sex.length);
		//status
		temp_int = Utils.toLH(status);
		System.arraycopy(temp_int, 0, buf, 4, temp_int.length);
		//zhicheng
		System.arraycopy(zhicheng, 0, buf, 8, zhicheng.length);
		//realName
		System.arraycopy(realName, 0, buf, 28, realName.length);
		//loginName
		System.arraycopy(loginName, 0, buf, 53, loginName.length);

		return buf;
	}

	//byte[]->ReqMsgUserInfo
	public static ReqMsgUserInfo getReqMsgUserInfo(byte[] buf){
		ReqMsgUserInfo p = new ReqMsgUserInfo();
		byte[] temp = null;
		byte[] temp_int = new byte[4];

		//sex
		System.arraycopy(buf, 0, temp, 0, 3);
		p.sex = temp;
		//status
		System.arraycopy(buf, 4, temp_int, 0, 4);
		p.status = Utils.vtolh(temp_int);
		//zhicheng
		System.arraycopy(buf, 8, temp, 0, 20);
		p.zhicheng = temp;
		//realName
		System.arraycopy(buf, 28, temp, 0, 25);
		p.realName = temp;
		//loginName
		System.arraycopy(buf, 53, temp, 0, 25);
		p.loginName = temp;

		return p;
	}
}
