package com.example.ustc.healthreps.repo;

import android.os.Handler;
import android.os.Message;

import com.example.ustc.healthreps.model.Medicine;
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

        sPrelistRepoControlmsgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ControlMsg msg1 = (ControlMsg) msg.obj;
                onRecvControlmsg(msg1);
            }
        };
    }

    //收到清单结果消息
    public void onRecvControlmsg(ControlMsg msg){
        //清单成功
        if(msg.getFlag() == Types.PreList_Content && msg.isYesno()){

            String dateTime = Utils.getFileNameDate(msg.getFilename());
            String filepath = AppPath.getPathByFileType("本地文件/");
            filepath += Users.sLoginUsername;
            filepath += "/已发清单/";
            filepath += dateTime;
            filepath += "/";
            filepath += msg.getFilename();

            String filepathOrigin = AppPath.getPathByFileType("本地文件/");
            filepathOrigin += Users.sLoginUsername;
            filepathOrigin += "/待发清单/";
            String originPath = AppPath.findFilePath(filepathOrigin,msg.getFilename());

            if(originPath.length() >0){
                Sockets.socket_center.copyFile(originPath,filepath);
                AppPath.deleteFile(originPath);
            }

            //向UI传递成功消息
            String showmsg = "清单已经发送成功";
            MedicinePickList.sPrelistResultHandler.obtainMessage(0,msg).sendToTarget();
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
            p.patient = "曹瑞娟".getBytes("GBK");//user.realName;
            p.sex = "女".getBytes("GBK");//user.sex;
            p.age = "10".getBytes("GBK");//user.age;
            p.ID = user.ID_Num;
            p.feibie = "自费".getBytes("GBK");
            p.shop = "store1".getBytes("GBK");//user.defaultStore;
            p.doctor = getRandomDocName().getBytes("GBK");
            p.pharmacist = "local".getBytes();
            p.phone = user.phone;
            p.selfreport = "Hello".getBytes("GBK");
            p.date = Utils.getDate().getBytes("GBK");
            p.creatorName = Users.sLoginUsername.getBytes("GBK");
            p.creatorType = Utils.changeTypeToInt(Users.sLoginUserType);
            p.status = 0;

            //content
            String prelistContentStr = "";
            List<Medicine> medicineList = prelistContent.medicines;
            for(int i = 0;i<medicineList.size();i++){
                Medicine medicine = medicineList.get(i);
                prelistContentStr += medicine.name;
                prelistContentStr += "$&*";
                prelistContentStr += medicine.spec;
                prelistContentStr += "$&*";
                prelistContentStr += medicine.getNum();
                prelistContentStr += "$&*";
                prelistContentStr += medicine.unit;
                prelistContentStr += "$&*";
                prelistContentStr += medicine.usage;    //用法用量（隐藏）
                prelistContentStr += "~#%";
                prelistContentStr += prelistContent.contentPost;    //备注
                prelistContentStr += "~#%";
                prelistContentStr += medicine.disease;    //对应病症
                //prelistContentStr += "&&";
            }

            p.content = prelistContentStr.getBytes("GBK");
            //filename
            //用户名
            String filenameStr = Users.sLoginUsername;
            filenameStr += "-";
            //医生名
            if(p.doctor.length==0){
                String randomDocName = getRandomDocName();
                filenameStr += randomDocName;
            }
            else {
                filenameStr += p.doctor;
            }

            //药剂师名
            filenameStr += "-";
            filenameStr += p.pharmacist;

            //病人名
            filenameStr += "-";
            filenameStr += p.patient;

            //时间
            filenameStr += "-";
            filenameStr += Utils.getDataAndTime();
            filenameStr += ".xdb";

            p.filename = filenameStr.getBytes("GBK");

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //在本地存清单文件
        String filePath = AppPath.getPathByFileType("本地文件/待发清单/");
        AppPath.CheckAndMkdirPathExist(filePath);
        filePath += filePath;
        if(Sockets.socket_center.savePrelist(filePath, p)){
            return p;
        }
        return p;
    }

    //获取随机医生名
    public String getRandomDocName(){
        return "doctor1";
    }

}
