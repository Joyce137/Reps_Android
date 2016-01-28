package com.example.ustc.healthreps.health.util;

/**
 * Created by HBL on 2015/12/25.
 */
public class CStrictMode {
    public static void strictMode()
    {
        //严苛模式（StrictMode）
        android.os.StrictMode.setThreadPolicy(new android.os.StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        android.os.StrictMode.setVmPolicy(new android.os.StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
    }


}
