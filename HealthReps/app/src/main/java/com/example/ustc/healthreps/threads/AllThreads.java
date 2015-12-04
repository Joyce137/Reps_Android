package com.example.ustc.healthreps.threads;

import java.util.Timer;

public class AllThreads {
	public static ReceiveThread sReceiveThread;
	public static SendFileThread sSendFileThread;
	//心跳包定时器
	public static Timer sHeatBeatTimer;
	public static HeartBeatTask sHeartBeatTask;
}
