package com.example.ustc.healthreps.model;

public class MedicStore {

	private String StoreName;
	private String StoreCategroy;
	private String StroeZone;

	public  MedicStore(){

	}

	public MedicStore(String StoreName,String StoreCategroy,String StroeZone){
		super();
		this.StoreName = StoreName;
		this.StoreCategroy = StoreCategroy;
		this.StroeZone = StroeZone;
	}

	public String getStoreName() {
		return StoreName;
	}

	public void setStoreName(String storeName) {
		StoreName = storeName;
	}

	public String getStoreCategroy() {
		return StoreCategroy;
	}

	public void setStoreCategroy(String storeCategroy) {
		StoreCategroy = storeCategroy;
	}

	public String getStroeZone() {
		return StroeZone;
	}

	public void setStroeZone(String stroeZone) {
		StroeZone = stroeZone;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getStoreName()+"   "+getStoreCategroy()+"    "+getStroeZone();
	}
	
}
