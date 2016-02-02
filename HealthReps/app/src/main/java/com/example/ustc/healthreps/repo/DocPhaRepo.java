package com.example.ustc.healthreps.repo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.ustc.healthreps.database.impl.MedicineDaoImpl;
import com.example.ustc.healthreps.model.DocPha;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.patient.DoctorList;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.ReqMsgUserInfo;
import com.example.ustc.healthreps.serverInterface.ReqSingleUserInfo;
import com.example.ustc.healthreps.serverInterface.ReqUserInfo;
import com.example.ustc.healthreps.serverInterface.SingleUserInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.ui.DoctorSessionAty;
import com.example.ustc.healthreps.ui.SearchDoctor;
import com.example.ustc.healthreps.utils.AppPath;
import com.example.ustc.healthreps.utils.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class DocPhaRepo extends ReceiveSuper{
    public static Handler sDocPhaRepoHandler = null;
    public static Handler sDocPhaRepoControlmsgHandler = null;

    public ArrayList<DocPha> list = new ArrayList<DocPha>();
    public ArrayList<ReqMsgUserInfo> msgList = new ArrayList<ReqMsgUserInfo>();
    //sex,status(登陆状态 0 离线；1 在线；3 忙碌),zhicheng-职称,realName-真实姓名,loginName-登录名

    public String mPhotoPath = "";
    public SingleUserInfo mSingleUserInfo;
    public Context context;

    //连接类型
    private int mConnectType;

    private MedicineDaoImpl dao;

    public DocPhaRepo(){
        super();
        sDocPhaRepoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                NetPack data = (NetPack) msg.obj;
                onRecvDocPhaMessage(data);
            }
        };

        sDocPhaRepoControlmsgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ControlMsg msg1 = (ControlMsg) msg.obj;
                onRecvControlmsg(msg1);
            }
        };
    }
    //收到消息
    public void onRecvDocPhaMessage(NetPack data){
        //请求单个用户的详细信息
        if(data.getM_nFlag() == Types.REQ_SINGLE_USER_INFO){
            mSingleUserInfo = SingleUserInfo.getSingleUserInfo(data.getM_buffer());
        }

//        //搜索产生的简单用户信息-用作显示在列表中
//        else if(data.getM_nFlag() == Types.REQ_USER_INFO){
//            ReqMsgUserInfo req = ReqMsgUserInfo.getReqMsgUserInfo(data.getM_buffer());
//            //msgList.add(req);
//            SearchDoctor.sDocHandler.obtainMessage(0, req).sendToTarget();
//        }

//        closeReceiveThread();
    }

    //收到医生连接消息
    public void onRecvControlmsg(ControlMsg msg){

        String doctorName = msg.getUsername();

        //请求医生连接结果
        if(msg.getFlag() == Types.To_User){
            //请求后返回的消息
            if(msg.getType() == 0){

                //对方同意
                if(msg.isYesno()){
                    Users.sCurConnectDoc = doctorName;
                    //向界面传递对方已同意
                    String showmsg = "y"+doctorName+"已同意";
                    SearchDoctor.sDoctorResultHandler.obtainMessage(0,showmsg).sendToTarget();
                }
                //对方不同意
                else{
                    //向界面传递对方不同意
                    String showmsg = "n"+doctorName+"不同意";
                    SearchDoctor.sDoctorResultHandler.obtainMessage(0,showmsg).sendToTarget();
                }
            }
            //收到请求
            else if(msg.getType() == 1){
                String reqName = msg.getUsername();
                //向UI传递是否接收对方请求
                String showmsg = "i"+"是否接收"+doctorName+"的请求";
                //-----??????
            }
            //已建立连接
            else if(msg.getType() == 2){
                mConnectType = msg.isYesno()?Types.USER_TYPE_DOCTOR:Types.USER_TYPE_PHA;
                //向UI传递已与xx建立连接c
                String showmsg = "c"+"已与"+doctorName+"建立连接";
                SearchDoctor.sDoctorResultHandler.obtainMessage(0,showmsg).sendToTarget();
            }
        }
        //断开连接的消息
        else if(msg.getFlag() == Types.Off_Link){
            //通知界面
            String showmsg = "offc"+doctorName+"已与你断开连接";
            DoctorSessionAty.sConectionStatusHandler.obtainMessage(0, showmsg).sendToTarget();
        }
    }

    //连接医生
    public void connectDoctor(String doctor){
        Sockets.socket_center.reqOffLink(doctor,true);
    }

    //断开连接
    public void disConnectDoctor(String doctor){
        Sockets.socket_center.reqOffLink(doctor,false);
    }

    //获取所有医生/药剂师(true->Doc,false->Pha)
    public ArrayList<DocPha> getAllDocPha(boolean doc_pha){
        return list;
    }

    //根据科室获取医生/药剂师
    public ArrayList<DocPha> getDocPhaByKeshi(boolean doc_pha,String keshi){

        return list;
    }

    //根据搜索条件搜索
    public void searchDoctor(SearchInfo searchInfo){
        ReqUserInfo reqUserInfo = new ReqUserInfo();
        try{
            reqUserInfo.doc_pha = true; //医生
            reqUserInfo.keshi = searchInfo.keshi.getBytes("GBK");
            reqUserInfo.username = Users.sLoginUsername.getBytes("GBK");
            reqUserInfo.type= Utils.changeTypeToInt(Users.sLoginUserType);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }


    //搜索药店
    public void searchStore(SearchInfo searchInfo){

    }

    //根据科室请求医生信息
    public void reqDocInfoByKeshi(String keshiStr){
        ReqUserInfo req = new ReqUserInfo();
        try{
            req.doc_pha = true;
            req.keshi = keshiStr.getBytes("GBK");
            req.username = Users.sLoginUsername.getBytes("GBK");
            req.type = Types.USER_TYPE_PATIENT;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //NetPack
        NetPack p = new NetPack(-1,ReqUserInfo.SIZE,Types.REQ_USER_INFO,req.getReqUserInfoBytes());
        p.CalCRC();

        Sockets.socket_center.sendPack(p);
    }




    //请求医生详细信息
    public void reqDocDetailInfoByUsername(String username){
        ReqSingleUserInfo req = new ReqSingleUserInfo();
        try{
            req.username = username.getBytes("GBK");
            req.type = Types.USER_TYPE_DOCTOR;

            //判断本地是否有该用户头像
            String picName = username;
            String picPath = AppPath.getPathByFileType("pic")+"/"+picName+".jpg";
            //如果有
            if(new File(picPath).exists()){
                //请求中不需要图片
                req.isPicExist = false;
                mPhotoPath = picPath;
            }
            //没有
            else {
                //图片也请求
                req.isPicExist = true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NetPack p = new NetPack(-1,ReqSingleUserInfo.SIZE,Types.REQ_SINGLE_USER_INFO,req.getReqSingleUserInfoBytes());
        p.CalCRC();
        Sockets.socket_center.sendPack(p);
    }
}
