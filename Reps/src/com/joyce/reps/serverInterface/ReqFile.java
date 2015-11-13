package com.joyce.reps.serverInterface;

//ReqFile 清单  资质图片  处方 等等    
public class ReqFile {
	public static final int size = 0;
	int type; // 2 资质图片， 1 指纹 ， 3 章 ，4 处方 盖医生章的 5 盖药剂师章和医生章的 6 预购清单
	byte[] filename = new byte[100];
	byte[] username = new byte[12];

	public byte[] getReqFileBytes() {
		return null;
	}

	public ReqFile getReqFile(byte[] data) {
		return null;
	}
}