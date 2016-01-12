package com.example.ustc.healthreps.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.FirstClassAdapter;
import com.example.ustc.healthreps.adapter.SecondClassAdapter;
import com.example.ustc.healthreps.adapter.TabStoreAdapter;
import com.example.ustc.healthreps.citylist.CityList;
import com.example.ustc.healthreps.gps.CurLocation;
import com.example.ustc.healthreps.gps.GPSService;
import com.example.ustc.healthreps.model.FirstClassItem;
import com.example.ustc.healthreps.model.MedicStore;
import com.example.ustc.healthreps.model.SecondClassItem;

/*
 * 问药功能模块主界面
 * 2015/12/12
 * by hzy
 */
public class SearchMedicine extends Fragment {
    View view;
    MedicineList smfragment = null;
    private LocationClient locationClient = null;

    private PopupWindow popupWindow;
    ImageView imageView;
    Toolbar toolbar;
    TextView menu1,menu2,menu3;

    private TextView tvCity,mainTab1TV,mainTab2TV,mainTab3TV;
    /**左侧一级分类的数据*/
    private List<FirstClassItem> firstList1,firstList2;
    /**右侧二级分类的数据*/
    private List<SecondClassItem> secondList1,secondList2;
    /**使用PopupWindow显示一级分类和二级分类*/
    private PopupWindow popupWindow1,popupWindow2,popupWindow3;

    /**左侧和右侧两个ListView*/
    private ListView leftLV1, rightLV1,leftLV2, rightLV2;

    //药店信息列表
    private ListView store_list_view;

    private TabStoreAdapter store_Adapter;

    List <MedicStore> list = new ArrayList <MedicStore>();
    //弹出PopupWindow时背景变暗
    private View darkView;

    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;

    //弹出PopupWindow时，箭头方向
    private boolean flag1=true,flag2=true,flag3=true;
    private LinearLayout layout;
    private ListView listView;

    //智能排序
    private TextView smart_sort,nearest_sort,hot_sort,best_sort;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.wenyao_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initview();
        initData();
        initPopup();
        //定位
        location();

        OnClickListenerImpl l = new OnClickListenerImpl();
        mainTab1TV.setOnClickListener(l);
        mainTab2TV.setOnClickListener(l);
        mainTab3TV.setOnClickListener(l);
    }

    //Toolbar
    LinearLayout layout_toolbar;
    public void initToolbar() {
        layout_toolbar = (LinearLayout) view.findViewById(R.id.ly_loc);
        layout_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CityList.class);
                startActivity(intent);
            }
        });
        toolbar = (Toolbar) view.findViewById(R.id.id_toolbar);

        imageView = (ImageView) view.findViewById(R.id.iv_toolbar_set);
        imageView.setOnClickListener(new View.OnClickListener() {
            boolean up = false;

            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater;
                View popup;
                layoutInflater = LayoutInflater.from(getActivity());
                popup = layoutInflater.inflate(R.layout.activity_toolbar_menu, null);
                popupWindow = new PopupWindow(popup, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, false);
                if (!up) {
                    popupWindow.setBackgroundDrawable(new ColorDrawable(0));

                    menu1 = (TextView) popup.findViewById(R.id.tv_menu_01);
                    menu1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "私人医生...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    menu2 = (TextView) popup.findViewById(R.id.tv_menu_02);
                    menu2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity().getApplicationContext(), "专属药店...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    menu3 = (TextView) popup.findViewById(R.id.tv_menu_03);
                    menu3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "个人设置...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    popupWindow.showAsDropDown(imageView);
                    popupWindow.update();
                    up = true;
                } else {
                    popupWindow.dismiss();
                    up = false;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null)
            switch (resultCode) {
                case 2:
                    tvCity.setText(data.getStringExtra("city"));
                    break;

                default:
                    break;
            }
    }

    //定位
    public void location(){
        GPSService mGPSService = new GPSService(getActivity());

        if(CurLocation.latitude != 0 && CurLocation.lontitude != 0)
            Toast.makeText(getActivity(), "Latitude:" + CurLocation.latitude + " | Longitude: " + CurLocation.lontitude, Toast.LENGTH_LONG).show();

        if(CurLocation.city!=null){
            tvCity.setText(CurLocation.city);
        }
        else {
            loadLocation();
        }
        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
    }

    /**
     * 获取位置
     */
    public void loadLocation() {
        if (locationClient == null) {
            locationClient = new LocationClient(getActivity());
            locationClient.registerLocationListener(new LocationListenner());
            LocationClientOption option = new LocationClientOption();
            option.setAddrType("all");
            option.setOpenGps(true);
            option.setCoorType("bd09ll");
            option.setScanSpan(2000);
            locationClient.setLocOption(option);
        }

        locationClient.start();
        locationClient.requestLocation();
    }

    /**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    private class LocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {

                if (location.getCity() != null
                        && !location.getCity().equals("")) {
                    locationClient.stop();
                    //城市
                    CurLocation.city = location.getCity();
                    tvCity.setText(CurLocation.city);

                    //经度
                    CurLocation.lontitude = location.getLongitude();
                    //纬度
                    CurLocation.latitude = location.getLatitude();

                    Toast.makeText(getActivity(), "---Latitude:" + CurLocation.latitude + " | Longitude: " + CurLocation.lontitude, Toast.LENGTH_LONG).show();

                } else {

                }
            } else {
                tvCity.setText("定位失败");
                tvCity.setTextColor(Color.RED);
            }

        }
    }
        //点击事件
    class OnClickListenerImpl implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fujin:
                    tab1OnClick();
                    break;
                case R.id.allStore:
                    tab2OnClick();
                    break;
                case R.id.smartSort:
                    tab3OnClick();
                    break;
                default:
                    break;
            }
        }

        private void tab3OnClick() {
                // TODO Auto-generated method stub
                if(flag3){
                    Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_up_black);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    mainTab3TV.setCompoundDrawables(null, null, nav_up, null);
                }
                else{
                    Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    mainTab3TV.setCompoundDrawables(null, null, nav_up, null);
                }

                //药店关键字过滤信息
                final String title[] = {"智能排序","离我最近","人气最高","评价最好"};
                //加载布局
                layout = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.more_setting_dialog, null);
                //找到布局控件
                listView = (ListView)layout.findViewById(R.id.lv_dialog);
                //设置适配器
                listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                        R.layout.more_text_setting,R.id.tv_text,title));
                //实例化popupwindow
                popupWindow3 = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //控制键盘是否可以获得焦点
                popupWindow3.setFocusable(true);
                popupWindow3.setAnimationStyle(R.style.mystyle);
                popupWindow3.setBackgroundDrawable(new BitmapDrawable(null, ""));
                WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);

                //获取xoff
                int xpos = manager.getDefaultDisplay().getWidth()/2-popupWindow3.getWidth()/2;
                //xoff,yoff基于anchor的左下角进行偏移
                popupWindow3.showAsDropDown(mainTab3TV, xpos, 0);
                //监听
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = title[position];
                        popupWindow3.dismiss();
                        mainTab3TV.setText(str);

                        //指示图标归位
                        Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        mainTab3TV.setCompoundDrawables(null, null, nav_up, null);

                        Toast.makeText(getActivity().getApplication(), str, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //药店信息过滤
            private void tab2OnClick() {

                if(flag2){
                    Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_up_black);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    mainTab2TV.setCompoundDrawables(null, null, nav_up, null);
                }
                else{
                    Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    mainTab2TV.setCompoundDrawables(null, null, nav_up, null);
                }

                //药店关键字过滤信息
                final String title[] = {"全部药店","医保药店","中药店","24h药店"};
                //加载布局
                layout = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.more_setting_dialog, null);
                //找到布局控件
                listView = (ListView)layout.findViewById(R.id.lv_dialog);
                //设置适配器
                listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                        R.layout.more_text_setting,R.id.tv_text,title));
                //实例化popupwindow
                popupWindow2 = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //控制键盘是否可以获得焦点
                popupWindow2.setFocusable(true);
                popupWindow2.setAnimationStyle(R.style.mystyle);
                popupWindow2.setBackgroundDrawable(new BitmapDrawable(null, ""));
                WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);

                //获取xoff
                int xpos = manager.getDefaultDisplay().getWidth()/2-popupWindow2.getWidth()/2;
                //xoff,yoff基于anchor的左下角进行偏移
                popupWindow2.showAsDropDown(mainTab2TV, xpos, 0);
                //监听
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = title[position];
                        popupWindow2.dismiss();
                        mainTab2TV.setText(str);
                        //指示图标归位
                        Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        mainTab2TV.setCompoundDrawables(null, null, nav_up, null);
                        Toast.makeText(getActivity().getApplication(), str, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        private void tab1OnClick() {
            // TODO Auto-generated method stub
            if (popupWindow1.isShowing()) {
                popupWindow1.dismiss();
            } else {
                Drawable nav_up = null;

                if(flag1)
                {
                    nav_up=getResources().getDrawable(R.drawable.ic_arrow_up_black);
                    flag1=false;
                }
                else
                {
                    nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                    flag1=true;
                }
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                mainTab1TV.setCompoundDrawables(null, null, nav_up, null);

                popupWindow1.showAsDropDown(view.findViewById(R.id.main_div_line));
                popupWindow1.setAnimationStyle(-1);
                //背景变暗
                darkView.startAnimation(animIn);
                darkView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initPopup() {
        // TODO Auto-generated method stub

        //popupWindow1为“附近”信息
        popupWindow1 = new PopupWindow(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_layout, null);
        leftLV1 = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV1 = (ListView) view.findViewById(R.id.pop_listview_right);

        popupWindow1.setContentView(view);
        popupWindow1.setBackgroundDrawable(new PaintDrawable());
        popupWindow1.setFocusable(true);

        popupWindow1.setHeight(ScreenUtils.getScreenH(getActivity()) * 2 / 3);
        popupWindow1.setWidth(ScreenUtils.getScreenW(getActivity()));

        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);

                leftLV1.setSelection(0);
                rightLV1.setSelection(0);
            }
        });
        //加载一级分类
        final FirstClassAdapter firstAdapter1 = new FirstClassAdapter(getActivity(), firstList1);
        leftLV1.setAdapter(firstAdapter1);

        //加载左侧第一行对应右侧二级分类
        secondList1 = new ArrayList<SecondClassItem>();
        secondList1.addAll(firstList1.get(0).getSecondList());
        final SecondClassAdapter secondAdapter1 = new SecondClassAdapter(getActivity(), secondList1);
        rightLV1.setAdapter(secondAdapter1);

        //左侧ListView点击事件
        leftLV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<SecondClassItem> list2 = firstList1.get(position).getSecondList();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    popupWindow1.dismiss();

                    int firstId = firstList1.get(position).getId();
                    String selectedName = firstList1.get(position).getName();
                    return;
                }

                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position){
                    return;
                }

                //根据左侧一级分类选中情况，更新背景色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();

                //显示右侧二级分类
                updateSecondListView1(list2, secondAdapter1);
            }
        });
        //右侧ListView点击事件
        rightLV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flag1=true;
                Drawable nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                mainTab1TV.setCompoundDrawables(null, null, nav_up, null);
                //关闭popupWindow，显示用户选择的分类
                popupWindow1.dismiss();

                int firstPosition = firstAdapter1.getSelectedPosition();
                int firstId = firstList1.get(firstPosition).getId();
                int secondId = firstList1.get(firstPosition).getSecondList().get(position).getId();
                String selectedName = firstList1.get(firstPosition).getSecondList().get(position)
                        .getName();
                handleResult(1,firstId, secondId,selectedName);
            }
        });


    }

    private void initData() {
        // TODO Auto-generated method stub
		/*
		 * 初始化附近城市信息，后期该信息从数据库中获取
		 */
        firstList1 = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> list11 = new ArrayList<SecondClassItem>();
        list11.add(new SecondClassItem(111, "附近"));
        list11.add(new SecondClassItem(112, "500米"));
        list11.add(new SecondClassItem(113, "1000米"));
        list11.add(new SecondClassItem(114, ">1000米"));
        firstList1.add(new FirstClassItem(1, "附近", list11));
        //2
        ArrayList<SecondClassItem> list12 = new ArrayList<SecondClassItem>();
        list12.add(new SecondClassItem(121, "左岸商业街"));
        list12.add(new SecondClassItem(122, "博览中心"));
        list12.add(new SecondClassItem(123, "娄葑镇"));
        list12.add(new SecondClassItem(124, "湖东邻里中心"));
        list12.add(new SecondClassItem(125, "李公堤"));
        list12.add(new SecondClassItem(126, "独墅湖大学城"));
        list12.add(new SecondClassItem(127, "跨塘"));
        list12.add(new SecondClassItem(128, "斜塘老街"));
        list12.add(new SecondClassItem(129, "唯亭"));
        list12.add(new SecondClassItem(1210, "胜浦"));
        list12.add(new SecondClassItem(1211, "车坊"));
        firstList1.add(new FirstClassItem(2, "工业园区", list12));
        //3
        ArrayList<SecondClassItem> list13 = new ArrayList<SecondClassItem>();
        list13.add(new SecondClassItem(131, "长桥地区"));
        list13.add(new SecondClassItem(132, "吴中地区"));
        list13.add(new SecondClassItem(133, "木渎地区"));
        list13.add(new SecondClassItem(134, "越溪"));
        list13.add(new SecondClassItem(135, "永旺梦乐城"));
        list13.add(new SecondClassItem(136, "太湖东山"));
        list13.add(new SecondClassItem(137, "太湖西山"));
        firstList1.add(new FirstClassItem(3, "吴中区", list13));
        //4
        ArrayList<SecondClassItem> list14 = new ArrayList<SecondClassItem>();
        list14.add(new SecondClassItem(141, "长桥地区"));
        list14.add(new SecondClassItem(142, "石路地区"));
        list14.add(new SecondClassItem(143, "留园"));
        list14.add(new SecondClassItem(144, "三香路沿线"));
        list14.add(new SecondClassItem(145, "山塘街"));
        list14.add(new SecondClassItem(146, "虎丘景区"));
        firstList1.add(new FirstClassItem(4, "金阊区", list14));

        //5
        ArrayList<SecondClassItem> list15 = new ArrayList<SecondClassItem>();
        list15.add(new SecondClassItem(151, "观前街地区"));
        list15.add(new SecondClassItem(152, "拙政园"));
        list15.add(new SecondClassItem(153, "景德路沿线"));
        list15.add(new SecondClassItem(154, "平江路"));
        list15.add(new SecondClassItem(155, "万达广场"));
        firstList1.add(new FirstClassItem(5, "平江区", list15));

        //6
        ArrayList<SecondClassItem> list16 = new ArrayList<SecondClassItem>();
        list16.add(new SecondClassItem(161, "十全街/凤凰街"));
        list16.add(new SecondClassItem(162, "南门"));
        list16.add(new SecondClassItem(163, "盘门"));
        list16.add(new SecondClassItem(164, "沧浪新城"));
        list16.add(new SecondClassItem(165, "宝带东路"));
        firstList1.add(new FirstClassItem(6, "沧浪区", list16));

        //copy
        firstList1.addAll(firstList1);


        //初始化药店信息
        List<MedicStore> list_store = new ArrayList<MedicStore>();
        list_store.add(new MedicStore("万寿堂大药房","中药店","2Km"));

        for(int i=0;i<list_store.size();i++)
        {
            MedicStore mstore = list_store.get(i);
            list.add(mstore);
        }

        store_Adapter = new TabStoreAdapter(getActivity(),list);
        store_list_view.setAdapter(store_Adapter);
        store_list_view.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                MedicStore mstore = (MedicStore)store_Adapter.getItem(position);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (fm.getFragments() != null && fm.getFragments().size() > 0) {
                    for (Fragment cf : fm.getFragments()) {
                        ft.hide(cf);
                    }
                }

                if(smfragment == null){
                    smfragment = new MedicineList();
                    Bundle bundle = new Bundle();
                    bundle.putString("store_name", mstore.getStoreName());
                    bundle.putString("store_category", mstore.getStoreCategroy());
                    smfragment.setArguments(bundle);
                    ft.replace(R.id.frame_content, smfragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else{
                    ft.show(smfragment);
                    ft.commit();
                }

            }
        });

    }

    private void initview() {
        tvCity = (TextView) view.findViewById(R.id.tv_curCity);
        mainTab1TV = (TextView) view.findViewById(R.id.fujin);
        mainTab2TV = (TextView) view.findViewById(R.id.allStore);
        mainTab3TV = (TextView) view.findViewById(R.id.smartSort);

        store_list_view = (ListView) view.findViewById(R.id.lv_store);
        darkView = view.findViewById(R.id.main_darkview);

        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);

        ViewGroup tableTitle = (ViewGroup) view.findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(65, 105, 225));
    }

    //刷新右侧ListView
    private void updateSecondListView1(List<SecondClassItem> list2,
                                       SecondClassAdapter secondAdapter) {
        secondList1.clear();
        secondList1.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }

    //处理点击结果
    private void handleResult(int flag,int firstId, int secondId,String selectedName){
        //String text = "first id:" + firstId + ",second id:" + secondId;
        String text = "你选择的是：" + selectedName;
        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        if(flag == 1)
            mainTab1TV.setText(selectedName);
        else if(flag == 2)
            mainTab2TV.setText(selectedName);
    }
}