package com.example.ustc.healthreps.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.serverInterface.PreList;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
 * 完整的清单信息界面
 * 2015/12/13
 * hzy
 */
public class RecordDetail extends Activity {
	
	public TextView patient,doctor,content,address,store,postContent,datatime,status;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record_detail);
		
		init();
	}

	private void init() {
		patient = (TextView)findViewById(R.id.patient);
		doctor = (TextView)findViewById(R.id.doctor);
		content = (TextView)findViewById(R.id.content);
		address = (TextView)findViewById(R.id.address);
		store = (TextView)findViewById(R.id.store);
		postContent = (TextView)findViewById(R.id.postContent);
		datatime = (TextView)findViewById(R.id.dataTime);
		status = (TextView)findViewById(R.id.status);

		Intent intent = getIntent();
		PreList preList = (PreList)intent.getSerializableExtra("prelist");
		try {
			patient.setText(new String(preList.patient,"GBK"));
			doctor.setText(new String(preList.doctor,"GBK"));
			address.setText(new String(preList.address,"GBK"));
			store.setText(new String(preList.shop,"GBK"));
			postContent.setText(new String(preList.selfreport,"GBK"));
			datatime.setText(new String(preList.date,"GBK")+"-"+preList.prelistName.time);
			status.setText(Utils.getStatus(preList.status));
			String contentStr = "";
			List<Medicine> medicines = preList.prelistContent.medicines;

			for(Medicine medicine:medicines){
				contentStr = contentStr + medicine.name+","+ medicine.category+","+ medicine.num
						+","+ medicine.unit+","+ medicine.disease;
				contentStr+="\n";
			}
			content.setText(contentStr);
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}

	}
}
