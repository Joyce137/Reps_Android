package com.example.ustc.healthreps.videoAudio.packages;


import com.example.ustc.healthreps.videoAudio.utils.Utils;

public class UDP_LOGIN_PACK {
	public final static int LOGIN_FLAG=8988;
	public final static int MAXPACKSIZE=530;
	static int size=536;

	public int flag;//转发的flag
	public int len;
	public byte[] name=new byte[MAXPACKSIZE];//包内数据，真正的包数据

	public UDP_LOGIN_PACK(){
		flag=LOGIN_FLAG;
		len=0;
		for(int i=0;i<530;i++){
			name[i]=0;
		}
	}
	public byte[] tobyte(){
		byte[] byte_sum=new byte[536];
		byte[] s_byte=new byte[2];
		byte[] i_byte=new byte[4];
		s_byte= Utils.shortToLH(flag);
		i_byte=Utils.toLH(len);

		System.arraycopy(s_byte, 0, byte_sum, 0, 2);
		System.arraycopy(i_byte, 0, byte_sum, 2, 4);
		System.arraycopy(name, 0, byte_sum, 6, 530);
		return byte_sum;
	}
	public static UDP_LOGIN_PACK topack(byte[] byte_sum){
		UDP_LOGIN_PACK pack=new UDP_LOGIN_PACK();
		byte[] s_byte=new byte[2];
		byte[] i_byte=new byte[4];

		System.arraycopy(byte_sum, 0, s_byte, 0, 2);
		System.arraycopy(byte_sum, 2, i_byte, 0, 4);
		pack.flag=Utils.vtolh(s_byte);
		pack.len=Utils.vtolh(i_byte);
		System.arraycopy(byte_sum, 6, pack.name, 0, 530);

		return pack;
	}
}
