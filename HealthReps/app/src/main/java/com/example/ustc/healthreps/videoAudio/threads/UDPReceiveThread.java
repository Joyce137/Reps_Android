package com.example.ustc.healthreps.videoAudio.threads;

import android.util.Log;

import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.videoAudio.packages.UDPType;
import com.example.ustc.healthreps.socket.UDPSocket;
import com.example.ustc.healthreps.videoAudio.packages.REQUEST_PACK;
import com.example.ustc.healthreps.videoAudio.utils.UDPUsers;
import com.example.ustc.healthreps.videoAudio.packages.PT_MESSAGE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.SocketException;

/**
 * Created by CaoRuijuan on 3/30/16.
 */
public class UDPReceiveThread implements Runnable{
    public static boolean isRun = true;
    int revMAXSIZE = 1500;
    byte[] revpack;
    @Override
    public void run() {
        try {
            byte[] buf = new byte[revMAXSIZE];// 创建一个byte类型的数组，用于存放接收到得数据
            while (isRun) {
                System.out.println("begin....");
                DatagramPacket pack = new DatagramPacket(buf, buf.length);// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小和长度
                UDPSocket.socket.receive(pack);// 读取接收到得数据包,如果客户端没有发送数据包，那么该线程就停滞不继续，这个同样也是阻塞式的
                // //////////////////////////////////
                // or maybe receive data from here
                revpack = pack.getData();
                dealwithpack(revpack);
                // //////////////////////////////////
                String ip = pack.getAddress().getHostAddress();// 得到发送数据包的主机的ip地址
                System.out.println(ip + "发送!\n");
            }
            // 注意不需要关闭服务器的socket，因为它一直等待接收数据
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dealwithpack(byte[] packbytes) {
        PT_MESSAGE PT_pack = PT_MESSAGE.topack(packbytes);
        switch (PT_pack.type) {
            // 登录成功消息
            case UDPType.LOGIN_FLAG:
                UDPUsers.isLogin = true;
                System.out.println("登陆成功！！！");
                Log.v("登陆：", "登录成功\n");
                break;
            //对方请求消息
            case UDPType.REQUEST_PACK_FLAG:
                REQUEST_PACK pack = REQUEST_PACK.topack(packbytes);
                String name1="",name2="";
                try {
                    name1 = new String(pack.mName,"GBK").trim();
                    name2 = new String(pack.yName,"GBK").trim();
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                if(name2.equals(Users.sLoginUsername)){
                    String alertMsg = name2+": "+name1+" 邀请你进行音视频聊天，是否同意？";

                }

                break;
//            // 获取对方信息成功
//            case UDPType.ADDR_FLAG:
//                UDPUsers.isGetname = true;
//                UDP_ADDR_PACK lppack = UDP_ADDR_PACK.topack(pack);
//                System.arraycopy(pack, 0, flag_b, 0, 2);
//                System.arraycopy(pack, 2, lppack.addr, 0, 16);// length of addr roughly 8.tobyte()
//                System.arraycopy(pack, 18, len_b, 0, 4);
//                System.arraycopy(lppack.addr, 0, peerAddr, 0, 16);
//                System.arraycopy(peerAddr, 2, peerPort, 0, 2);
//                // port的数值可否手动定义？//TODO
//                System.arraycopy(pack, 6, peerIP, 0, 4);
//                String ip = IPconvert(peerIP);
//                port = Utils.vtolh(peerPort);
//                System.out.println("获取对方信息成功！！！\n");
//                Log.v("对方信息：", "获取对方信息成功\n");
//                break;
//            // 收到对方的试探信号，说明建立连接成功
//            case UDPType.HANDSHAKE:
//                UDPUsers.isPentrate = true;
//                isRun = false;// 结束线程
//                isRun_audio = false;// 结束线程
//                HANDSHAKE_MESSAGE lp = HANDSHAKE_MESSAGE.topack(pack);
//                byte[] hs_byte_message = new byte[12];
//                HANDSHAKE_MESSAGE hs_message = new HANDSHAKE_MESSAGE();
//                hs_message.type = HANDSHAKE;
//                hs_message.username = lp.username;
//                hs_byte_message = hs_message.tobyte();
//                // 向对方发送一个消息
//                try {
//                    // 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
//                    DatagramPacket pack_datagram = new DatagramPacket(
//                            hs_byte_message, hs_byte_message.length,
//                            InetAddress.getByName(IPconvert(peerIP)), port);// peerIP.toString()
//                    // 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号
//
//                    System.out.println("packet length : "
//                            + pack_datagram.getLength());
//                    socket_login.send(pack_datagram);// 发送数据包 socket_thread
//                    pack_datagram = null;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
        }
    }
}
