package com.example.ustc.healthreps.serverInterface;

import android.graphics.Color;

//定义静态类型常量
//#define
public class Types {
	// Socket相关
	public static final String AES_KEY = "Wsn406";
	public static final int center_Port = 1001;
	public static final int version_Port = 1002;
	public static final String version_IP = "123.57.231.105"; // "114.214.166.134";

	// 数据包相关
	public static final int MAX_PACKBUFFER_SIZE = 8 * 1024; // 包体大小
	public static final int PACK_START_FLAG = 0x9202; // 包头标志
	public static final int HEADPACK = 8;

	// 心跳包相关
	public static final int HEAT_BEAT_FLAG = 0x00AA; // 心跳包标志
	public static final int HeartBeat = 0x0705; // 心跳包
	public static final int RECONNTECT = 0x0729;
	public static final int USER_RECONNECT_FLAG = 0x0741; // 断线重连
	public static final int BACKCOLOR = Color.rgb(241, 243, 243);
	public static final int USER_ONLINE_FLAG = 0x0740; // 主页面内上线
	public static final int Off_Link = 0x0709; // 断开会话

	// 用户类型
	public static final int USER_TYPE_STORE = 0x0001;
	public static final int USER_TYPE_DOCTOR = 0x0000;
	public static final int USER_TYPE_PHA = 0x0002;
	public static final int USER_TYPE_PATIENT = 0x0003; // 用户类型为患者

	// 登录相关
	public static final int LoginUP = 0x0800; // 登陆信息 密码和账号
	public static final int Login_is = 0x0706; // 登陆后 服务器返回的消息
	public static final int USER_LOGIN_FLAG = 0x0739; // 初始页面登录

	// 注册相关
	public static final int Reg_Center = 0x0707; // 注册 消息包
	public static final int Reg_is = 0x0708; // 注册 服务器返回的消息

	// 用户信息相关
	public static final int REQ_USER_INFO = 0x0728;
	public static final int ForwardInfo = 0x0730;
	public static final int Mod_Pass = 0x0731;
	public static final int Mod_Info = 0x0732;
	public static final int BangDing = 0x750; // 绑定
	public static final int MODIFY_USER_INFO = 0x0756;

	// 请求相关
	public static final int REQ_SINGLE_USER_INFO = 0x0734;
	public static final int To_User = 0x0712; // 请求链接
	public static final int MM_RECVCONNREQ = 0x0745; // 自定义消息

	// 功能相关
	public static final int Chufang_Content = 0x0710; // 处方内容
	public static final int PreList_Content = 0x0711; // 清单内容
	public static final int CheckChufang = 0x0713; // 药剂师审核通过后 发送给服务器的消息
													// 也用Do_Control_Msg结构体
	public static final int Req_AllPharmacist = 0x0714; // 请求所有药剂师
	public static final int Req_AllDoctors = 0x0902; // 请求所有医生
	public static final int Req_AllSTORES = 0x0751; // 请求所有药店

	// 文件相关
	public static final int FileFree = 500000; // 文件指针定时器 释放时间 一秒为1000
	public static final int FILE_MAX_BAG = 1000; // 当发送文件的时候 每个包发送多少个字符
	public static final int FILESTRAT = 0x0801; // 文件头
	public static final int FILEEND = 0x0802; // 文件尾
	public static final int FILETAG = 0x0803; // 文件标志
	public static final int MY_MSG = 0x0805; // postthread
	public static final int INFONOYES = 0x0700; // 一些验证消息之类的标志
	public static final int FILESENDNOYES = 0x0701; // 文件发送成功与否
	public static final int FileList = 0x0717; // 文件列表
	public static final int Req_File = 0x0715; // 请求文件
	public static final int FILE_TYPE_PIC_REG_SMALL = 0x0735; // 小窗口请求资质图片
	public static final int FILE_TYPE_PRES_CHECK_REJECT = 0x0736; // 审核后 拒绝的处方

	public static final int FILE_TYPE_CHAT_PICTURE = 0x0901; // 普通会话发送的图片

	public static final int FILE_TYPE_PIC_REG = 0x0718; // 用户注册资质图片
	public static final int FILE_TYPE_PRES_CHECK = 0x0720; // 审核后处方
	public static final int FILE_TYPE_PRES_LOCAL_UNCHECK = 0x072a; // 本地待审核处方
	public static final int FILE_TYPE_PRES_UNCHECK = 0x0721; // 未审核处方
	public static final int FILE_TYPE_DOCPHA_STAMP = 0x0722;
	public static final int FILE_TYPE_HOSPITAL_STAMP = 0x0723;
	public static final int FILE_TYPE_PRELIST = 0x0727; // 清单文件

	public static final int MOD_FILE_TYPE_PIC_REG = 0x072b; // 用户注册资质图片
	public static final int MOD_FILE_TYPE_USER_FP = 0x072c; // 用户指纹
	public static final int MOD_FILE_TYPE_DOCPHA_STAMP = 0x072d;

	public static final int ORDER_STATUS_PRELIST = 0x0903;//订单状态：只是prelist，还没有生成处方图片
	public static final int ORDER_STATUS_CHUFANG = 0x0904;//订单状态：已生成处方图片，但是待第三方药师审核
	public static final int ORDER_STATUS_CHECKED = 0x0905;//订单状态：已完成处方图片且已审核，或由药店的本地药师审核

	public static final int CHUFANG_GENERATE_TO_PATIENT = 0x0906;//医生给手机app患者用户生成的处方，服务器收到后需要转成清单
	public static final int CHUFANG_GENERATE_TO_STORE = 0x0907;//医生给药店用户生成的处方

	//排序类型
	public static final int SORTTYPE_SMART = 0x0001;		//智能排序
	public static final int SORTTYPE_CLOSE = 0x0002;		//离我最近
	public static final int SORTTYPE_POPULAR = 0x0003;		//人气最高
	public static final int SORTTYPE_COMMENT = 0x0004;		//评价最好

	//searchUser flag
	public static final int Search_Users_By_Rules = 0x0908;	//移动端设定特定条件搜索医生或药店;

	//req perlist status
	public static final int PATIENT_REQ_PRELIST_STATUS = 0x0910;  //请求清单状态
}
