package com.joyce.reps.threads;

import java.io.IOException;

import android.util.Log;

import com.joyce.reps.LoginActivity;
import com.joyce.reps.model.Sockets;
import com.joyce.reps.model.TCPSocket;
import com.joyce.reps.serverInterface.LoginBackInfo;
import com.joyce.reps.serverInterface.NetPack;
import com.joyce.reps.serverInterface.PackHead;
import com.joyce.reps.serverInterface.Types;

public class ReceiveThread extends Thread {
	public String threadName = "ReceiveThread";

	public ReceiveThread() {
		this.setName(threadName);
	}

	public boolean isTrue = true;
	TCPSocket p = Sockets.socket_center; // 从主线程中传过来
	byte recvBuf[] = new byte[10000];
	boolean pack_err = false;
	boolean isPackageHead = false;

	@Override
	public void run() {
		synchronized (this) {
			Log.e("ReceiveMessageThread", "run() ");

			while (isTrue) {
				synchronized (this) {
					try {
						this.wait(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				try {
					p.mSocket.getInputStream().read(recvBuf);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (pack_err == false) {
					if (isPackageHead == true) {
						PackHead ph = PackHead.getPackHeadInfo(recvBuf);
						if (ph.getM_Start() == Types.PACK_START_FLAG) {
							isPackageHead = false;
						} else {
							for (int i = 0; i < recvBuf.length; i++) {
								recvBuf[i] = 0;
							}
							pack_err = true;
						}
					} else {
						NetPack data = NetPack.getNET_PACKInfo(recvBuf);
						if (data.VerifyCRC() == data.getM_Crc()) {
							// 交给RecvPack处理
							recvPack(data);

							// 将recvBuf清空
							for (int i = 0; i < recvBuf.length; i++) {
								recvBuf[i] = 0;
							}
							pack_err = true;
						} else {
							// 将recvBuf清空
							for (int i = 0; i < recvBuf.length; i++) {
								recvBuf[i] = 0;
							}
							pack_err = true;
						}
					}
				} else {
					// 取出recvBuf前4位，即检测其flag是什么
					NetPack data = NetPack.getNET_PACKInfo(recvBuf);
					if (data.getM_Start() == Types.PACK_START_FLAG) {
						// isPackageHead = true;
						pack_err = false;
					} else {
						byte ch = recvBuf[1];
						for (int i = 0; i < recvBuf.length; i++) {
							recvBuf[i] = 0;
						}
						recvBuf[0] = ch;
						pack_err = true;
					}
				}
			}
		}
	}

	// 分发数据包RecvPack
	public void recvPack(NetPack data) {
		Log.e("RecvPack", "RecvPack------");
		// 登录标志
		if (data.getM_nFlag() == Types.Login_is) {
			LoginBackInfo y = LoginBackInfo.getLogin_Back_Info(data
					.getM_buffer());
			if (y.getRecon() == Types.USER_LOGIN_FLAG) {
				// 登录-向登录界面发消息
				LoginActivity.sLoginHandler.obtainMessage(0, data)
						.sendToTarget();
			}
		} else if (data.getM_nFlag() == Types.INFONOYES) {
			// doControlMsg(data);
		} else {
			// onRecvNetPack(data);
		}
	}
}
