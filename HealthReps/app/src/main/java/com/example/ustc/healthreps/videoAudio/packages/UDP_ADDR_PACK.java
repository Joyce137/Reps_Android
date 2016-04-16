package com.example.ustc.healthreps.videoAudio.packages;

import com.example.ustc.healthreps.videoAudio.utils.Utils;

public class UDP_ADDR_PACK {
	public final static int ADDR_FLAG=8986;
	public final static int MAXPACKSIZE=530;
	static int size=552;

	public int flag;//转发的flag
	public byte[] addr=new byte[16];//mysockaddr
	public int len;
	public byte[] name=new byte[MAXPACKSIZE];//包内数据，真正的包数据

	public UDP_ADDR_PACK(){
		flag=ADDR_FLAG;
		len=0;
		for(int i=0;i<530;i++){
			name[i]=0;
		}
	}
	public byte[] tobyte(){
		byte[] byte_sum=new byte[552];
		byte[] s_byte=new byte[2];
		byte[] a_byte=new byte[16];
		byte[] i_byte=new byte[4];
		s_byte= Utils.shortToLH(flag);
		i_byte= Utils.toLH(len);
		a_byte=addr;//.tobyte()

		System.arraycopy(s_byte, 0, byte_sum, 0, 2);
		System.arraycopy(a_byte, 0, byte_sum, 2, 16);
		System.arraycopy(i_byte, 0, byte_sum, 18, 4);
		System.arraycopy(name, 0, byte_sum, 22, 530);
		return byte_sum;
	}
	public static UDP_ADDR_PACK topack(byte[] byte_sum){
		UDP_ADDR_PACK pack=new UDP_ADDR_PACK();
		byte[] s_byte=new byte[2];
		byte[] a_byte=new byte[16];
		byte[] i_byte=new byte[4];

		System.arraycopy(byte_sum, 0, s_byte, 0, 2);
		System.arraycopy(byte_sum, 2, a_byte, 0, 16);
		System.arraycopy(byte_sum, 18, i_byte, 0, 4);
		pack.flag=Utils.vtolh(s_byte);
		pack.len=Utils.vtolh(i_byte);
		pack.addr=a_byte;//mysockaddr.topack(a_byte)
		System.arraycopy(byte_sum, 22, pack.name, 0, 530);

		return pack;
	}
}
