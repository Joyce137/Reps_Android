package com.example.ustc.healthreps.videoAudio.link_package;

import com.example.ustc.healthreps.videoAudio.androiddecoder_log.Utils;

public class HANDSHAKE_MESSAGE {
	public final static int HANDSHAKE=1004;		//客户端发送请求，请求服务器让对方联系自己
	public final static int USERNAME_SIZE=10;
	static int size=12;

	public int type;
	public byte[] username=new byte[USERNAME_SIZE];//包内数据，真正的包数据

	public HANDSHAKE_MESSAGE(){
		type=HANDSHAKE;
		for(int i=0;i<10;i++){
			username[i]=0;
		}
	}
	public byte[] tobyte(){
		byte[] byte_sum=new byte[12];
		byte[] t_byte=new byte[2];
		t_byte= Utils.shortToLH(type);

		System.arraycopy(t_byte, 0, byte_sum, 0, 2);
		System.arraycopy(username, 0, byte_sum, 2, 10);
		return byte_sum;
	}
	public static HANDSHAKE_MESSAGE topack(byte[] byte_sum){
		HANDSHAKE_MESSAGE pack=new HANDSHAKE_MESSAGE();
		byte[] t_byte=new byte[2];

		System.arraycopy(byte_sum, 0, t_byte, 0, 2);
		pack.type=Utils.vtolh(t_byte);
		System.arraycopy(byte_sum, 2, pack.username, 0, 10);

		return pack;
	}
}
