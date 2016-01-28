package com.example.ustc.healthreps.repo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.example.ustc.healthreps.ChangePwdActivity;
import com.example.ustc.healthreps.LoginActivity;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.ModPassword;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.ReceiveThread;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class ChangePwdRepo extends ReceiveSuper{
    public static Handler sChangePwdRepoHandler = null;
    public String toastMsg;

    public ChangePwdRepo(){
        super();
        sChangePwdRepoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ControlMsg msg1 = (ControlMsg) msg.obj;
                onRecvChangePwdMessage(msg1);
            }
        };

        //socket初始化成功
        if(socketSuccess){
            //启动接收线程
            if(AllThreads.sReceiveThread == null){
                AllThreads.sReceiveThread = new ReceiveThread("Recv_Thread");
                AllThreads.sReceiveThread.start();
            }
        }
        else {
            ChangePwdActivity.sMsgHandler.obtainMessage(0, -1).sendToTarget();
        }
    }

    //提交密码
    public void submitPwd(String username,String newPwd){
        ModPassword mod = new ModPassword();
        try{
            mod.name = username.getBytes("GBK");
            //密码加密
            mod.oldPwd = Utils.encryptPwd("zoompass");
            mod.newPwd = Utils.encryptPwd(newPwd);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NetPack p = new NetPack(-1,ModPassword.SIZE, Types.Mod_Pass,mod.getModPasswordBytes());
        p.CalCRC();
        Sockets.socket_center.sendPack(p);
    }

    //接收修改结果
    public void onRecvChangePwdMessage(ControlMsg msg){
//        if(msg.isYesno()){
//            //修改成功
//            toastMsg = "密码设置成功";
//        }
//        else {
//            toastMsg = "密码设置失败";
//        }
        toastMsg = "新密码设置成功";
        ChangePwdActivity.sMsgHandler.obtainMessage(0, toastMsg).sendToTarget();
//        closeReceiveThread();
    }
}
