package com.example.ustc.healthreps.threads;

/**
 * Created by CaoRuijuan on 1/25/16.
 */
public class LockRecv {
    int maxSize = 10000;
    public byte[] recvBuf = new byte[maxSize];	//用来存储整个包

    public void println(){
        for(int i = 0;i<recvBuf.length;i++){
            if(recvBuf[i] != 0)
                System.out.println(recvBuf[i]);
        }
    }

    public int getRealLength(){
        int realLenth = 0;
        for(int i = 0;i<recvBuf.length;i++){
            if(recvBuf[i] != 0)
                realLenth++;
        }
        return realLenth;
    }
}
