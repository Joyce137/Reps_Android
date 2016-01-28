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
import com.example.ustc.healthreps.model.MedicStore;

import java.util.List;

/**
 * Created by hzy on 2016/1/11.
 * 问药之药店信息管理adapter
 */
public class TabStoreAdapter extends BaseAdapter{
    private List<MedicStore> list;
    private LayoutInflater inflater;

    public TabStoreAdapter(Context context,List<MedicStore> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        MedicStore medicStore = (MedicStore) this.getItem(pos);
        ViewHolder viewHolder;
        if(view == null)
        {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.store_info_header, null);
            viewHolder.mStoreName = (TextView)view.findViewById(R.id.store_name);
            viewHolder.mStoreCategory = (TextView)view.findViewById(R.id.store_kind);
            viewHolder.mStoreZone = (TextView)view.findViewById(R.id.store_away);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mStoreName.setText(medicStore.getStoreName());
        viewHolder.mStoreCategory.setText(medicStore.getStoreCategroy());
        viewHolder.mStoreZone.setText(medicStore.getStroeZone());

        viewHolder.mStoreName.setTextColor(Color.BLACK);
        viewHolder.mStoreCategory.setTextColor(Color.BLACK);
        viewHolder.mStoreZone.setTextColor(Color.BLACK);
//
//        int size = (int) viewHolder.mStoreName.getTextSize() + 3;
//        viewHolder.mStoreName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
//        viewHolder.mStoreCategory.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
//        viewHolder.mStoreZone.setTextSize(TypedValue.COMPLEX_UNIT_PX, size-1);

        return view;
    }

    public static class ViewHolder{
        public TextView mStoreName;
        public TextView mStoreCategory;
        public TextView mStoreZone;
    }
}
