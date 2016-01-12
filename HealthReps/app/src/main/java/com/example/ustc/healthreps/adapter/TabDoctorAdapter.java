package com.example.ustc.healthreps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Doctor;

import java.util.List;

/**
 * 寻医之医生管理listview适配器
 * Created by hzy on 2016/1/9.
 */
public class TabDoctorAdapter extends BaseAdapter{
    private List<Doctor> list;
    private LayoutInflater inflater;

    public TabDoctorAdapter(Context context,List<Doctor> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        Doctor doctor = (Doctor) this.getItem(pos);
        ViewHolder viewHolder;
        if(view == null)
        {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.doc_info_header, null);
            viewHolder.mDocName = (TextView)view.findViewById(R.id.doc_name);
            viewHolder.mDocDep = (TextView)view.findViewById(R.id.doc_dep);
            viewHolder.mDocGrade = (TextView)view.findViewById(R.id.doc_grade);
            viewHolder.mDochosname = (TextView)view.findViewById(R.id.doc_hosName);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mDocName.setText(doctor.getDoctorName());
        viewHolder.mDocDep.setText(doctor.getDepartmentName());
        viewHolder.mDocGrade.setText(doctor.getGradeName());
        viewHolder.mDochosname.setText(doctor.getHospitalName());

        viewHolder.mDocName.setTextColor(Color.BLACK);
        viewHolder.mDocDep.setTextColor(Color.BLACK);
        viewHolder.mDocGrade.setTextColor(Color.BLACK);
        viewHolder.mDochosname.setTextColor(Color.BLACK);

        int size = (int) viewHolder.mDocName.getTextSize() + 3;
        viewHolder.mDocName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        viewHolder.mDocDep.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        viewHolder.mDocGrade.setTextSize(TypedValue.COMPLEX_UNIT_PX, size-1);
        viewHolder.mDochosname.setTextSize(TypedValue.COMPLEX_UNIT_PX, size-1);

        return view;
    }

    public static class ViewHolder{
        public TextView mDocName;
        public TextView mDocDep;
        public TextView mDocGrade;
        public TextView mDochosname;
    }
}
