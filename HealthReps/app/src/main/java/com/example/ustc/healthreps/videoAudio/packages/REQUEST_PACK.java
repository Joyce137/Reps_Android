package com.example.ustc.healthreps.videoAudio.packages;

import com.example.ustc.healthreps.videoAudio.utils.Utils;

/**
 * Created by CaoRuijuan on 3/30/16.
 */
public class REQUEST_PACK {
    public final static int USERNAME_SIZE=10;
    public static int SIZE = 22;

    public int flag;
    public byte[] mName = new byte[USERNAME_SIZE];
    public byte[] yName = new byte[USERNAME_SIZE];

    public REQUEST_PACK(){
        flag = UDPType.REQUEST_PACK_FLAG;
    }
    public byte[] tobyte(){
        byte[] byte_sum=new byte[SIZE];
        byte[] t_byte;
        t_byte= Utils.shortToLH(flag);

        System.arraycopy(t_byte, 0, byte_sum, 0, 2);
        System.arraycopy(mName, 0, byte_sum, 2, 10);
        System.arraycopy(yName, 0, byte_sum, 12, 10);
        return byte_sum;
    }
    public static REQUEST_PACK topack(byte[] byte_sum){
        REQUEST_PACK pack=new REQUEST_PACK();
        byte[] t_byte=new byte[2];

        System.arraycopy(byte_sum, 0, t_byte, 0, 2);
        pack.flag=Utils.vtolh(t_byte);
        System.arraycopy(byte_sum, 2, pack.mName, 0, 10);
        System.arraycopy(byte_sum, 12, pack.yName, 0, 10);

        return pack;
    }
}
