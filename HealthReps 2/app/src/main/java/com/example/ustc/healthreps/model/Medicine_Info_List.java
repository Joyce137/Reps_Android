package com.example.ustc.healthreps.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzy on 2016/1/12.
 * 在MedicList类中选择若干个Mediciine对象和数量传递到MedicinePickList
 */
public class Medicine_Info_List  implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;
    private List<Medicine> medicineEntityList = new  ArrayList<Medicine>();

    public Medicine_Info_List(){}

    public void setArrayList(List<Medicine> arrayList) {
        this.medicineEntityList = arrayList;
    }

    public List<Medicine> getArrayList() {
        return medicineEntityList;

    }

    public int getSize() {
        return medicineEntityList.size();
    }
}
