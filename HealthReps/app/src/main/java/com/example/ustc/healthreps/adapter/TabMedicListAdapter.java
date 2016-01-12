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
import com.example.ustc.healthreps.model.Medicine;

import java.util.List;

/**
 * Created by hzy on 2016/1/12.
 */
public class TabMedicListAdapter extends BaseAdapter {
    private List<Medicine> list;
    private LayoutInflater inflater;

    public TabMedicListAdapter(Context context,List<Medicine> list){
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
        Medicine medicine = (Medicine) this.getItem(pos);
        ViewHolder viewHolder;
        if(view == null)
        {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.medic_list_header, null);
            viewHolder.mMedicName = (TextView)view.findViewById(R.id.medic_name);
            viewHolder.mMedicCategory = (TextView)view.findViewById(R.id.medic_behavior);
            viewHolder.mMedicNum = (TextView)view.findViewById(R.id.medic_num);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mMedicName.setText(medicine.getMedicName());
        viewHolder.mMedicCategory.setText(medicine.getMedicCategroy());
        viewHolder.mMedicNum.setText(medicine.getNum()+"");

        viewHolder.mMedicName.setTextColor(Color.BLACK);
        viewHolder.mMedicCategory.setTextColor(Color.BLACK);
        viewHolder.mMedicNum.setTextColor(Color.BLACK);

//        int size = (int) viewHolder.mMedicName.getTextSize() + 3;
//        viewHolder.mMedicName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
//        viewHolder.mMedicCategory.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
//        viewHolder.mMedicNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        return view;
    }

    public static class ViewHolder{
        public TextView mMedicName;
        public TextView mMedicCategory;
        public TextView mMedicNum;
    }
}
