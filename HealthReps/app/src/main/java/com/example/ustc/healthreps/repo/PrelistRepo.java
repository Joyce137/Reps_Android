package com.example.ustc.healthreps.repo;

import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.PreList;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.serverInterface.UserInfo;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.utils.AppPath;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class PrelistRepo extends ReceiveSuper{
    UserInfo user = Users.sLoginUser;
    //清单中药品列表
    public ArrayList<Medicine> prelist_medicine = new ArrayList<Medicine>();

    public PrelistRepo(){
        super();
    }

    //检查清单药品种类（选择药品时判断）
    //规则：开药种类不可超过5种，同类药已选择3种！
    public boolean checkPrelist(){

        return true;
    }

    //获取content
    public String getContent(){
        String content = null;
        return content;
    }

    //获取filename
    public String getFilename(){
        String filename = null;
        return filename;
    }

    public void sendPrelist(){
        PreList prelist = savePrelistToLocal();
        if (prelist != null){
            NetPack pack = new NetPack(-1, PreList.size, Types.PreList_Content,prelist.getPreListBytes());
            pack.CalCRC();
            Sockets.socket_center.sendPack(pack);
        }
    }
    public PreList savePrelistToLocal(){
        PreList p = new PreList();
        try{
            p.patient = user.realName;
            p.sex = user.sex;
            p.age = user.age;
            p.ID = user.ID_Num;
            p.feibie = "自费".getBytes("GBK");
            p.shop = user.defaultStore;
            p.doctor = "local".getBytes();
            p.pharmacist = "local".getBytes();
            p.phone = user.phone;
            p.selfreport = "Hello".getBytes("GBK");
            p.date = Utils.getDate().getBytes("GBK");
            //content
            p.content = getContent().getBytes("GBK");
            //filename
            p.filename = getFilename().getBytes("GBK");

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //在本地存清单文件
        String filePath = AppPath.getPathByFileType("本地文件/待发清单/");
        if(Sockets.socket_center.savePrelist(filePath, p)){
            return p;
        }
        return null;
    }

}
