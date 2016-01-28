package com.example.ustc.healthreps.threads;
import android.util.Log;

import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.PackHead;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.utils.Utils;

import java.io.IOException;

/**
 * Created by CaoRuijuan on 1/24/16.
 */
//接收线程
public class ReceiveThread extends Thread{
	Object lock = new Object(); // static确保只有一把锁
	public ReceiveThread(String threadName){
		this.setName(threadName);
	}
	//    TCPSocket p = Sockets.socket_center; //从主线程中传过来
	public boolean isTrue = true;
	int maxSize = 10000;
	byte[] recvBuf = new byte[maxSize];	//用来存储整个包
	byte[] buff = new byte[maxSize];	//仅仅用来read
	boolean pack_err = false;
	boolean isPackageHead = true;
	int len = Types.HEADPACK;		//read时单个包的read长度
	int retVal = 0;						//read到的实际长度
	int length = 0;					//包的总长度（包括包头）
	int buffCounter = 0;			//buff指向recvbuf的偏移量
	@Override
	public void run() {
		synchronized (lock) {
			Log.e("ReceiveMessageThread", "run() ");

			while (true) {

				synchronized (this) {
					try {
						this.wait(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				try {
					//read:返回的这个数组中的字节个数，缓冲区没有满，则返回真实的字节个数，到未尾时都返回-1
					retVal = Sockets.socket_center.mSocket.getInputStream().read(buff,0,len);
					System.out.println("----len-----"+len + "------retval------"+retVal);

					//根据buff指针偏移量复制到recvbuf
					System.arraycopy(buff, 0, recvBuf, buffCounter, retVal);
				} catch (IOException e) {
					e.printStackTrace();
				}
				catch (Exception e){
					if(retVal == -1){
						try {
							this.wait(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						continue;
					}
				}

				//缓冲区满了
				if(retVal == maxSize){
					synchronized (this) {
						try {
							this.wait(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					continue;
				}

				if(retVal == len){
					if (pack_err == false) {
						if (isPackageHead == true) {
							//读取的是包头
							PackHead ph = PackHead.getPackHeadInfo(recvBuf);
							//是开始
							if (ph.getM_Start() == Types.PACK_START_FLAG) {
								//解析包头中显示的包内容长度
								buffCounter = retVal;	//移动buff指针
								len = ph.getnDataLen();	//下一个要接收的包的长度
								isPackageHead = false;	//不是包头了
								length = length + retVal;	//此包的总长度
							}
							else {
								//不是开始
								//recvbuf清零，且buff指向0
								for (int i = 0; i < recvBuf.length; i++) {
									recvBuf[i] = 0;
								}
								//buff = recvBuf;
								buffCounter = 0;

								//下次读两个字节
								len = 2;
								//报错误
								pack_err = true;
								//总长度清零
								length = 0;
							}
						}
						else {
							//不是包头，包内容
							//读到的包的总长度
							length = length + retVal;

							NetPack data = NetPack.getNET_PACKInfo(recvBuf);
							if (data.VerifyCRC() == data.getM_Crc()) {
								// 交给RecvPack处理
								Sockets.socket_center.recvPack(data);

								// 将recvBuf清空
								for (int i = 0; i < recvBuf.length; i++) {
									recvBuf[i] = 0;
								}

								//指针重新指向0
								//buff = recvBuf;
								buffCounter = 0;

								//下一个还接收包头
								len = Types.HEADPACK;
								isPackageHead = true;
							} else {
								//包校验有问题
								// 将recvBuf清空
								for (int i = 0; i < recvBuf.length; i++) {
									recvBuf[i] = 0;
								}
								//指针归零
								//buff = recvBuf;
								buffCounter = 0;
								//下次接收2个
								len = 2;
								pack_err = true;
								length = 0;
							}
						}
					}
					else {
						//有错时，再读2个
						// 取出recvBuf前2位，即检测其flag是什么
						byte temp[] = new byte[4];
						System.arraycopy(recvBuf,0,temp,0,2);
						int flag = Utils.vtolh(temp);
						//flag是开始标志
						if (flag == Types.PACK_START_FLAG) {
							//使用此时的开始标志
							//buff下移两位
							buffCounter += 2;
							//下次读6位
							len = Types.HEADPACK - 2;
							isPackageHead = true;
							pack_err = false;
							length = 2;
						} else {
							//不是开始,留一位recvBuf[1]-recvBuf[0]
							byte ch = recvBuf[1];
							for (int i = 0; i < recvBuf.length; i++) {
								recvBuf[i] = 0;
							}
							recvBuf[0] = ch;
							len = 1;
							length = 1;
							//buff = recvBuf[1];
							buffCounter = 1;
							pack_err = true;
						}
					}
				}
				else{
					//继续接收
					//buff = buff + retVal;
					buffCounter = buffCounter + retVal;
					len = len - retVal;
					length = length + retVal;
				}
			}
		}
	}
};