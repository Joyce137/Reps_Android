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
    public static UserInfo sLoginUser;
    public static boolean sOnline;

    //所有医生列表
    public static ArrayList<DocPha> sAllDoctors = new ArrayList<DocPha>();
}
