package com.example.ustc.healthreps.videoAudio.threads;

public class newclassrev {

	public int n;
	public byte[] lock_buf_rev;
	public int[] flag_rev;

	public newclassrev() {
		n = 0;
		lock_buf_rev = new byte[52600*5];
		flag_rev = new int[500];
		for (int i = 0; i < 500; i++) {
			flag_rev[i] = 0;
		}
	}

}
