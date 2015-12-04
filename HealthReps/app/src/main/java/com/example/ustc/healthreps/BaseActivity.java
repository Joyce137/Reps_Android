package com.example.ustc.healthreps;

import java.util.Timer;

import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.HeartBeatTask;
import com.example.ustc.healthreps.threads.ReceiveThread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

//用于实现心跳包，需要心跳包的类都得继承这个类
public class BaseActivity extends Activity{
	public static int mBeatTimes = 0;
	public static String mLoginName = LoginActivity.mLoginUsername;
	public static Handler sAlertHandler;
	private ReceiveThread mReceiveThread = AllThreads.sReceiveThread;
	private Timer mHeatBeatTimer = AllThreads.sHeatBeatTimer;
	private HeartBeatTask mHeartBeatTask = AllThreads.sHeartBeatTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//启动接收线程
		if (mReceiveThread == null) {
			mReceiveThread = new ReceiveThread();
			mReceiveThread.start();
		}
		
		//启动心跳包线程
		if(mHeatBeatTimer == null){
			mHeatBeatTimer = new Timer();
		}
		if(mHeartBeatTask == null){
			mHeartBeatTask = new HeartBeatTask();
		}
		mHeatBeatTimer.schedule(mHeartBeatTask, 1000, 5000);
		
		//接收消息
		sAlertHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String alert = (String) msg.obj;
				Toast.makeText(getApplicationContext(), alert,
						Toast.LENGTH_SHORT).show();
			}
		};
	}
}
