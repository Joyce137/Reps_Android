package com.example.ustc.healthreps.repo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.model.PrelistName;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.PreList;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.serverInterface.UserInfo;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.ui.MedicinePickList;
import com.example.ustc.healthreps.utils.AppPath;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class PrelistRepo extends ReceiveSuper{
    public static Handler sPrelistRepoControlmsgHandler = null;
    UserInfo user = Users.sLoginUser;
    //清单中药品列表
    public ArrayList<Medicine> prelist_medicine = new ArrayList<Medicine>();

    public PrelistRepo(){
        super();

//        if(Looper.myLooper() == null)
//            Looper.prepare();
        sPrelistRepoControlmsgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ControlMsg msg1 = (ControlMsg) msg.obj;
                onRecvControlmsg(msg1);
            }
        };
//        Looper.loop();
    }

    //收到清单结果消息
    public void onRecvControlmsg(ControlMsg msg){
        //清单成功
        if(msg.getFlag() == Types.PreList_Content && msg.isYesno()){
            String filePath = AppPath.getPathByFileType("本地文件");
            AppPath.CheckAndMkdirPathExist(filePath);

            filePath += "/";
            filePath += Users.sLoginUsername;
            AppPath.CheckAndMkdirPathExist(filePath);

            filePath += "/已发清单";
            AppPath.CheckAndMkdirPathExist(filePath);

//            String dateTime = Utils.getFileNameDate(msg.getFilename());
//            filePath += "/";
//            filePath += dateTime;
//            AppPath.CheckAndMkdirPathExist(filePath);

            filePath += "/";
            filePath += msg.getFilename();

            String filepathOrigin = AppPath.getPathByFileType("本地文件");
            filepathOrigin += "/";
            filepathOrigin += Users.sLoginUsername;
            filepathOrigin += "/待发清单/";
            String originPath = AppPath.findFilePath(filepathOrigin,msg.getFilename());

            if(originPath.length() >0){
                Sockets.socket_center.copyFile(originPath,filePath);
                AppPath.deleteFile(originPath);
            }

            //向UI传递成功消息
            String showmsg = "清单已经发送成功";
            MedicinePickList.sPrelistResultHandler.obtainMessage(0,showmsg).sendToTarget();
        }
    }

    //检查清单药品种类（选择药品时判断）
    //规则：开药种类不可超过5种，同类药已选择3种！
    public boolean checkPrelist(){

        return true;
    }

    public void sendPrelist(PrelistContent prelistContent){
        PreList prelist = savePrelistToLocal(prelistContent);
        if (prelist != null){
            NetPack pack = new NetPack(-1, PreList.size, Types.PreList_Content,prelist.getPreListBytes());
            pack.CalCRC();
            Sockets.socket_center.sendPack(pack);
        }
    }
    public PreList savePrelistToLocal(PrelistContent prelistContent){
        PreList p = new PreList();
        try{
            p.patient = user.realName;
            p.sex = user.sex;
            p.age = user.age;
            p.ID = user.ID_Num;
            p.feibie = prelistContent.feibie.getBytes("GBK");

            if(Users.sCurStore.length() == 0){
                p.shop = Users.sDefaultStore.getBytes("GBK");
            }
            else {
                p.shop = Users.sCurStore.getBytes("GBK");
            }

            p.doctor = getRandomDocName().getBytes("GBK");
            p.pharmacist = "".getBytes();
            p.phone = user.phone;
            p.selfreport = "Hello".getBytes("GBK");
            p.date = Utils.getDate().getBytes("GBK");
            p.creatorName = Users.sLoginUsername.getBytes("GBK");
            p.creatorType = Utils.changeTypeToInt(Users.sLoginUserType);
            p.status = 0;
            p.content = prelistContent.getPrelistcontentStr().getBytes("GBK");
            //filename
            String filenameStr = new PrelistName().getPrelistName(p);
            p.filename = filenameStr.trim().getBytes("GBK");

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //在本地存清单文件
        String filePath = AppPath.getPathByFileType("本地文件");
        AppPath.CheckAndMkdirPathExist(filePath);

        filePath += "/";
        filePath += Users.sLoginUsername;
        AppPath.CheckAndMkdirPathExist(filePath);

        filePath += "/待发清单";
        AppPath.CheckAndMkdirPathExist(filePath);

        try {
            filePath = filePath + "/"+new String(p.filename,"GBK").trim();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        if(Sockets.socket_center.savePrelist(filePath, p)){
            return p;
        }
        return null;
    }

    //获取随机医生名
    public String getRandomDocName(){
        if(Users.sDefaultDoctor.length() == 0)
            return "";
        else
            return Users.sDefaultDoctor;
    }

}
