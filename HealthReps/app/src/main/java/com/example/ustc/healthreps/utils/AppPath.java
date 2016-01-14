package com.example.ustc.healthreps.utils;

import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.File;
import java.lang.String;
import java.util.ArrayList;

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

    //找到文件路径
    public static String findFilePath(String filePath,String filename){

        String originpath = "";
        try{
            ArrayList<String> items = new ArrayList<String>();
            ArrayList<String> paths = new ArrayList<String>();
            File f = new File(filePath);
            File[] files = f.listFiles();// 列出所有文件
            // 如果不是根目录,则列出返回根目录和上一目录选项
            if (!filePath.equals(filePath)) {
                items.add("返回根目录");
                paths.add(filePath);
                items.add("返回上一层目录");
                paths.add(f.getParent());
            }
            // 将所有文件存入list中
            if(files != null){
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++) {
                    File file = files[i];

                    if(file.getName() == filename){
                        return file.getPath();
                    }
                    items.add(file.getName());
                    paths.add(file.getPath());
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return originpath;
    }

    //删除文件
    public static void deleteFile(String filepath) {
        File file = new File(filepath);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i].getPath()); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            Log.e("deleteFile","文件不存在！" + "\n");
        }
    }

}
