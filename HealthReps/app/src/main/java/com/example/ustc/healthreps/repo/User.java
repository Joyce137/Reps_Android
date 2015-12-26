package com.example.ustc.healthreps.repo;

/**
 * Created by CaoRuijuan on 12/20/15.
 */
public class User {
    public String userName;     //用户名
    public String email;        //邮箱
    public String password;     //密码
    public String realName;     //真实姓名
    public String sex;          //性别
    public String age;          //年龄----通过出生日期计算
    public String idCardNumber;     //身份证号
    public String yibaoCardNumber;  //医保卡号
    public String address;          //地址
    public String selfIntroduction; //个人自述
    public String phoneNumber;      //手机号码
    public String imagePath;        //头像图片路径

    //专属药店
    public String defaultStore = "store1";
    //vip
    public int vip = 0;
}
