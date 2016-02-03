package com.example.ustc.healthreps;

import android.app.Activity;
import android.os.Bundle;

import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.ReceiveThread;
import com.example.ustc.healthreps.utils.AndroidNetAccess;

/**
 * Created by CaoRuijuan on 12/11/15.
 */
public class ReceiveActivity extends Activity{
    private ReceiveThread mReceiveThread = AllThreads.sReceiveThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加网络驻主线程网络访问权限
        AndroidNetAccess.netAccess();

        // 初始化Socket
        Sockets.socket_center.initSocket();

        // 启动接收线程
        if (mReceiveThread == null) {
            mReceiveThread = new ReceiveThread("ResvThread");
            mReceiveThread.start();
        }
    }

    public void closeReceiveThread(){
        // 关闭接收线程
        mReceiveThread.isTrue = false;
        mReceiveThread.interrupt();
        mReceiveThread = null;
    }
}
