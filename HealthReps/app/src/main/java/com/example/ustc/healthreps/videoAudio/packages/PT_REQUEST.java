package com.example.ustc.healthreps.videoAudio.packages;


import com.example.ustc.healthreps.videoAudio.utils.Utils;

public class PT_REQUEST {
	public final static int USERNAME_SIZE=10;
	public int type;
	public byte[] ip=new byte[16];
	public int port;
	public byte[] username=new byte[USERNAME_SIZE];//包内数据，真正的包数据

	public PT_REQUEST(){
		type=0;
		port=0;
		for(int i=0;i<16;i++){
			ip[i]=0;
		}
	}
	public byte[] tobyte(){
		byte[] byte_sum=new byte[30];
		byte[] t_byte=new byte[2];
		byte[] p_byte=new byte[2];
		t_byte= Utils.shortToLH(type);
		p_byte=Utils.shortToLH(port);

		System.arraycopy(t_byte, 0, byte_sum, 0, 2);
		System.arraycopy(ip, 0, byte_sum, 2, 16);
		System.arraycopy(p_byte, 0, byte_sum, 18, 2);
		System.arraycopy(username, 0, byte_sum, 20, 10);
		return byte_sum;
	}
	public static PT_REQUEST topack(byte[] byte_sum){
		PT_REQUEST pack=new PT_REQUEST();
		byte[] t_byte=new byte[2];
		byte[] p_byte=new byte[2];

		System.arraycopy(byte_sum, 0, t_byte, 0, 2);
		pack.type=Utils.vtolh(t_byte);
		System.arraycopy(byte_sum, 2, pack.ip, 0, 16);
		System.arraycopy(byte_sum, 18, p_byte, 0, 2);
		pack.type=Utils.vtolh(p_byte);
		System.arraycopy(byte_sum, 20, pack.username, 0, 10);

		return pack;
	}
}
