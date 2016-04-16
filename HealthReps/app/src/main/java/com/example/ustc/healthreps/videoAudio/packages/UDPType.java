package com.example.ustc.healthreps.videoAudio.packages;

/**
 * Created by CaoRuijuan on 3/30/16.
 */
public class UDPType {
    public final static int REQUEST_PACK_FLAG = 8985;
    public final static int LOGIN_FLAG = 8988;
    public final static int ADDR_FLAG = 8986;
    public final static int HANDSHAKE = 1004; // 对方发送的试探信号
    public final static int PENTRATE = 1003; // 客户端发送请求，请求服务器让对方联系自己
    public final static int PANTRATEFAILED = 1005; // 穿透失败
}
