package com.example.ustc.healthreps.videoAudio.thread_video;

public class newclass 
{
	
	public int n;
	public byte[] lock_buf;
	public int[] flag;
	
	public newclass(){
		n=0;
		lock_buf=new byte[52600*5];
		flag=new int[500];
		for(int i=0;i<500;i++){
			flag[i]=0;
		}
	}
}


