package com.example.ustc.healthreps.serverInterface;

//Req_MSG_USER_INFO	---刷新状态的时候显示的用户信息
public class RefreshUserInfo {
	byte[] sex = new byte[3];
	int status; // 登陆状态 0 离线；1 在线；3 忙碌
	byte[] zhicheng = new byte[20]; // 职称
	byte[] realName = new byte[25]; // 真实姓名
	byte[] loginName = new byte[25];// 登录名 必须唯一
}
