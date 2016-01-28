package com.example.ustc.healthreps.utils;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用配置类
 * Created by CaoRuijuan on 1/27/16.
 */
public class AppManager extends Application {
    /**打开的activity**/
    private List<Activity> activities = new ArrayList<Activity>();
    /**应用实例**/
    private static AppManager instance;
    /**
     *  获得实例
     * @return
     */
    public static AppManager getInstance(){
        return instance;
    }
    /**
     * 新建了一个activity
     * @param activity
     */
    public void addActivity(Activity activity){
        activities.add(activity);
    }
    /**
     *  结束指定的Activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if (activity!=null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * 应用退出，结束所有的activity
     */
    public void exit(){
        for (Activity activity : activities) {
            if (activity!=null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}