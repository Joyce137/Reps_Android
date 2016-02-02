package com.example.ustc.healthreps.model;

import com.example.ustc.healthreps.serverInterface.PreList;
import com.example.ustc.healthreps.utils.Utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by CaoRuijuan on 2/1/16.
 */
public class PrelistName implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;

    public String storeName;
    public String doctorName;
    public String pharmacistName;
    public String patientRealName;
    public String date;
    public String time;
    public String prelistNameStr;

    public String getPrelistName(PreList preList){
        try {
            storeName = new String(preList.shop,"GBK").trim();
            doctorName = new String(preList.doctor,"GBK").trim();
            pharmacistName = new String(preList.pharmacist,"GBK").trim();
            patientRealName = new String(preList.patient,"GBK").trim();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        date = Utils.getDate().trim();
        time = Utils.getTime().trim();

        if(doctorName.length() == 0)
            doctorName = "doctor";
        if(pharmacistName.length() == 0)
            pharmacistName = "local";

        prelistNameStr = storeName + "-" + doctorName
                + "-" + pharmacistName + "-" + patientRealName
                + "-" + date + "-" + time + ".xdb";
        return prelistNameStr;
    }

    public static PrelistName getPrelistFromName(String prelistNameStr){
        PrelistName prelistName = new PrelistName();
        String fileValues[] = prelistNameStr.split("-");
        prelistName.storeName = fileValues[0];
        prelistName.doctorName = fileValues[1];
        prelistName.pharmacistName = fileValues[2];
        prelistName.patientRealName = fileValues[3];
        prelistName.date = fileValues[4];
        prelistName.time = fileValues[5].substring(0,6);

        PreList preList = new PreList();
        preList.prelistName = prelistName;
        try {
            preList.shop = fileValues[0].getBytes("GBK");
            preList.filename = prelistNameStr.getBytes("GBK");
            preList.patient = fileValues[3].getBytes("GBK");
            preList.doctor = fileValues[1].getBytes("GBK");
            preList.pharmacist = fileValues[2].getBytes("GBK");
            preList.date = fileValues[4].getBytes("GBK");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return prelistName;
    }
}
