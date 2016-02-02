package com.example.ustc.healthreps.model;

import com.example.ustc.healthreps.serverInterface.SingleUserInfo;
import com.example.ustc.healthreps.serverInterface.UserInfo;

import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class Users {
    public static String sLoginUsername = "";
    public static String sLoginPassword = "";
    public static String sLoginUserType = "患者";
    public static UserInfo sLoginUser = new UserInfo();
    public static boolean sISDetailUserInfo = false;
    public static boolean sOnline = false;
    public static boolean sISSignout = false;

    //默认药店
    public static String sDefaultStore = "";
    //私人医生
    public static String sDefaultDoctor = "";
    public static String sCurStore = "";

    //所有医生列表
    public static ArrayList<DocPha> sAllDoctors = new ArrayList<DocPha>();

    //当前连接的医生
    public static String sCurConnectDoc = "";

    public static UserInfo sRegisterUser = new UserInfo();  //正注册的用户
}
