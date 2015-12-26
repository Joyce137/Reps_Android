package com.example.ustc.healthreps.repo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.example.ustc.healthreps.database.DBManagerForExist;
import com.example.ustc.healthreps.model.DocPha;
import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.patient.DoctorList;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.ReqMsgUserInfo;
import com.example.ustc.healthreps.serverInterface.ReqSingleUserInfo;
import com.example.ustc.healthreps.serverInterface.ReqUserInfo;
import com.example.ustc.healthreps.serverInterface.SingleUserInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
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
    public ArrayList<DocPha> list = new ArrayList<DocPha>();
    public ArrayList<ReqMsgUserInfo> msgList = new ArrayList<ReqMsgUserInfo>();
    //sex,status(登陆状态 0 离线；1 在线；3 忙碌),zhicheng-职称,realName-真实姓名,loginName-登录名

    public String mPhotoPath = "";
    public SingleUserInfo mSingleUserInfo;
    public Context context;

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

    //搜索药品（从本地数据库搜索）
    public ArrayList<Medicine> searchDrug(SearchInfo searchInfo,Context context){
        DBManagerForExist dbManager = new DBManagerForExist(context);
        dbManager.openDatabase();

        SQLiteDatabase database = dbManager.getDatabase();

        //Medicine逻辑对象
        MedicineRepo mp = new MedicineRepo(database);

        //查询结果列表
        ArrayList<Medicine> list = mp.getMedicineByCategory(searchInfo.drugCategory);

        dbManager.closeDatabase();
        return list;
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

    //收到消息
    public void onRecvDocPhaMessage(NetPack data){
        //请求单个用户的详细信息
        if(data.getM_nFlag() == Types.REQ_SINGLE_USER_INFO){
            mSingleUserInfo = SingleUserInfo.getSingleUserInfo(data.getM_buffer());
        }

        //搜索产生的简单用户信息-用作显示在列表中
        else if(data.getM_nFlag() == Types.REQ_USER_INFO){
            ReqMsgUserInfo req = ReqMsgUserInfo.getReqMsgUserInfo(data.getM_buffer());
            //msgList.add(req);
            DoctorList.sDocHandler.obtainMessage(0, req).sendToTarget();
        }

        closeReceiveThread();
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
