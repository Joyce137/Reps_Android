package com.example.ustc.healthreps.repo;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.ustc.healthreps.LoginActivity;
import com.example.ustc.healthreps.TestActivity;
import com.example.ustc.healthreps.database.entity.Cookie;
import com.example.ustc.healthreps.database.impl.CookieDaoImpl;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.repo.ReceiveSuper;
import com.example.ustc.healthreps.serverInterface.LoginBackInfo;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.ReqSingleUserInfo;
import com.example.ustc.healthreps.serverInterface.ReqUsernameInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.socket.TCPSocket;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.HandlerThread;
import com.example.ustc.healthreps.threads.HeartBeatTask;
import com.example.ustc.healthreps.threads.LockRecv;
import com.example.ustc.healthreps.threads.ReceiveThread;
import com.example.ustc.healthreps.threads.ReceiveThread1;
import com.example.ustc.healthreps.utils.CRC4;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by CaoRuijuan on 12/17/15.
 */
public class LoginRepo extends ReceiveSuper{
    public static Handler sLoginHandler = null;
    public static int mBeatTimes = 0;
//    private ReceiveThread mReceiveThread = AllThreads.sReceiveThread;
    public static Handler sAlertHandler;

    private byte[] crcPwd;      //加密后的密码
    private int userType;       //用户类型

    public LoginRepo(){
        super();

        sLoginHandler =new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                NetPack data = (NetPack) msg.obj;
                onRecvLoginMessage(data);
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
            LoginActivity.sLoginResultHandler.obtainMessage(0, -1).sendToTarget();
        }

    }
    // 登录事件
    public void login(String username,String pwd,String type) {
        Users.sLoginUserType = type;

        startReceiveThread();
        // 密码加密

        String str = pwd;
        crcPwd = str.getBytes();
        for (int i = 0; i < crcPwd.length; i++)
            crcPwd[i] = 0;
        byte strb[] = str.getBytes();
        for (int i = 0; i < str.length(); i++) {
            crcPwd[i] = strb[i];
        }
        CRC4 crc = new CRC4();
        byte b[] = Types.AES_KEY.getBytes();
        crc.Encrypt(crcPwd, b);

        // 判断用户类型
        userType = Types.USER_TYPE_PATIENT;
        if (type.equals("患者")) {
            userType = Types.USER_TYPE_PATIENT;
        } else if (type.equals("医生")) {
            userType = Types.USER_TYPE_DOCTOR;
        } else if (type.equals("药师")) {
            userType = Types.USER_TYPE_PHA;
        } else {
            userType = Types.USER_TYPE_PATIENT;
        }

        //设置用户类型为药店
        userType = Types.USER_TYPE_PATIENT;
        Sockets.socket_center.sendUserinfo(username, crcPwd, userType, Types.USER_LOGIN_FLAG);
    }

    //判断cookie是否有效
    public Cookie validCookie(CookieDaoImpl cookieDao){
        ArrayList<Cookie> list = cookieDao.findAll();
        if(list.size() != 0){
            Cookie cookie = list.get(0);
            String cookieDate = cookie.cookieDate;

            //第一条时间已超过一个月
            if(!Utils.checkValid(cookieDate)){
                cookieDao.delete(1);
            }
            //有效，则返回用户名
            return cookie;
        }
        else
            return null;
    }

    //添加到cookie
    public void addToCookie(CookieDaoImpl cookieDao){
        if(cookieDao.checkUsernameExistInCookie(Users.sLoginUsername.trim()) == null){
//            cookieDao.addNewUserToCookie(Users.sLoginUsername,crcPwd,userType);
            //先清空后插入
            cookieDao.clear();
            cookieDao.addNewUserToCookie();
        }
        else{
            cookieDao.updateDate(1);
        }
    }

    // 收到login登录结果信息
    public void onRecvLoginMessage(NetPack data) {
        System.out.println("onRecvLoginMessage-----------");
        String login_result = "";
        LoginBackInfo y = LoginBackInfo.getLogin_Back_Info(data.getM_buffer());
        Users.sLoginUsername = y.getUsername();
        Sockets.socket_center.mUsername = y.getUsername();
        boolean yesno = y.isYesno();
        if (yesno) {
            // 登录成功
            Users.sLoginUsername = y.getUsername();
            Users.sLoginUserType = Utils.changeTypeToString(y.getType());
            Users.sOnline = true;

            LoginActivity.sLoginResultHandler.obtainMessage(0, 0).sendToTarget();
        } else {
            // 1、登陆的时候 type = 1 密码和账号错误；type = 2 已经在线; type = 3 使用错了客户端; type = 4 审核未过
            Users.sOnline = false;
            int type = y.getType();
            LoginActivity.sLoginResultHandler.obtainMessage(0, type).sendToTarget();
        }
//        closeReceiveThread();
    }


    //关闭接收线程
    @Override
    public void closeReceiveThread() {
        super.closeReceiveThread();
    }
}
