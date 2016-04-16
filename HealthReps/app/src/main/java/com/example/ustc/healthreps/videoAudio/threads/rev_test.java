package com.example.ustc.healthreps.videoAudio.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.util.Log;

public class rev_test implements Runnable {
	private String re = null;
	newclassrev lock_rev;
	byte[] revpack;
	int MAXSIZE = 530;
	int pack_len=526;

	int pos_rev = 0;
	int p_num_rev = 0;
	long currenttime_rev = 0;

	public rev_test(newclassrev lock){
		this.lock_rev=lock;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramSocket socket = null;
		try {
			// socket = new DatagramSocket(8881);// 创建DatagramSocket对象并绑定一个本地端口号
			if (socket == null) {
				socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.bind(new InetSocketAddress(8881));
			}
			while (true) {
				System.out.println("begin....");
				byte[] buf = new byte[pack_len];// 创建一个byte类型的数组，用于存放接收到得数据 MAXSIZE
				DatagramPacket pack = new DatagramPacket(buf, buf.length);// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小和长度
				socket.receive(pack);// 读取接收到得数据
				// 包,如果客户端没有发送数据包，那么该线程就停滞不继续，这个同样也是阻塞式的
				// //////////////////////////////////
				// or maybe receive data from here
				revpack = pack.getData();
				synchronized (lock_rev) {
					// /LZ
					if (lock_rev.n < 3)
						lock_rev.n++;
					else
						lock_rev.n = 3;
					System.arraycopy(revpack, 0, lock_rev.lock_buf_rev,	pos_rev * pack_len, pack_len);
					lock_rev.flag_rev[pos_rev] = 1;
					if (++pos_rev == 500) {
						// if (lock.flag[0] == 1)
						// ;
						pos_rev = 0;
					}
				}
				p_num_rev++;// /////////////////////////////////////////
				if ((System.currentTimeMillis() - currenttime_rev) >= 1000) {
					Log.v("pack_num_rev:", String.valueOf(p_num_rev));
					currenttime_rev = System.currentTimeMillis();
					p_num_rev = 0;
				}
				// //////////////////////////////////
				String ip = pack.getAddress().getHostAddress();// 得到发送数据包的主机的ip地址
				re = ip + "发送!\n";
				System.out.println(re);
			}
			// 注意不需要关闭服务器的socket，因为它一直等待接收数据
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// /////////////////////////////////////////////////////
	/**/
}