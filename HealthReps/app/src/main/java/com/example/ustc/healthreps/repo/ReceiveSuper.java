package com.example.ustc.healthreps.repo;

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

    private ReceiveThread mReceiveThread = AllThreads.sReceiveThread;

    public ReceiveSuper(){
        // 添加网络驻主线程网络访问权限
        AndroidNetAccess.netAccess();

        // 初始化socket// 初始化Socket
//        if(!Sockets.socket_center.initSocket()){
//            //Toast.makeText(this, "without network,init fail", Toast.LENGTH_LONG).show();
////            Intent intent = new Intent(TestActivity.this,ErrorActivity.class);
////            startActivity(intent);
//            return;
//        }

        // 启动接收线程
        // startReceiveThread();
    }

    //开启接收线程
    public void startReceiveThread(){
        if (mReceiveThread == null) {
            mReceiveThread = new ReceiveThread();
            mReceiveThread.start();
        }
    }

    // 关闭接收线程
    public void closeReceiveThread(){
        mReceiveThread.isTrue = false;
        mReceiveThread.interrupt();
        mReceiveThread = null;
    }
}
