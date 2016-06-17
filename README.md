# HealthReps_Android
工程实践：#752面向远程医疗的移动信息客户端系统设计-基于Android

版本说明：
  仅仅包含患者端
  没有音视频整合
  蓝牙属于初级阶段，仅仅包含界面
  功能部分仅实现核心功能，其余api功能齐全

运行平台：Android Studio
SDK版本:
    compileSdkVersion 23
    buildToolsVersion '23.0.0'

    defaultConfig {
        applicationId "com.example.ustc.healthreps"
        minSdkVersion 17
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"
    }
所需外部jar
    compile 'de.hdodenhof:circleimageview:2.0.0'
    compile 'com.android.support:appcompat-v7:23.1.1'
    compile 'com.android.support:design:23.1.1'
    compile 'com.android.support:recyclerview-v7:+'
    compile 'com.android.support:cardview-v7:+'
    compile 'com.google.android.gms:play-services-appindexing:8.1.0'
    compile files('libs/BaiduLBS_Android.jar')
    compile files('libs/pinyin4j-2.5.0.jar')
    //短信验证码sdk
    compile name:'SMSSDK-2.0.1',ext:'aar'
    
包说明：
    adapter: 所有listview和tabActivity等控件对应的自定义adapter类
    citylist&gps: 定位相关，包括android自带API和百度地图API同时定位
    database: SQLite数据库相关，包含已经存在的数据库表的使用DBManagerForExist类，和新建数据库DBManagerForNew类
    heath: 蓝牙相关，其中
      service: 蓝牙设备和蓝牙相关服务
      ui: 蓝牙相关界面
      view: 蓝牙界面中自定义绘制图形
      util: 工具类集合
    model: 相关实体类集合
    register: 注册类集合
    patient: 患者相关类
    serverInterface: 中央服务器接口类集合
    sms: 短信验证码相关
    socket: TCP传输相关类集合
    threads: 线程类集合
    ui: 具体界面类集合
    utils: 总工具类集合
      
