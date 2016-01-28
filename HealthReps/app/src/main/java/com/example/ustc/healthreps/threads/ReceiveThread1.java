package com.example.ustc.healthreps.threads;

import com.example.ustc.healthreps.socket.Sockets;

import java.io.IOException;

/**
 * Created by CaoRuijuan on 1/25/16.
 */
public class ReceiveThread1 extends Thread{
    LockRecv recv;

    public ReceiveThread1(LockRecv recv){
        this.recv = recv;
    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                synchronized (recv){
                    Sockets.socket_center.mSocket.getInputStream().read(recv.recvBuf);
                    recv.println();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
