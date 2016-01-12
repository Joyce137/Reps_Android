package com.example.ustc.healthreps.model;

import java.io.Serializable;

/*
 * 药品类
 * 2015/12/12
 * hzy
 */
public class Medicine  implements Serializable {

	private static final long serialVersionUID = -7060210544600464481L;
	private String MedicName;          //药品名字
	private String MedicCategroy;      //药品类别
	private String MedicBehavior;      //药品主治症状
    private int num=0;                   //选药品数量，生成药品清单用

	public Medicine(String MedicName,String MedicCategroy,String MedicBehavior){
		super();
		this.MedicName = MedicName;
		this.MedicCategroy = MedicCategroy;
		this.MedicBehavior = MedicBehavior;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
