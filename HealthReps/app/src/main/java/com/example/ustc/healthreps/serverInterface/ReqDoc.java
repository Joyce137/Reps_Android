package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

//ReqDoc
public class ReqDoc {
	byte[] docName = new byte[12];
	byte[] realName = new byte[20];

	public static int SIZE = 12+20;

	//ReqDoc->byte[]
	public byte[] getReqDocBytes() {
		byte[] buf = new byte[SIZE];

		//docName
		System.arraycopy(docName, 0, buf, 0, docName.length);
		//realName
		System.arraycopy(realName, 0, buf, 12, realName.length);

		return buf;
	}

	//byte[]->ReqDoc
	public static ReqDoc getReqDoc(byte[] buf) {
		ReqDoc p = new ReqDoc();
		byte[] temp = null;

		//docName
		System.arraycopy(buf, 0, temp, 0, 12);
		p.docName = temp;
		//realName
		System.arraycopy(buf, 12, temp, 0, 20);
		p.realName = temp;

		return p;
	}
}
