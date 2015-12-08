package com.example.ustc.healthreps.threads;

import java.util.TimerTask;

import android.util.Log;

import com.example.ustc.healthreps.BaseActivity;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.socket.TCPSocket;
import com.example.ustc.healthreps.serverInterface.Types;

//心跳包任务
public class HeartBeatTask extends TimerTask{
	//String jobName = "HeartBeatTask"; 
	int m_beatTimes = BaseActivity.mBeatTimes;
	TCPSocket mSocket = Sockets.socket_center;
	
	@Override
	public void run() {
		Log.e("HeartBeatTask", "run()");
		HeartBeatTimeProc();
	}
	
	//心跳包执行动作
	public void HeartBeatTimeProc(){
		mSocket.sendHeartBeat();
		//synchronized (lock) {
			m_beatTimes++;
			if(m_beatTimes <= 2){
					//m_connect_alert.setBackgroundColor(color.green);
				System.out.println(m_beatTimes+"-----color.green");
				if(m_beatTimes == 2){
					mSocket.sendHeartBeat();
				}
			}
			else if(m_beatTimes == 3 || m_beatTimes == 4){
				//m_connect_alert.setBackgroundColor(color.yellow);
				System.out.println(m_beatTimes+"-----color.yellow");
				mSocket.sendHeartBeat();
			}
			else if(m_beatTimes >= 5){
				//m_connect_alert.setBackgroundColor(color.red);
				System.out.println(m_beatTimes+"-----color.red");
				mSocket.shutSocket();
		        if (mSocket.initSocket(Types.center_Port, Types.version_IP))
		        {
		        	mSocket.reLogin(BaseActivity.mLoginName);
		        }
			}
		//}
	}
}