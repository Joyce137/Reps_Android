package com.example.ustc.healthreps.serverInterface;

//Mod_PASSWORD  ---修改密码
public class ModPassword {
	byte[] name = new byte[30];
	byte[] oldPwd = new byte[30];
	byte[] newPwd = new byte[30];

	public static int SIZE = 30+30+30;

	//转化为byte数组
	public byte[] getModPasswordBytes(){
		byte[] buf = new byte[SIZE];
		//name
		System.arraycopy(name,0,buf,0,name.length);
		//oldPwd
		System.arraycopy(oldPwd,0,buf,30,oldPwd.length);
		//newPwd
		System.arraycopy(newPwd,0,buf,60,newPwd.length);

		return buf;
	}

	//byte数组转化为类对象
	public static ModPassword getModPassword(byte[] buf){
		ModPassword p = new ModPassword();
		byte[] temp = null;
		//name
		System.arraycopy(buf,0,temp,0,30);
		p.name = temp;

		//oldPwd
		System.arraycopy(buf,30,temp,0,30);
		p.oldPwd = temp;

		//newPwd
		System.arraycopy(buf,60,temp,0,30);
		p.newPwd = temp;

		return p;
	}
}