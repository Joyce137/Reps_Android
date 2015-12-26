package com.example.ustc.healthreps.repo;

/**
 * Created by CaoRuijuan on 12/21/15.
 */
public class SearchInfo {
    //医生
    public String doctorName;   //医生名
    public String distance;     //附近---距离
    public String address;      //附近---地址
    public String hospital;     //医院---医院
    public String keshi;        //医院---科室

    //药店
    public String drugStoreName;        //药店名
    public String drugStoreCategory;    //药店类别

    //药品
    public String drugName;             //药品名
    public String drugCategory;         //药品类型

    //all
    public String sortMethod;   //排序方法
}
