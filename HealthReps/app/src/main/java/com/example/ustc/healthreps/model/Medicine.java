package com.example.ustc.healthreps.model;

import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.annotation.ColumnName;
import com.example.ustc.healthreps.database.annotation.PrimaryKey;
import com.example.ustc.healthreps.database.annotation.TableName;

import java.io.Serializable;

/**
 * Created by CaoRuijuan on 12/7/15.
 */
@TableName(DBConstants.TABLE_Medicine)
public class Medicine implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    //对象属性
    @PrimaryKey(autoincrement=true)
    @ColumnName(DBConstants.MEDICINE_ID)
    public int ID;          //ID

    @ColumnName(DBConstants.MEDICINE_spec)
    public String spec;     //规格

    @ColumnName(DBConstants.MEDICINE_drugID)
    public int drugID;      //货品ID

    @ColumnName(DBConstants.MEDICINE_name)
    public String name;     //通用名称

    @ColumnName(DBConstants.MEDICINE_unit)
    public String unit;     //单位

    @ColumnName(DBConstants.MEDICINE_address)
    public String address;  //产地

    @ColumnName(DBConstants.MEDICINE_category)
    public String category; //类别

    @ColumnName(DBConstants.MEDICINE_usage)
    public String usage;    //用法用量

    @ColumnName(DBConstants.MEDICINE_pinyin)
    public String pinyin;   //拼音简码

    @ColumnName(DBConstants.MEDICINE_taboo)
    public String taboo;    //禁忌

    @ColumnName(DBConstants.MEDICINE_disease)
    public String disease;  //对应病症1

    public int num=0;                   //选药品数量，生成药品清单用

    public Medicine(){
        super();
    }
}
