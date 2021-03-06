package com.example.ustc.healthreps.serverInterface;

//ChuFang  ---处方内容
public class Prescription {
	public byte[] patient = new byte[12]; // 病人姓名
	public byte[] sex = new byte[5];
	public byte[] age = new byte[5];
	public byte[] ID = new byte[5];
	public byte[] feibie = new byte[10];
	public byte[] address = new byte[100];
	public byte[] content = new byte[800];
	public byte[] shop = new byte[12];
	public byte[] doctor = new byte[12];
	public byte[] pharmacist = new byte[12];
	public byte[] phone = new byte[15];
	public byte[] selfreport = new byte[50]; // 患者自述
	public byte[] filename = new byte[100];
	public byte[] date = new byte[10];
	public byte[] title = new byte[100];
	public byte[] bianhao = new byte[100];

	public static int SIZE = 12+5+5+5+10+100+800+12+12+12+15+50+100+10+100+100;

	//Prescription->byte[]
	public byte[] getPrescriptionByte(){
		byte[] buf = new byte[SIZE];

		//patient
		System.arraycopy(patient,0,buf,0,patient.length);
		//sex
		System.arraycopy(sex,0,buf,12,sex.length);
		// age
		System.arraycopy(age, 0, buf, 17, age.length);
		//ID
		System.arraycopy(ID, 0, buf, 22, ID.length);
		//feibie
		System.arraycopy(feibie, 0, buf, 27, feibie.length);
		// address
		System.arraycopy(address, 0, buf, 37, address.length);
		//content
		System.arraycopy(content, 0, buf, 137, content.length);
		// shop
		System.arraycopy(shop, 0, buf, 937, shop.length);
		//doctor
		System.arraycopy(doctor, 0, buf, 949, doctor.length);
		//pharmacist
		System.arraycopy(pharmacist, 0, buf, 961, pharmacist.length);
		//phone
		System.arraycopy(phone, 0, buf, 973, phone.length);
		//selfreport
		System.arraycopy(selfreport, 0, buf, 988, selfreport.length);
		//filename
		System.arraycopy(filename, 0, buf, 1038, filename.length);
		//date
		System.arraycopy(date, 0, buf, 1138, date.length);
		//title
		System.arraycopy(title, 0, buf, 1148, title.length);
		//bianhao
		System.arraycopy(bianhao, 0, buf, 1248, bianhao.length);

		return buf;
	}

	//byte[]->Prescription
	public static Prescription getPrescription(byte[] buf){
		Prescription p = new Prescription();
		byte[] temp = null;

		//patient
		System.arraycopy(buf,0,temp,0,12);
		p.patient = temp;
		//sex
		System.arraycopy(buf,12,temp,0,5);
		p.sex = temp;
		// age
		System.arraycopy(buf,17,temp,0,5);
		p.age = temp;
		//ID
		System.arraycopy(buf,22,temp,0,5);
		p.ID = temp;
		//feibie
		System.arraycopy(buf,27,temp,0,10);
		p.feibie = temp;
		// address
		System.arraycopy(buf,37,temp,0,100);
		p.address = temp;
		//content
		System.arraycopy(buf,137,temp,0,800);
		p.content = temp;
		// shop
		System.arraycopy(buf,937,temp,0,12);
		p.shop = temp;
		//doctor
		System.arraycopy(buf,949,temp,0,12);
		p.doctor = temp;
		//pharmacist
		System.arraycopy(buf,961,temp,0,12);
		p.pharmacist = temp;
		//phone
		System.arraycopy(buf,973,temp,0,15);
		p.phone = temp;
		//selfreport
		System.arraycopy(buf,988,temp,0,50);
		p.selfreport = temp;
		//filename
		System.arraycopy(buf,1038,temp,0,100);
		p.filename = temp;
		//date
		System.arraycopy(buf,1138,temp,0,10);
		p.date = temp;
		//title
		System.arraycopy(buf,1148,temp,0,100);
		p.title = temp;
		//bianhao
		System.arraycopy(buf,1248,temp,0,100);
		p.bianhao = temp;

		return p;
	}
}
