package com.example.ustc.healthreps.model;

import com.example.ustc.healthreps.serverInterface.SingleUserInfo;
import com.example.ustc.healthreps.serverInterface.UserInfo;

import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class Users {
    public static String sLoginUsername;
    public static String sLoginUserType;
    public static UserInfo sLoginUser = new UserInfo();
    public static boolean sOnline;

    //默认药店
    public static String sDefaultStore = null;
    //私人医生
    public static String sDefaultDoctor = null;

    //所有医生列表
    public static ArrayList<DocPha> sAllDoctors = new ArrayList<DocPha>();

    //当前连接的医生
    public static String sCurConnectDoc = null;
}
