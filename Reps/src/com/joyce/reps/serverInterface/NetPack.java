package com.joyce.reps.serverInterface;

import android.util.Log;

import com.joyce.reps.utils.Utils;

//struct NET_PACK - 网络包
public class NetPack {
	private int m_Start; // 包头标志
	private int m_Crc;
	private int nDataLen;
	private int m_nFlag;
	private byte[] m_buffer;

	public byte[] getM_buffer() {
		return m_buffer;
	}

	public void setM_buffer(byte[] m_buffer) {
		this.m_buffer = m_buffer;
	}

	public NetPack() {
		Reset();
	}

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

	public void Reset() {
		m_Start = Types.PACK_START_FLAG;
		m_Crc = -1;
		nDataLen = 0;
		m_nFlag = 0;
	}

	// 除了数据以外的信息size
	public static int infoSize = 2 + 2 + 2 + 2;
	// 该pack总size
	public int size = infoSize;
	private byte buf[];

	// 构造并转化
	public NetPack(int m_Crc, int nDataLen, int m_nFlag, byte[] m_buffer) {
		this.m_Start = Types.PACK_START_FLAG;
		this.m_Crc = m_Crc;
		this.nDataLen = nDataLen;
		this.m_nFlag = m_nFlag;
		this.m_buffer = m_buffer;

		int truesize = infoSize + m_buffer.length;
		buf = new byte[truesize];

		// m_Start(2)
		byte[] temp = Utils.shortToLH(m_Start);
		System.arraycopy(temp, 0, buf, 0, temp.length);

		// m_Crc(2)
		temp = Utils.shortToLH(m_Crc);
		System.arraycopy(temp, 0, buf, 2, temp.length);

		// nDataLen(2)
		temp = Utils.shortToLH(nDataLen);
		System.arraycopy(temp, 0, buf, 4, temp.length);

		// m_nFlag(2)
		temp = Utils.shortToLH(m_nFlag);
		System.arraycopy(temp, 0, buf, 6, temp.length);

		// m_buffer
		System.arraycopy(m_buffer, 0, buf, 8, m_buffer.length);
	}

	// byte数组转化为类对象
	public static NetPack getNET_PACKInfo(byte[] buf) {
		int m_Start = Types.PACK_START_FLAG;
		int m_Crc = -1;
		int nDataLen = 0;
		int m_nFlag = 0;
		byte[] m_buffer;

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

		// m_buffer
		int len = buf.length - 8;
		m_buffer = new byte[len];
		System.arraycopy(buf, 8, m_buffer, 0, len);

		return new NetPack(m_Crc, nDataLen, m_nFlag, m_buffer);
	}

	// 返回要发送的byte数组
	public byte[] getBuf() {
		return buf;
	}

	// 按byte加在一起，模65536，返回一个0-65535的数据设置成m_Crc
	public void CalCRC() {
		int sum = 0;
		for (int i = 0; i < nDataLen; i++) {
			sum += m_buffer[i];
		}
		this.m_Crc = sum % 65536;
		byte[] temp = Utils.shortToLH(m_Crc);
		Log.e("CalCRC", "temp" + temp.length);
		System.arraycopy(temp, 0, this.buf, 2, temp.length);
	}

	public int VerifyCRC() {
		int sum = 0;
		Log.e("VerifyCRC", "nDataLen" + nDataLen);
		for (int i = 0; i < nDataLen; i++) {
			sum += m_buffer[i];
		}
		return sum % 65536;
	}
}