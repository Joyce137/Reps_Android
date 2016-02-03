package com.example.ustc.healthreps.serverInterface;

/**
 * Created by CaoRuijuan on 1/26/16.
 */
public class SearchUser {
    public int type;        //医生/药店
    public float longitude; //经度
    public float latitude;  //纬度
    public int distance;    //距离（附近xx米)
    public byte[] district = new byte[100];     //地址（区&详细地址)
    public byte[] companyName = new byte[100];  //医院名/药店属性
    public byte[] keshi = new byte[50];         //科室
    public byte sortType;                       //排序类型
}
