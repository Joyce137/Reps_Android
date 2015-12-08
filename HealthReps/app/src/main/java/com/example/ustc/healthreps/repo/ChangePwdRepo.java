package com.example.ustc.healthreps.repo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.example.ustc.healthreps.ChangePwdActivity;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.ModPassword;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
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
    }

    //提交密码
    public void submitPwd(String oldPwd,String newPwd){
        ModPassword mod = new ModPassword();
        try{
            mod.name = Users.sLoginUsername.getBytes("GBK");
            //密码加密
            mod.oldPwd = Utils.encryptPwd(oldPwd);
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
        if(msg.isYesno()){
            //修改成功
            toastMsg = "密码修改成功";
        }
        else {
            toastMsg = "密码修改失败，原密码输入不正确";
        }
        ChangePwdActivity.sMsgHandler.obtainMessage(0, toastMsg).sendToTarget();
        closeReceiveThread();
    }
}
