package com.example.ustc.healthreps.database.impl;

import android.content.Context;

import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.database.support.DaoSupportImpl;
import com.example.ustc.healthreps.repo.SearchInfo;

import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 1/11/16.
 */
public class MedicineDaoImpl extends DaoSupportImpl<Medicine>{
    private Context context;

    public MedicineDaoImpl(Context context){
        super(context, "medicine",true);
        this.context = context;
    }


    //根据条件查找
    public ArrayList<Medicine> queryMedicinesByNameOrCategory(String searchStr){
        String[] colums = new String[]{DBConstants.MEDICINE_name,DBConstants.MEDICINE_category,DBConstants.MEDICINE_disease};
//        String selector = DBConstants.MEDICINE_name + " like \'"+searchStr+"\' or " + DBConstants.MEDICINE_category + " like \'"+searchStr+"\'";
        String selector = DBConstants.MEDICINE_name + " like \"%" + searchStr + "%\"" + " or " + DBConstants.MEDICINE_category + " like \"%" + searchStr + "%\"";
        return find(colums,selector,null);
//        String sqlStr = " where " + DBConstants.MEDICINE_name + " like \"%" + searchStr + "%\"" + " or " + DBConstants.MEDICINE_category + " like \"%" + searchStr + "%\"";
//        return executeSql(colums,sqlStr);
    }


    //根据类型查找
    public ArrayList<Medicine> queryMedicineBySearch(SearchInfo info){
        String[] colums = new String[]{DBConstants.MEDICINE_name,DBConstants.MEDICINE_category,DBConstants.MEDICINE_disease};
        String selector = DBConstants.MEDICINE_category + "=?";
        String[] selectorargs = new String[]{info.drugStoreCategory};
        return find(colums,selector,selectorargs,null);
    }
}
