package com.example.ustc.healthreps.videoAudio.thread_video;

public class MyUtils {

	/**
	 * int转byte数组
	 *
	 * @param a
	 * @return
	 */
	public static byte[] int2Byte(int a) {
		byte[] b = new byte[4];
		b[0] = (byte) (a >> 24);
		b[1] = (byte) (a >> 16);
		b[2] = (byte) (a >> 8);
		b[3] = (byte) (a);
		return b;
	}

	/**
	 * 将short转成byte[2]
	 *
	 * @param a
	 * @return
	 */
	public static byte[] short2Byte(short a) {
		byte[] b = new byte[2];
		b[0] = (byte) (a >> 8);
		b[1] = (byte) (a);
		return b;
	}

	/**
	 * 将byte[2]转换成short
	 * @param b
	 * @return
	 */
	public static short byte2Short(byte[] b){
		return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
	}

	/**
	 * byte数组转int
	 *
	 * @param b
	 * @return
	 */
	public static int byte2Int(byte[] b) {
		return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
	}
}
