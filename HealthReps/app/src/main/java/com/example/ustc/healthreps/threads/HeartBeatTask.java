package com.example.ustc.healthreps.threads;

import java.util.TimerTask;

import android.util.Log;

import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.socket.TCPSocket;
import com.example.ustc.healthreps.serverInterface.Types;

//心跳包任务
public class HeartBeatTask extends TimerTask{
	//String jobName = "HeartBeatTask";

	public static int mBeatTimes = 0;
	
	@Override
	public void run() {
		Log.e("HeartBeatTask", "run()");
		HeartBeatTimeProc();
	}
	
	//心跳包执行动作
	public void HeartBeatTimeProc(){
		Sockets.socket_center.sendHeartBeat();
		//synchronized (lock) {
			mBeatTimes++;
			if(mBeatTimes <= 2){
					//m_connect_alert.setBackgroundColor(color.green);
				System.out.println(mBeatTimes+"-----color.green");
				if(mBeatTimes == 2){
					Sockets.socket_center.sendHeartBeat();
				}
			}
			else if(mBeatTimes == 3 || mBeatTimes == 4){
				//m_connect_alert.setBackgroundColor(color.yellow);
				System.out.println(mBeatTimes+"-----color.yellow");
				Sockets.socket_center.sendHeartBeat();
			}
			else if(mBeatTimes >= 5){
				Users.sOnline = false;
				//m_connect_alert.setBackgroundColor(color.red);
				System.out.println(mBeatTimes + "-----color.red");
//				Sockets.socket_center.shutSocket();

		        if (Sockets.socket_center.initSocket())
		        {
					Sockets.socket_center.reLogin(Users.sLoginUsername);
		        }
			}
		//}
	}
}