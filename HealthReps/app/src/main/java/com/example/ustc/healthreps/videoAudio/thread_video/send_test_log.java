package com.example.ustc.healthreps.videoAudio.thread_video;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.example.ustc.healthreps.videoAudio.link_package.UDP_FORWARD_PACK;
import android.util.Log;

public class send_test_log implements Runnable {
	byte[] snd_buf;

	//	public final static int PANTRATEFAILED=1005;		//穿透失败
	newclass lock;
	String IP;
	byte[] peerIP;
	int pack_len=526;
	int port;
	byte[] peerPort;
	int mode;
	byte[] peerAddr;

	int i = 0;// /LZ
	byte[] sndpack = new byte[pack_len];
	int p_num_snd = 0;
	long currenttime_snd = 0;
	boolean isSend;

	public send_test_log(newclass L){
		this.lock=L;
	}
	public void setIP(String ip, int p){
		this.IP=ip;
		this.port=p;
	}
	public void setMode(int m){
		this.mode=m;
	}
	public void setPeerIP(byte[] addr, byte[] ip, byte[] p){
		this.peerAddr=addr;
		this.peerIP=ip;
		this.peerPort=p;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramSocket socket = null;
		try {
			if (socket == null) {
				socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.bind(new InetSocketAddress(8880));
			}
			while (true) {
				try {
					Thread.sleep(5);// /LZ
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				synchronized (lock) {
					// /LZ
					if (lock.n == 3 && lock.flag[i] == 1) {
						System.arraycopy(lock.lock_buf, i * pack_len, sndpack, 0, pack_len);
						isSend = true;
						lock.flag[i] = 0;
						if (++i == 500)
							i = 0;
					} else {
						isSend = false;
					}
				}
				if(mode!=1){
					UDP_FORWARD_PACK fpack=new UDP_FORWARD_PACK();
					System.arraycopy(peerAddr, 0, fpack.addr, 0, 16);
//					System.arraycopy(peerPort, 0, fpack.addr, 2, 2);
//					System.arraycopy(peerIP, 0, fpack.addr, 4, 4);
					System.arraycopy(sndpack, 0, fpack.data, 0, sndpack.length);
					fpack.len=sndpack.length;
					snd_buf=new byte[552];
					snd_buf=fpack.tobyte();
				} else {
					snd_buf = new byte[526];
					System.arraycopy(sndpack, 0, snd_buf, 0, 526);
				}
//				snd_buf=sndpack;
				if (isSend) {
					// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
					DatagramPacket pack_datagram = new DatagramPacket(snd_buf, snd_buf.length, InetAddress.getByName(IP), port);// inetAddress//8881
					// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号

					System.out.println("packet length : " + pack_datagram.getLength());
					p_num_snd++;// /////////////////////////////////////////
					if ((System.currentTimeMillis() - currenttime_snd) >= 1000) {
						Log.v("pack_num_snd:", String.valueOf(p_num_snd));
						currenttime_snd = System.currentTimeMillis();
						p_num_snd = 0;
					}
					socket.send(pack_datagram);// 发送数据包
					pack_datagram = null;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}