package com.example.ustc.healthreps.model;

/**
 * Created by CaoRuijuan on 12/7/15.
 */
public class Medicine {
    //表名称
    public static String TABLE = "data";

    //表项名
    public static String KEY_ID = "ID";         //ID
    public static String KEY_spec = "spec";     //规格
    public static String KEY_drugID = "drugID";      //货品ID
    public static String KEY_name = "name";     //通用名称
    public static String KEY_unit = "unit";     //单位
    public static String KEY_address = "address";  //产地
    public static String KEY_category = "category"; //类别
    public static String KEY_usage = "usage";    //用法用量
    public static String KEY_pinyin = "pinyin";   //拼音简码
    public static String KEY_taboo = "taboo";    //禁忌
    public static String KEY_disease = "disease";  //对应病症1

    //对象属性
    public int ID;          //ID
    public String spec;     //规格
    public int drugID;      //货品ID
    public String name;     //通用名称
    public String unit;     //单位
    public String address;  //产地
    public String category; //类别
    public String usage;    //用法用量
    public String pinyin;   //拼音简码
    public String taboo;    //禁忌
    public String disease;  //对应病症1

    private String MedicName;          //药品名字
    private String MedicCategroy;      //药品类别
    private String MedicBehavior;      //药品主治症状

    public Medicine(){
        super();
    }
    public Medicine(String MedicName,String MedicCategroy,String MedicBehavior){
        super();
        this.MedicName = MedicName;
        this.MedicCategroy = MedicCategroy;
        this.MedicBehavior = MedicBehavior;
    }

    public String getMedicName() {
        return MedicName;
    }

    public void setMedicName(String medicName) {
        MedicName = medicName;
    }

    public String getMedicCategroy() {
        return MedicCategroy;
    }

    public void setMedicCategroy(String medicCategroy) {
        MedicCategroy = medicCategroy;
    }

    public String getMedicBehavior() {
        return MedicBehavior;
    }

    public void setMedicBehavior(String medicBehavior) {
        MedicBehavior = medicBehavior;
    }
    public String toString() {
        return getMedicName() + "" + getMedicCategroy() + "" + getMedicBehavior();
    }
}
