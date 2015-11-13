package com.joyce.reps.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.joyce.reps.serverInterface.ErrorNo;
import com.joyce.reps.serverInterface.NetPack;
import com.joyce.reps.serverInterface.Types;
import com.joyce.reps.serverInterface.UserLogin;

import android.util.Log;

public class TCPSocket {
	public Socket mSocket;
	public String mUsername;

	// 初始化Socket
	// InitSocket
	public boolean initSocket(int DefaultPort, String DefaultIP) {
		Log.e("InitSocket", "0");
		InetAddress addr = null;
		try {
			Log.e("InitSocket", "1");
			addr = InetAddress.getByName(DefaultIP);
			mSocket = new Socket(addr, DefaultPort);

			return true;
		} catch (UnknownHostException e) {
			Log.e("InitSocket", "2");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.e("InitSocket", "3");
			e.printStackTrace();
			return false;
		}
	}

	public void initSocket() {
		if (!initSocket(Types.center_Port, Types.version_IP))
			if (!initSocket(Types.center_Port, Types.version_IP))
				if (!initSocket(Types.center_Port, Types.version_IP)) {
					System.out.println("网络故障，请稍后重试");
					return;
				}
		Log.e("initSocket", "initSocket成功！");
	}

	// 发送数据包sendPack
	public boolean sendPack(NetPack data) {
		int flag = data.getM_Start();
		// 循环发送
		while (true) {
			int retVal;
			retVal = sendMsg(mSocket, data.getBuf(), data.size, flag);
			if (retVal != 1) {
				Log.e("SendPack", "SendPack: " + retVal);
				return false;
			}
			Log.e("SendPack", "Yes__SendPack: " + retVal);
			return true;
		}
	}

	// 发送消息
	private int sendMsg(Socket socket, byte[] data, int len, int flag) {
		int resultNumber = 1;
		if (socket == null) {
			Log.e("SendMsg", "SendMsg: +ErrorNo.SOCKET_NULL");
			return ErrorNo.SOCKET_NULL;
		}
		if (data == null) {
			Log.e("SendMsg", "SendMsg: +ErrorNo.DATA_NULL");
			return ErrorNo.DATA_NULL;
		}
		if (len < 0) {
			Log.e("SendMsg", "SendMsg: +ErrorNo.MINUSVALUE");
			return ErrorNo.MINUSVALUE;
		}

		int available = 0;
		try {
			if ((available = socket.getInputStream().available()) >= 0) {
				socket.getInputStream().skip(available);
			}

			socket.getOutputStream().write(data);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e("SendMsg", "SendMsg: " + resultNumber);
		return resultNumber;
	}

	// SendUserinfo - 发送用户名和密码
	public void sendUserinfo(String username, byte[] key, int type, int flag) {
		UserLogin user = new UserLogin(username, key, type, flag);
		NetPack p = new NetPack(-1, UserLogin.size, Types.LoginUP,
				user.getBuf());
		p.size = NetPack.infoSize + UserLogin.size;
		p.CalCRC();
		sendPack(p);
	}
}