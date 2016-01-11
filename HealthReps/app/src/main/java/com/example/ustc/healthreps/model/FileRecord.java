package com.example.ustc.healthreps.model;
/*
 * 文件管理类之记录类
 * 2015/13/13
 * hzy
 */
public class FileRecord {

	private String time;   //记录时间
	private String mstore; //药店信息
	private String doctor; //医生
	private String record; //会诊记录信息
	private String flag;   //状态
	public FileRecord(String time,String mstore,String doctor,String record,String flag){
		this.time = time;
		this.mstore = mstore;
		this.doctor = doctor;
		this.record = record;
		this.flag = flag;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMstore() {
		return mstore;
	}
	public void setMstore(String mstore) {
		this.mstore = mstore;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "FileRecord [time=" + time + ", mstore=" + mstore + ", doctor="
				+ doctor + ", record=" + record + ", flag=" + flag + "]";
	}
	
	
}
