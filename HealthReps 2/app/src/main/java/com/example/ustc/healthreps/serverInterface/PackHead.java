package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

public class PackHead {
	public int getM_Start() {
		return m_Start;
	}

	public void setM_Start(int m_Start) {
		this.m_Start = m_Start;
	}

	public int getM_Crc() {
		return m_Crc;
	}

	public void setM_Crc(int m_Crc) {
		this.m_Crc = m_Crc;
	}

	public int getnDataLen() {
		return nDataLen;
	}

	public void setnDataLen(int nDataLen) {
		this.nDataLen = nDataLen;
	}

	public int getM_nFlag() {
		return m_nFlag;
	}

	public void setM_nFlag(int m_nFlag) {
		this.m_nFlag = m_nFlag;
	}

	private int m_Start; // 包头标志
	private int m_Crc;
	private int nDataLen;
	private int m_nFlag;

	public PackHead() {
		m_Start = Types.PACK_START_FLAG;
		m_Crc = -1;
		m_nFlag = 0;
		nDataLen = 0;
	}

	public static int size = 2 + 2 + 2 + 2;
	private byte buf[] = new byte[size];

	// 构造并转化
	public PackHead(int m_Start, int m_Crc, int nDataLen, int m_nFlag) {
		this.m_Start = m_Start;
		this.m_Crc = m_Crc;
		this.nDataLen = nDataLen;
		this.m_nFlag = m_nFlag;

		// m_Start(2)
		byte[] temp = Utils.shortToLH(m_Start);
		System.arraycopy(temp, 0, buf, 0, temp.length);

		// m_Start(2)
		temp = Utils.shortToLH(m_Crc);
		System.arraycopy(temp, 0, buf, 2, temp.length);

		// nDataLen(2)
		temp = Utils.shortToLH(nDataLen);
		System.arraycopy(temp, 0, buf, 4, temp.length);

		// m_nFlag(2)
		temp = Utils.shortToLH(m_nFlag);
		System.arraycopy(temp, 0, buf, 6, temp.length);
	}

	// ͨbyte数组还原为对象
	public static PackHead getPackHeadInfo(byte[] buf) {
		int m_Start = Types.PACK_START_FLAG;
		int m_Crc = -1;
		int nDataLen = 0;
		int m_nFlag = 0;

		byte[] temp = new byte[4];
		// m_Start
		System.arraycopy(buf, 0, temp, 0, 2);
		m_Start = Utils.vtolh(temp);

		// m_Crc
		System.arraycopy(buf, 2, temp, 0, 2);
		m_Crc = Utils.vtolh(temp);

		// nDatalen
		System.arraycopy(buf, 4, temp, 0, 2);
		nDataLen = Utils.vtolh(temp);

		// m_nFlag
		System.arraycopy(buf, 6, temp, 0, 2);
		m_nFlag = Utils.vtolh(temp);

		return new PackHead(m_Start, m_Crc, nDataLen, m_nFlag);
	}

	// 返回要发送的byte数组
	public byte[] getBuf() {
		return buf;
	}
}
