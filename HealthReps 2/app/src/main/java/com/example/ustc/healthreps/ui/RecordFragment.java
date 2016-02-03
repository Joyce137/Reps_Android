package com.example.ustc.healthreps.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.TabRecordAdapter;
import com.example.ustc.healthreps.model.FileRecord;

import java.util.ArrayList;
import java.util.List;

/*
 * 文件管理类中的记录类
 * 2015/12/12
 * hzy
 */
public class RecordFragment extends Fragment implements OnItemClickListener{
	
	private ListView r_list_view;
	private TabRecordAdapter adapter;
	//save all record message
	List<FileRecord> list = new ArrayList<FileRecord>();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_record, null);
        r_list_view = (ListView) view.findViewById(R.id.RecordlistView);
        r_list_view.setOnItemClickListener(this);
        
		ViewGroup tableTitle = (ViewGroup) view.findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(65, 105, 225));
        init();
        return view;
	}

	private void init() {
		List <FileRecord> list_record = new ArrayList <FileRecord>();
		list_record.add(new FileRecord("2015/12/12","万寿堂","王医生","状况良好","已看"));
		list_record.add(new FileRecord("2015/12/13","阳光药店","张医生","状况未知","已预约"));
        
        for(int i=0;i<list_record.size();i++){
        	FileRecord record = list_record.get(i);
        	list.add(record);
        }
        
        adapter = new TabRecordAdapter(this.getActivity(),list);
        r_list_view.setAdapter(adapter);
       
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		FileRecord record = (FileRecord) adapter.getItem(position);

		Intent i = new Intent(getActivity(),RecordDetail.class);
//		i.setComponent(new ComponentName("com.example.ui",
//				"com.example.ui.RecordDetail"));
		i.putExtra("record_time", record.getTime());
		i.putExtra("record_flag", record.getFlag());
		startActivity(i);
	}
}
