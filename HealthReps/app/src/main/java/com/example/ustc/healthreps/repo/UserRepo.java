package com.example.ustc.healthreps.repo;

import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.ReqSingleUserInfo;
import com.example.ustc.healthreps.serverInterface.ReqUsernameInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by CaoRuijuan on 12/21/15.
 */
public class UserRepo {
    //请求登录用户信息
    public void reqUserInfo(String username,String type, boolean withImage){
        ReqSingleUserInfo info = new ReqSingleUserInfo();
        try{
            info.username = Users.sLoginUsername.getBytes("GBK");
            info.type = Utils.changeTypeToInt(type);
            info.isPicExist = true;
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        NetPack p = new NetPack(-1,ReqSingleUserInfo.SIZE, Types.MODIFY_USER_INFO,info.getReqSingleUserInfoBytes());
        p.CalCRC();
        Sockets.socket_center.sendPack(p);
    }

    //请求所有医生
    public void reqAllDoctors(){
        ReqUsernameInfo req = new ReqUsernameInfo();
        try{
            req.username = Users.sLoginUsername.getBytes("GBK");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        NetPack p1 = new NetPack(-1,ReqUsernameInfo.SIZE,Types.Req_AllDoctors,req.getReqUsernameInfoBytes());
        p1.CalCRC();
        Sockets.socket_center.sendPack(p1);
    }
}
