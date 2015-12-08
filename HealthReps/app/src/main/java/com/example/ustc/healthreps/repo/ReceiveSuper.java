package com.example.ustc.healthreps.repo;

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
        // 启动接收线程
        if (mReceiveThread == null) {
            mReceiveThread = new ReceiveThread();
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
