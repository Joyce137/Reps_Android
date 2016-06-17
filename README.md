# HealthReps_Android
工程实践：#752面向远程医疗的移动信息客户端系统设计-基于Android

版本说明：2.0版本
  包含患者端、医生端、药师端、药监局端
  包含音视频整合
  蓝牙界面大变换，总体界面风格变换

运行平台：Android Studio
SDK版本:
    compileSdkVersion 23
    buildToolsVersion '23.0.0'

    defaultConfig {
        applicationId "com.example.ustc.healthreps"
        minSdkVersion 17
        targetSdkVersion 23
        versionCode 2
        versionName "2.0"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
    sourceSets { main { jni.srcDirs = ['src/main/jni', 'src/main/jni/aa'] } }
    
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
    register: 注册类集合
    citylist&gps: 定位相关，包括android自带API和百度地图API同时定位
    database: SQLite数据库相关，包含已经存在的数据库表的使用DBManagerForExist类，和新建数据库DBManagerForNew类
    serverInterface: 中央服务器接口类集合
    sms: 短信验证码相关
    
    -----modify-----
    adapter: 所有listview和tabActivity等控件对应的自定义adapter类
    heath: 蓝牙相关，其中
      service: 蓝牙设备和蓝牙相关服务
      ui: 蓝牙相关界面
      view: 蓝牙界面中自定义绘制图形
      util: 工具类集合
    patient: 患者相关类集合
    model: 相关实体类集合
    repo: 实体对应的操作功能类集合
    socket: TCP传输相关类集合
    threads: 线程类集合
    ui: 具体界面类集合
    utils: 总工具类集合
    
    -----add-----
    admin: 药监局相关类集合
    doctor: 医生相关类集合
    pha: 药师相关类集合
    videoAudio: 音视频相关类
      codec: 视频编码AvcEncoder,视频解码Decoder
      packages: UDP传输相关数据包格式
      threads: 音视频相关线程类集合
      utils: 音视频相关工具类集合
    
    
      
