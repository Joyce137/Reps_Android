package com.example.ustc.healthreps.videoAudio.packages;

import com.example.ustc.healthreps.videoAudio.utils.Utils;

/**
 * Created by CaoRuijuan on 3/28/16.
 */
public class mysockaddr {
    static int size=16;

    public int sin_family;
    public int sin_port;
    //	public pa_addr sin_addr;
    public byte[] sin_zero=new byte[8];

    public mysockaddr(){
        this.sin_family=0;
        this.sin_port=0;
        for(int i=0;i<8;i++){
            this.sin_zero[i]=0;
        }
    }
    public byte[] tobyte(){
        byte[] byte_sum=new byte[16];
        byte[] i_sf_byte=new byte[2];
        byte[] i_sp_byte=new byte[2];
        byte[] p_byte=new byte[4];
        i_sf_byte= Utils.shortToLH(sin_family);
        i_sp_byte=Utils.toLH(sin_port);
//		p_byte=sin_addr.tobyte();

        System.arraycopy(i_sf_byte, 0, byte_sum, 0, 2);
        System.arraycopy(i_sp_byte, 0, byte_sum, 2, 2);
        System.arraycopy(p_byte, 0, byte_sum, 4, 4);
        System.arraycopy(sin_zero, 0, byte_sum, 8, 8);
        return byte_sum;
    }
    public static mysockaddr topack(byte[] byte_sum){
        mysockaddr pack=new mysockaddr();
        byte[] i_sf_byte=new byte[2];
        byte[] i_sp_byte=new byte[2];
        byte[] p_byte=new byte[4];

        System.arraycopy(byte_sum, 0, i_sf_byte, 0, 2);
        System.arraycopy(byte_sum, 2, i_sp_byte, 0, 2);
        System.arraycopy(byte_sum, 4, p_byte, 0, 4);
        pack.sin_family=Utils.vtolh(i_sf_byte);
        pack.sin_port=Utils.vtolh(i_sp_byte);
//		pack.sin_addr=pa_addr.topack(p_byte);
        System.arraycopy(byte_sum, 8, pack.sin_zero, 0, 8);

        return pack;
    }

}