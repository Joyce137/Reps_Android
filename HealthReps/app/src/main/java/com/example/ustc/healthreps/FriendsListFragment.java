package com.example.ustc.healthreps;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/25.
 */
public class FriendsListFragment extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public  List<FriendsBean> friendsBeans = new ArrayList<FriendsBean>();
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_friends_list, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.friends_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       for (int i=0;i<50;i++)
       {
           friendsBeans.add(new FriendsBean("APP"+i,"Hao"+i));
       }
        adapter=new FriendsAdapter(getActivity(),friendsBeans);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
