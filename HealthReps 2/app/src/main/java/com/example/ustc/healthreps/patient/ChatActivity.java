package com.example.ustc.healthreps.patient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.repo.ChatRepo;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.P2PInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;

import java.io.UnsupportedEncodingException;

/**
 * Created by CaoRuijuan on 12/9/15.
 */
public class ChatActivity extends MainActivity{
    public static Handler sChatHandler = null;
    private ChatRepo repo = new ChatRepo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_session);
        initView();
    }

    //初始化界面
    public void initView(){
        sChatHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                P2PInfo info = (P2PInfo) msg.obj;
                showMsg(info);
            }
        };

        //发送msg
        repo.sendMsg("12","123");
    }

    //显示消息
    public void showMsg(P2PInfo info){
        String name = info.username.toString();
        String toUsername = info.toUsername.toString();
        String msg = info.info.toString();
    }
}
