package com.example.ustc.healthreps.serverInterface;

//ReqPha
public class ReqPha {
	byte[] phaName = new byte[12];
	byte[] realName = new byte[20];

	public static int SIZE = 12+20;

	//ReqPha->byte[]
	public byte[] getReqPhaBytes() {
		byte[] buf = new byte[SIZE];

		//phaName
		System.arraycopy(phaName, 0, buf, 0, phaName.length);
		//realName
		System.arraycopy(realName, 0, buf, 12, realName.length);

		return buf;
	}

	//byte[]->ReqPha
	public static ReqPha getReqPha(byte[] buf) {
		ReqPha p = new ReqPha();
		byte[] temp = null;

		//phaName
		System.arraycopy(buf, 0, temp, 0, 12);
		p.phaName = temp;
		//realName
		System.arraycopy(buf, 12, temp, 0, 20);
		p.realName = temp;

		return p;
	}
}
