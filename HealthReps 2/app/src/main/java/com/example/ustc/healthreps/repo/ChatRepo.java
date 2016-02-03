package com.example.ustc.healthreps.repo;

import android.os.Handler;
import android.os.Message;

import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.patient.ChatActivity;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.P2PInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by CaoRuijuan on 12/21/15.
 */
public class ChatRepo extends ReceiveSuper{
    public static Handler sChatHandler = null;
    public ChatRepo(){
        super();
        sChatHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                NetPack data = (NetPack) msg.obj;
                onRecvChatMsg(data);
            }
        };
    }

    //发送消息
    public void sendMsg(String toUsername, String msg){
        P2PInfo p2PInfo = new P2PInfo();
        try{
            p2PInfo.username = Users.sLoginUsername.getBytes("GBK");
            p2PInfo.toUsername = toUsername.getBytes("GBK");
            p2PInfo.info = msg.getBytes("GBK");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NetPack p = new NetPack(-1,P2PInfo.SIZE, Types.ForwardInfo,p2PInfo.getP2PInfoByte());
        p.CalCRC();
        Sockets.socket_center.sendPack(p);
    }

    //接收消息
    public void onRecvChatMsg(NetPack pack){
        P2PInfo info = P2PInfo.getP2PInfo(pack.getM_buffer());
        ChatActivity.sChatHandler.obtainMessage(0, info).sendToTarget();
    }
}
