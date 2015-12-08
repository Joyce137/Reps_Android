package com.example.ustc.healthreps.patient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ustc.healthreps.R;

public class DoctorList extends Activity implements OnItemClickListener {

	private ListView d_list_view;
	private ArrayAdapter<Doctor> adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_doctorlist);
		adapter = new ArrayAdapter<Doctor>(this,
				android.R.layout.simple_list_item_1);

		d_list_view = (ListView) findViewById(R.id.DoctorlistView);
		d_list_view.setAdapter(adapter);

		adapter.add(new Doctor("李医生", "内科", "副主任医师", "协和医院"));
		adapter.add(new Doctor("张医生", "内科", "副主任医师", "第一医院"));
		adapter.add(new Doctor("韩医生", "内科", "副主任医师", "协和医院"));
		adapter.add(new Doctor("王医生", "内科", "副主任医师", "协和医院"));
		adapter.add(new Doctor("赵医师", "内科", "副主任医师", "协和医院"));
		adapter.add(new Doctor("安医生", "内科", "副主任医师", "协和医院"));

		d_list_view.setOnItemClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Doctor doctor = adapter.getItem(position);

		Intent i = new Intent();
		i.setComponent(new ComponentName("com.joyce.reps",
				"com.joyce.reps.patient.DoctorSessionAty"));
		i.putExtra("doctor_name", doctor.getDoctorName());
		i.putExtra("grade_name", doctor.getGradeName());
		startActivity(i);
	}

}
