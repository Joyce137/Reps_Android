package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

/**
 * Created by CaoRuijuan on 1/30/16.
 */
public class SearchUserInfo {
    public int type;        //医生/药店
    public int distance;    //距离
    public byte[] district = new byte[100];     //地址（区&详细地址)
    public byte[] companyName = new byte[100];  //医院名/药店属性
    public byte[] keshi = new byte[50];         //科室
    public int sortType;                       //排序类型
    public int rank;        //名次
    public int rate;
    public byte[] sex = new byte[5];
    public byte[] age = new byte[5];
    public int status; // 登陆状态 0 离线；1 在线；3 忙碌
    public byte[] zhicheng = new byte[20]; // 职称
    public byte[] realName = new byte[25]; // 真实姓名
    public byte[] loginName = new byte[25];// 登录名 必须唯一
    public int cw; // 中西药 true:中药，false:西药
    public byte[] shopName = new byte[100];
    public int vip;
    public int off; // false：不能接受离线文件
    public byte[] phone = new byte[20]; // 电话
    public byte[] dianzhang = new byte[15];
    public byte[] caozuoyuan = new byte[15];

    public static int SIZE = 4 + 4 + 100 + 100 + 50 + 2 + 4 + 4 + 4 + 5 + 5 + 2 + 4
            + 20 + 25 + 25 + 2 + 4 + 100 + 4 + 4 + 20 + 15 + 15 + 2;

    //转byte[]
    public byte[] getSearchUserInfoBytes(){
        byte[] buf = new byte[SIZE];
        //type
        byte temp[] = Utils.toLH(type);
        System.arraycopy(temp, 0, buf, 0, temp.length);

        //distance
        temp = Utils.toLH(distance);
        System.arraycopy(temp, 0, buf, 4, temp.length);

        //district
        System.arraycopy(district, 0, buf, 8, district.length);

        //companyName
        System.arraycopy(companyName, 0, buf, 108, companyName.length);

        //keshi
        System.arraycopy(keshi, 0, buf, 208, keshi.length);

        //sortType
        temp = Utils.toLH(type);
        System.arraycopy(temp, 0, buf, 260, temp.length);

        //rank
        temp = Utils.toLH(rank);
        System.arraycopy(temp, 0, buf, 264, temp.length);

        //rate
        temp = Utils.toLH(rate);
        System.arraycopy(temp, 0, buf, 268, temp.length);

        // sex
        System.arraycopy(sex, 0, buf, 272, sex.length);
        // age
        System.arraycopy(age, 0, buf, 277, age.length);
        // status
        temp = Utils.toLH(status);
        System.arraycopy(temp, 0, buf, 284, temp.length);
        // zhicheng
        System.arraycopy(zhicheng, 0, buf, 288, zhicheng.length);
        // realName
        System.arraycopy(realName, 0, buf, 308, realName.length);
        // loginName
        System.arraycopy(loginName, 0, buf, 333, loginName.length);

        // cw
        temp = Utils.toLH(cw);
        System.arraycopy(temp, 0, buf, 360, temp.length);

        // shopName
        System.arraycopy(shopName, 0, buf, 364, shopName.length);

        // vip
        temp = Utils.toLH(vip);
        System.arraycopy(temp, 0, buf, 464, temp.length);
        // off
        temp = Utils.toLH(off);
        System.arraycopy(temp, 0, buf, 468, temp.length);
        // phone
        System.arraycopy(phone, 0, buf, 472, phone.length);

        // dianzhang
        System.arraycopy(dianzhang, 0, buf, 492, dianzhang.length);
        // caozuoyuan
        System.arraycopy(caozuoyuan, 0, buf, 507, caozuoyuan.length);

        return buf;
    }

    //byte[]转对象
    public static SearchUserInfo getSearchUserInfo(byte[] buf){
        SearchUserInfo searchUserInfo = new SearchUserInfo();
        //type
        byte[] temp4 = new byte[4];
        System.arraycopy(buf, 0, temp4, 0, 4);
        searchUserInfo.type = Utils.vtolh(temp4);

        //distance
        System.arraycopy(buf, 4, temp4, 0, 4);
        searchUserInfo.distance = Utils.vtolh(temp4);

        //district
        System.arraycopy(buf, 8, searchUserInfo.district, 0, 100);

        //companyName
        System.arraycopy(buf, 108, searchUserInfo.companyName, 0, 100);

        //keshi
        System.arraycopy(buf, 208, searchUserInfo.keshi, 0, 50);

        //sortType
        System.arraycopy(buf, 260, temp4, 0, 4);
        searchUserInfo.sortType = Utils.vtolh(temp4);

        //rank
        System.arraycopy(buf, 264, temp4, 0, 4);
        searchUserInfo.type = Utils.vtolh(temp4);

        //rate
        System.arraycopy(buf, 268, temp4, 0, 4);
        searchUserInfo.type = Utils.vtolh(temp4);

        // sex
        System.arraycopy(buf, 272, searchUserInfo.sex, 0, 5);

        // age
        System.arraycopy(buf, 277, searchUserInfo.age, 0, 5);

        // status
        System.arraycopy(buf, 284, temp4, 0, 4);
        searchUserInfo.status = Utils.vtolh(temp4);

        // zhicheng
        System.arraycopy(buf, 288, searchUserInfo.zhicheng, 0, 20);

        // realName
        System.arraycopy(buf, 308, searchUserInfo.realName, 0, 25);
        // loginName
        System.arraycopy(buf, 333, searchUserInfo.loginName, 0, 25);

        // cw
        System.arraycopy(buf, 360, temp4, 0, 4);
        searchUserInfo.cw = Utils.vtolh(temp4);

        // shopName
        System.arraycopy(buf, 364, searchUserInfo.shopName, 0, 100);

        // vip
        System.arraycopy(buf, 464, temp4, 0, 4);
        searchUserInfo.vip = Utils.vtolh(temp4);
        // off
        System.arraycopy(buf, 468, temp4, 0, 4);
        searchUserInfo.off = Utils.vtolh(temp4);

        // phone
        System.arraycopy(buf, 472, searchUserInfo.phone, 0, 20);

        // dianzhang
        System.arraycopy(buf, 492, searchUserInfo.dianzhang, 0, 15);

        // caozuoyuan
        System.arraycopy(buf, 507, searchUserInfo.caozuoyuan, 0, 15);

        return searchUserInfo;
    }
}
