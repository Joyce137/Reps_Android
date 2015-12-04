package com.example.ustc.healthreps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HBL on 2015/11/21.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    List<DeviceBean> devicesBeans = new ArrayList<DeviceBean>();
    private Context context;

    public DeviceAdapter(Context context, List<DeviceBean> devicesBeans) {
        this.context = context;
        this.devicesBeans = devicesBeans;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_scan_device_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DeviceBean p = devicesBeans.get(position);
        holder.nameText.setText(p.getName());
        holder.addressText.setText(p.getAddress());
        //holder.mImageView.setImageDrawable();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Context context = v.getContext();
                Intent intent = new Intent(context, FriendsDetailActivity.class);
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return devicesBeans == null ? 0 : devicesBeans.size();
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView addressText;
        public final View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            nameText = (TextView) v.findViewById(R.id.scan_device_name);
            addressText=(TextView)v.findViewById(R.id.scan_device_address);


        }
    }
}
