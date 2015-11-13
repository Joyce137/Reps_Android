package com.joyce.reps.serverInterface;

//HeartBeat
public class HeartBeat {
	byte[] username = new byte[15]; // char[15]
	// byte ack; //确认字符ACK

	public HeartBeat() {
		for (int i = 0; i < username.length; i++) {
			username[i] = 0;
		}
	}

	public static int size = 15;
	byte[] buf = new byte[size];

	public HeartBeat(byte[] username) {
		this.username = username;

		// username
		System.arraycopy(username, 0, buf, 0, username.length);
	}

	public static HeartBeat getHeartBeatInfo(byte buf[]) {
		byte[] tempStr15 = new byte[15];
		System.arraycopy(buf, 0, tempStr15, 0, 15);
		return new HeartBeat(tempStr15);
	}

	// 返回要发送的byte数组
	public byte[] getBuf() {
		return buf;
	}
};
