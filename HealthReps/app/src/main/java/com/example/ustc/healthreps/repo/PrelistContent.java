package com.example.ustc.healthreps.repo;

import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.model.PrelistName;
import com.example.ustc.healthreps.serverInterface.PreList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoRuijuan on 12/21/15.
 */
public class PrelistContent implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;

    public List<Medicine> medicines = new ArrayList<>();   //药品集合
    public String feibie;               //费别（自费、省医保、市医保、农合）
    public String contentPost;          //备注
    private String medicinesStr = "";        //药品字符串
    private String prelistcontentStr = "";          //内容字符串
    private String diseasesStr = "";             //隐藏属性-对应病症

    public String getMedicinesStr() {
        return medicinesStr;
    }

    public String getPrelistcontentStr() {
        return prelistcontentStr;
    }

    public String getDiseasesStr() {
        return diseasesStr;
    }

    public PrelistContent(){

    }
    public PrelistContent(List<Medicine> medicines, String feibie, String contentPost){
        this.medicines = medicines;
        this.feibie = feibie;
        this.contentPost = contentPost;

        //算出药品字符串和对应病症
        for (Medicine medicine:medicines) {
            //药品
            medicinesStr = medicine.name;
            medicinesStr += "$&*";
            medicinesStr += medicine.category;
            medicinesStr += "$&*";
            medicinesStr += medicine.num;
            medicinesStr += "$&*";
            medicinesStr += medicine.unit;
            medicinesStr += "$&*";
            medicinesStr += medicine.usage;    //用法用量（隐藏）

            prelistcontentStr += medicinesStr;
            prelistcontentStr += "$&*";

            //对应病症
            diseasesStr += medicine.disease;
//            diseasesStr += " ";
//            diseasesStr += medicine.disease2;    //对应病症2
//            diseasesStr += " ";
//            diseasesStr += medicine.disease3;    //对应病症3

            prelistcontentStr += diseasesStr;
            prelistcontentStr += "~#%";
        }

        prelistcontentStr += contentPost;   //备注
    }

    public static PrelistContent getPrelistContentByStr(String prelistcontentStr){
        PrelistContent prelistContent = new PrelistContent();
        String allMedicinesStr[] = prelistcontentStr.split("~#%");
        prelistContent.contentPost = allMedicinesStr[allMedicinesStr.length-1];

        for(int i = 0;i<allMedicinesStr.length-1;i++){
            Medicine medicine = new Medicine();
            String curMedicineStr = allMedicinesStr[i];
            //每条清单
            String everyMedicineStr[] = curMedicineStr.split("\\$&\\*");
            medicine.name = everyMedicineStr[0];
            medicine.category = everyMedicineStr[1];
            medicine.num = Integer.parseInt(everyMedicineStr[2]);
            medicine.unit = everyMedicineStr[3];
            medicine.usage = everyMedicineStr[4];
            medicine.disease = everyMedicineStr[5];

            prelistContent.medicines.add(medicine);
        }
        prelistContent.prelistcontentStr = prelistcontentStr;
        return prelistContent;
    }
}
