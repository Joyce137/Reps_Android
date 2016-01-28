package com.example.ustc.healthreps.repo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.ustc.healthreps.LoginActivity;
import com.example.ustc.healthreps.patient.RegisterActivity;
import com.example.ustc.healthreps.register.RegisterActivityFin;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.serverInterface.UserInfo;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.ReceiveThread;
import com.example.ustc.healthreps.threads.SendFileThread;
import com.example.ustc.healthreps.utils.CRC4;

import java.io.UnsupportedEncodingException;

/**
 * Created by CaoRuijuan on 12/20/15.
 */
public class RegisterRepo extends ReceiveSuper {
    public static Handler sRegisterHandler = null;
    private SendFileThread mSendFileThread = AllThreads.sSendFileThread;

    public static final int REG_INFO_TYPE = 0x0003;
    private int m_dlgType = REG_INFO_TYPE;

    public RegisterRepo(){

        super();
        sRegisterHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ControlMsg data = (ControlMsg) msg.obj;
                onRecvRegMessage(data);
            }
        };
        //socket初始化成功
        if(socketSuccess){
            //启动接收线程
            if(AllThreads.sReceiveThread == null){
                AllThreads.sReceiveThread = new ReceiveThread("Recv_Thread");
                AllThreads.sReceiveThread.start();
            }

            //启动发文件线程
            if(mSendFileThread == null){
                mSendFileThread = new SendFileThread();
                mSendFileThread.start();
            }
        }
        else {
            LoginActivity.sLoginResultHandler.obtainMessage(0, -1).sendToTarget();
        }
    }

    //登录
    public void register(UserInfo userInfo) {
        sendHeadImage(userInfo.loginName.toString(),userInfo.imagePath);
        sendTextInfo(userInfo);
    }
        //发头像
    private void sendHeadImage(String userName, String imagePath) {
        String fileName = userName+".jpg";
        //m_picPath = "/sdcard2/1.jpg";
        Sockets.socket_center.sendFile(imagePath, fileName, Types.FILE_TYPE_PIC_REG);
    }

        //发文本
    private void sendTextInfo(UserInfo userInfo) {
        NetPack pack = new NetPack(-1, UserInfo.size, m_dlgType == REG_INFO_TYPE?Types.Reg_Center:Types.Mod_Info, userInfo.getMSG_USER_INFOBytes());
        pack.CalCRC();
        Sockets.socket_center.sendPack(pack);
    }

    //接收注册结果信息
    public void onRecvRegMessage(ControlMsg msg) {
        int regResult;
        if(msg.getFlag() == Types.Reg_is){
            if (msg.isYesno())
            {
                regResult = 0;
            }
            else{
                regResult = msg.getType();
            }
            //发送消息到注册界面
            RegisterActivityFin.sRegisterResultHandler.obtainMessage(0, regResult).sendToTarget();

            //关闭发送文件线程
            closeSendFileThread();

            //关闭接收线程
//            closeReceiveThread();

            Looper.loop();
        }
    }

    //关闭发文件线程
    public void closeSendFileThread(){
        mSendFileThread.isTrue = false;
        mSendFileThread.interrupt();
        mSendFileThread = null;
    }

    //关闭接收线程
    @Override
    public void closeReceiveThread() {
        super.closeReceiveThread();
    }
}
