package com.example.ustc.healthreps.repo;

import android.os.StrictMode;
import android.widget.Toast;

import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.ReceiveThread;
import com.example.ustc.healthreps.utils.AndroidNetAccess;

/**
 * Created by CaoRuijuan on 12/8/15.
 * 接收消息父类
 */
public class ReceiveSuper {

//    public ReceiveThread mReceiveThread = AllThreads.sReceiveThread;
    public boolean socketSuccess = true;

    public ReceiveSuper(){
        // 添加网络驻主线程网络访问权限
        AndroidNetAccess.netAccess();

        // 初始化socket
        if(!Sockets.socket_center.initSocket()){
            socketSuccess = false;
            System.out.println("socket init fail ");
//            Toast.makeText(this, "without network,init fail", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(TestActivity.this,ErrorActivity.class);
//            startActivity(intent);
            return;
        }

//        // 启动接收线程
//        startReceiveThread();
    }

    //开启接收线程
    public void startReceiveThread(){
        if (AllThreads.sReceiveThread == null) {
            AllThreads.sReceiveThread = new ReceiveThread("Recv_thread");
            AllThreads.sReceiveThread.start();
        }
    }

    // 关闭接收线程
    public void closeReceiveThread(){
        AllThreads.sReceiveThread.isTrue = false;
        AllThreads.sReceiveThread.interrupt();
        AllThreads.sReceiveThread = null;
    }
}
