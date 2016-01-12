package com.example.ustc.healthreps.utils;

import android.os.Environment;

import java.io.File;
import java.lang.String;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class AppPath {
    public static final String PACKAGE_NAME = "com.example.ustc.healthreps";

    //在手机里存放数据库的位置(/data/data/com.example.ustc.healthreps/databases/xx.db)
    public static final String DB_PATH = AppPath.getPathByFileType("databases");

    //在手机里存放数据库的位置(/data/data/com.example.ustc.healthreps)
    public static final String APP_PATH = "/data/data/"
//            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;

    //得到存放类型的path
    public static String getPathByFileType(String type){
        String path = APP_PATH + "/" + type;
        return path;
    }

       //判断路径是否存在，不存在则创建
    public static void CheckAndMkdirPathExist(String path){
        File file=new File(path);
        if(!file.exists())
            file.mkdir();
    }

}
