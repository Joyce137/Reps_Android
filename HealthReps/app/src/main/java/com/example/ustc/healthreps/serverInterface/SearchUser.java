package com.example.ustc.healthreps.serverInterface;

import com.example.ustc.healthreps.utils.Utils;

/**
 * Created by CaoRuijuan on 1/26/16.
 */
public class SearchUser {
    public int type;        //医生/药店
    public float longitude; //经度
    public float latitude;  //纬度
    public int distance;    //距离（附近xx千米)
    public byte[] district = new byte[100];     //地址（区&详细地址)
    public byte[] companyName = new byte[100];  //医院名/药店属性
    public byte[] keshi = new byte[50];         //科室
    public int sortType;                       //排序类型

    public static int SIZE = 4 + 4 + 4 + 4 + 100 + 100 + 50 + 2 + 4;

    public SearchUser(){
        super();
    }

    public SearchUser(int type){
        this.type = type;
        distance = 40000;
        district = "".getBytes();
        companyName = "".getBytes();
        keshi = "".getBytes();
        sortType = Types.SORTTYPE_SMART;
    }

    //转byte[]
    public byte[] getSearchUserBytes(){
        byte[] buf = new byte[SIZE];
        //type
        byte temp[] = Utils.toLH(type);
        System.arraycopy(temp, 0, buf, 0, temp.length);

        //longitude
        temp = Utils.float2byte(longitude);
        System.arraycopy(temp, 0, buf, 4, temp.length);

        //latitude
        temp = Utils.float2byte(latitude);
        System.arraycopy(temp, 0, buf, 8, temp.length);

        //distance
        temp = Utils.toLH(distance);
        System.arraycopy(temp, 0, buf, 12, temp.length);

        //district
        System.arraycopy(district, 0, buf, 16, district.length);

        //companyName
        System.arraycopy(companyName, 0, buf, 116, companyName.length);

        //keshi
        System.arraycopy(keshi, 0, buf, 216, keshi.length);

        //sortType
        temp = Utils.toLH(sortType);
        System.arraycopy(temp, 0, buf, 268, temp.length);

        return buf;
    }

    //byte[]转对象
    public static SearchUser getSearchUser(byte[] buf){
        SearchUser searchUser = new SearchUser();
        //type
        byte[] temp4 = new byte[4];
        System.arraycopy(buf, 0, temp4, 0, 4);
        searchUser.type = Utils.vtolh(temp4);

        //longitude
        System.arraycopy(buf, 4, temp4, 0, 4);
        searchUser.longitude = Utils.byte2float(temp4);

        //latitude
        System.arraycopy(buf, 8, temp4, 0, 4);
        searchUser.latitude = Utils.byte2float(temp4);

        //distance
        System.arraycopy(buf, 12, temp4, 0, 4);
        searchUser.distance = Utils.vtolh(temp4);

        //district
        System.arraycopy(buf, 16, searchUser.district, 0, 100);

        //companyName
        System.arraycopy(buf, 116, searchUser.companyName, 0, 100);

        //keshi
        System.arraycopy(buf, 216, searchUser.keshi, 0, 50);

        //sortType
        System.arraycopy(buf, 268, temp4, 0, 4);
        searchUser.sortType = Utils.vtolh(temp4);

        return searchUser;
    }
}
