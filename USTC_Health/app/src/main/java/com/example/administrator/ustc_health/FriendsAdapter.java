package com.example.administrator.ustc_health;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.ustc_health.R.id.friends_pic;

/**
 * Created by Administrator on 2015/10/25.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    List<FriendsBean> friendsBeans = new ArrayList<FriendsBean>();
    private Context context;

    public FriendsAdapter(Context context, List<FriendsBean> friendsBeans) {
        this.context = context;
        this.friendsBeans = friendsBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_friends_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
// 给ViewHolder设置元素
        FriendsBean p = friendsBeans.get(position);
        holder.mTextView.setText(p.name);
        //holder.mImageView.setImageDrawable();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, FriendsDetailActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsBeans == null ? 0 : friendsBeans.size();
    }


    // 重写的自定义ViewHolder
    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public final View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mTextView = (TextView) v.findViewById(R.id.friends_name);
            mImageView = (ImageView) v.findViewById(R.id.friends_pic);

        }
    }
}
