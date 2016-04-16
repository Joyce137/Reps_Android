package com.example.ustc.healthreps.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.FirstClassAdapter;
import com.example.ustc.healthreps.adapter.SecondClassAdapter;
import com.example.ustc.healthreps.adapter.TabDoctorAdapter;
import com.example.ustc.healthreps.gps.CurLocation;
import com.example.ustc.healthreps.gps.GPSService;
import com.example.ustc.healthreps.citylist.CityList;
import com.example.ustc.healthreps.model.Doctor;
import com.example.ustc.healthreps.model.FirstClassItem;
import com.example.ustc.healthreps.model.SecondClassItem;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.repo.DocPhaRepo;
import com.example.ustc.healthreps.serverInterface.SearchUser;
import com.example.ustc.healthreps.serverInterface.SearchUserInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.utils.Utils;
import com.example.ustc.healthreps.video.androiddecoder_log.Integration;

/*
 * 2015/12/07
 * 寻医功能界面
 * by hzy
 */
public class SearchDoctor extends Fragment {

    //接收处理结果
    public static Handler sDoctorResultHandler = null;

    View view;
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


    //医生信息列表
    private ListView doc_list_view;
    private TabDoctorAdapter doc_Adapter;

    List <Doctor> list = new ArrayList <Doctor>();
    //弹出PopupWindow时背景变暗
    private View darkView;

    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;

    //弹出PopupWindow时，箭头方向
    private boolean flag1=true,flag2=true,flag3=true;

    //智能排序
    private TextView smart_sort,nearest_sort,hot_sort,best_sort;

    private LinearLayout layout;
    private ListView listView;

    private Doctor curDoctor;

    private String district = "附近", address = "", hospitalOrkeshiName = "医院";
    public static Handler sDocHandler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.xunyi_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        findView();

        sDocHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                SearchUserInfo result = (SearchUserInfo)msg.obj;
                insertIntoList(result);
            }
        };

        sDoctorResultHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String data = (String) msg.obj;
                onRecvDoctorResult(data);
            }
        };

        initData();
        initPopup();
        //定位
        location();

        OnClickListenerImpl l = new OnClickListenerImpl();
        mainTab1TV.setOnClickListener(l);
        mainTab2TV.setOnClickListener(l);
        mainTab3TV.setOnClickListener(l);
    }

    //将得到的结果插入到list中
    public void insertIntoList(SearchUserInfo searchUserInfo){
        //显示在列表中的实体
        String doctorName = "";
        String departmentName = "";
        String gradeName = "";
        String hospitalName = "";
        String loginName = "";

        try {
            doctorName = new String(searchUserInfo.realName,"GBK").trim();
            departmentName = new String(searchUserInfo.keshi,"GBK").trim();
            gradeName = new String(searchUserInfo.zhicheng,"GBK").trim();
            hospitalName = new String(searchUserInfo.companyName,"GBK").trim();
            loginName = new String(searchUserInfo.loginName,"GBK").trim();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        if(doctorName.length() == 0)
            doctorName = "test";
        if(departmentName.length() == 0)
            departmentName = "test";
        if(gradeName.length() == 0)
            gradeName = "test";
        if(hospitalName.length() == 0)
            hospitalName = "test";
        if(loginName.length() == 0)
            loginName = "doctor1";

        Doctor doctor = new Doctor(doctorName,departmentName,gradeName,hospitalName);
        doctor.loginName = loginName;
//        if(searchUserInfo.rank == 0)
//            list.clear();
        list.add(doctor);
        doc_Adapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
    }

    //收到处理结果
    public void onRecvDoctorResult(String data){

        if(data.startsWith("y")) {
            Toast.makeText(getActivity(),data.substring(1),Toast.LENGTH_SHORT).show();
        }
        else if(data.startsWith("n")){
            Toast.makeText(getActivity(),data.substring(1),Toast.LENGTH_SHORT).show();
        }

        else if(data.startsWith("c")){
            Toast.makeText(getActivity(),data.substring(1),Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(getActivity().getApplicationContext(), DoctorSessionAty.class);
//            i.putExtra("doctor_name", curDoctor.getDoctorName());
//            i.putExtra("grade_name", curDoctor.getGradeName());
//            startActivity(i);

            Intent intent = new Intent(getActivity().getApplicationContext(), Integration.class);
            String a = Users.sLoginUsername;
            String b = curDoctor.getDoctorName();

            if(a==null||b==null){
                Toast.makeText(getActivity().getApplicationContext(),"名字不能为空",Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("mName",a);
            bundle.putString("yName",b);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    //Toolbar
    LinearLayout layout_popup;
    private String title[] = {"搜索设备","专属药店","私人医生","添加好友","个人设置"};
    private ListView listView_popup;
    public void showPopupWindow(View parent){
        //加载布局
        layout_popup = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.more_setting_dialog, null);
        //找到布局控件
        listView_popup = (ListView)layout_popup.findViewById(R.id.lv_dialog);
        //设置适配器
        listView_popup.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.more_text_setting, R.id.tv_text, title));
        //实例化popupwindow
        popupWindow = new PopupWindow(layout_popup, 350,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);

        //popupWindow = new PopupWindow(layout,370,570);
        //控制键盘是否可以获得焦点
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.mystyle);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);

        //获取xoff
        int xpos = manager.getDefaultDisplay().getWidth()/2-popupWindow.getWidth()/2;
        //xoff,yoff基于anchor的左下角进行偏移
        popupWindow.showAsDropDown(parent, xpos, 0);
        //监听
        listView_popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str = null;

                popupWindow.dismiss();
                switch (position) {
                    case 0:
                        str = "搜索设备";
                        //不是我们组的功能^-^
                        break;
                    case 1:
                        str = "专属药店";
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        if (fm.getFragments() != null && fm.getFragments().size() > 0) {
                            for (Fragment cf : fm.getFragments()) {
                                ft.remove(cf);
                            }
                        }
                        SearchMedicine smfragment = new SearchMedicine();
                        Bundle bundle = new Bundle();
                        bundle.putInt("flag", 1);   //flag=1,表示对SearchMedic来说已经设置过默认药店
                        smfragment.setArguments(bundle);
                        ft.replace(R.id.frame_content, smfragment);
                        ft.addToBackStack(null);
                        ft.commit();

                        Users.sDefaultStore = "store1";
                        Sockets.socket_center.sendBindStoreDoctorInfo(true);

                        break;
                    case 2:
                        str = "私人医生";
                        Users.sDefaultDoctor = "doctor1";
                        Sockets.socket_center.sendBindStoreDoctorInfo(false);
                        break;
                    case 3:
                        str = "添加好友";
                        //不是我们组的功能^-^
                        break;
                    case 4:
                        str = "个人设置";
                        Intent intent4 = new Intent(view.getContext(), PersonSetting.class);
                        view.getContext().startActivity(intent4);
                        break;
                }

                Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void initToolbar(){
        layout_popup=(LinearLayout)view.findViewById(R.id.ly_loc);
        layout_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CityList.class);
                startActivityForResult(intent, 1);
            }
        });
        toolbar=(Toolbar)view.findViewById(R.id.id_toolbar) ;

//        imageView=(ImageView)view.findViewById(R.id.iv_toolbar_set) ;
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupWindow(imageView);
//            }
//        });
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
            if(CurLocation.city.endsWith("市"))
                CurLocation.city = CurLocation.city.substring(0,CurLocation.city.length()-1);
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
                case R.id.doctor:
                    tab2OnClick();
                    break;
                case R.id.sort:
                    tab3OnClick();
                    break;
                default:
                    break;
            }
        }

        private void tab3OnClick() {
            Drawable nav_up = null;

            if(flag3)
            {
                nav_up=getResources().getDrawable(R.drawable.ic_arrow_up_black);
                flag3=false;
            }
            else
            {
                nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                flag3=true;
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
                    Toast.makeText(getActivity().getApplication(), str, Toast.LENGTH_SHORT).show();
                    handleResult(3, 0, 0, str);
                }
            });

        }

        private void tab2OnClick() {
            if (popupWindow2.isShowing()) {
                popupWindow2.dismiss();
            }
            else {
                Drawable nav_up = null;

                if(flag2)
                {
                    nav_up=getResources().getDrawable(R.drawable.ic_arrow_up_black);
                    flag2=false;
                }
                else
                {
                    nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                    flag2=true;
                }
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                mainTab2TV.setCompoundDrawables(null, null, nav_up, null);

                popupWindow2.showAsDropDown(view.findViewById(R.id.main_div_line));
                popupWindow2.setAnimationStyle(-1);
                //背景变暗
                darkView.startAnimation(animIn);
                darkView.setVisibility(View.VISIBLE);
            }
        }

        private void tab1OnClick() {
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

        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
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
                //设置选择
                district = firstList1.get(position).getName();

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

                address = selectedName;
                handleResult(1,firstId, secondId,selectedName);
            }
        });

        /*
         * ------------------------------------------------------------------------
         */
        popupWindow2 = new PopupWindow(getActivity());

        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.popup_layout, null);
        leftLV2 = (ListView) view2.findViewById(R.id.pop_listview_left);
        rightLV2 = (ListView) view2.findViewById(R.id.pop_listview_right);

        popupWindow2.setContentView(view2);
        popupWindow2.setBackgroundDrawable(new PaintDrawable());
        popupWindow2.setFocusable(true);

        popupWindow2.setHeight(ScreenUtils.getScreenH(getActivity()) * 2 / 3);
        popupWindow2.setWidth(ScreenUtils.getScreenW(getActivity()));

        popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);

                leftLV2.setSelection(0);
                rightLV2.setSelection(0);
            }
        });

        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FirstClassAdapter firstAdapter2 = new FirstClassAdapter(getActivity(), firstList2);
        leftLV2.setAdapter(firstAdapter2);

        //加载左侧第一行对应右侧二级分类
        secondList2 = new ArrayList<SecondClassItem>();
        secondList2.addAll(firstList2.get(0).getSecondList());
        final SecondClassAdapter secondAdapter2 = new SecondClassAdapter(getActivity(), secondList2);
        rightLV2.setAdapter(secondAdapter2);

        //左侧ListView点击事件
        leftLV2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<SecondClassItem> list2 = firstList2.get(position).getSecondList();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    popupWindow2.dismiss();

                    int firstId = firstList2.get(position).getId();
                    String selectedName = firstList2.get(position).getName();
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
                updateSecondListView2(list2, secondAdapter2);

                hospitalOrkeshiName = firstList2.get(position).getName();
            }
        });

        //右侧ListView点击事件
        rightLV2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flag2=true;
                Drawable nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                mainTab2TV.setCompoundDrawables(null, null, nav_up, null);
                //关闭popupWindow，显示用户选择的分类
                popupWindow2.dismiss();

                int firstPosition = firstAdapter2.getSelectedPosition();
                int firstId = firstList2.get(firstPosition).getId();
                int secondId = firstList2.get(firstPosition).getSecondList().get(position).getId();
                String selectedName = firstList2.get(firstPosition).getSecondList().get(position)
                        .getName();
                handleResult(2,firstId, secondId,selectedName);
            }
        });

        /*
         * --------------------------------------------------------------
         */
        popupWindow3 = new PopupWindow(getActivity());

        View view3 = LayoutInflater.from(getActivity()).inflate(R.layout.smart_sort, null);

        smart_sort = (TextView) view3.findViewById(R.id.tv_smart);
        nearest_sort = (TextView) view3.findViewById(R.id.tv_nearest);
        hot_sort = (TextView) view3.findViewById(R.id.tv_hotest);
        best_sort = (TextView) view3.findViewById(R.id.tv_best);

        popupWindow3.setContentView(view3);
        popupWindow3.setFocusable(true);

        popupWindow3.setHeight(ScreenUtils.getScreenH(getActivity())*5/18);
        popupWindow3.setWidth(ScreenUtils.getScreenW(getActivity()));

        popupWindow3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);
            }
        });

        OnClickListener listen = new ClickListener();
        smart_sort.setOnClickListener(listen);
        nearest_sort.setOnClickListener(listen);
        hot_sort.setOnClickListener(listen);
        best_sort.setOnClickListener(listen);

    }
    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String str="null";
            switch (view.getId()) {
                case R.id.tv_smart:
                    str = "智能排序";
                    break;
                case R.id.tv_nearest:
                    str = "离我最近";
                    break;
                case R.id.tv_hotest:
                    str = "人气最高";
                    break;
                case R.id.tv_best:
                    str = "评价最好";
                    break;
                default:
                    break;
            }
            Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            mainTab3TV.setText(str);
            popupWindow3.dismiss();
            Drawable nav_up = null;
            nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
            flag3=true;
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            mainTab3TV.setCompoundDrawables(null, null, nav_up, null);

            handleResult(3, 0, 0, str);
        }
    }

    private void initData() {
        firstList1 = new ArrayList<FirstClassItem>();
        //1
        final ArrayList<SecondClassItem> list11 = new ArrayList<SecondClassItem>();
        list11.add(new SecondClassItem(111, "附近"));
        list11.add(new SecondClassItem(112, "500千米"));
        list11.add(new SecondClassItem(113, "1000千米"));
        list11.add(new SecondClassItem(114, "2000千米"));
        list11.add(new SecondClassItem(114, "10000千米"));
        list11.add(new SecondClassItem(114, ">10000千米"));
        firstList1.add(new FirstClassItem(1, "附近", list11));
        //2
        ArrayList<SecondClassItem> list12 = new ArrayList<SecondClassItem>();
        list12.add(new SecondClassItem(121, "左岸商业街"));
        list12.add(new SecondClassItem(122, "博览中心"));
        list12.add(new SecondClassItem(123, "仁爱路"));
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



        /*
         * 医院信息来言初始化
         */
        firstList2 = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> list21 = new ArrayList<SecondClassItem>();
        list21.add(new SecondClassItem(211, "全部医院"));
        list21.add(new SecondClassItem(212, "独墅湖医院"));
        list21.add(new SecondClassItem(213, "协和医院"));
        list21.add(new SecondClassItem(214, "……"));
        list21.add(new SecondClassItem(215, "其他医院"));
        firstList2.add(new FirstClassItem(1, "医院", list21));

        ArrayList<SecondClassItem> list22 = new ArrayList<SecondClassItem>();
        list22.add(new SecondClassItem(221, "全部科室"));
        list22.add(new SecondClassItem(222, "内科"));
        list22.add(new SecondClassItem(223, "外科"));
        list22.add(new SecondClassItem(224, "肿瘤科"));
        list22.add(new SecondClassItem(225, "妇产科"));
        list22.add(new SecondClassItem(222, "眼科"));
        list22.add(new SecondClassItem(223, "耳鼻喉科"));
        list22.add(new SecondClassItem(224, "骨科"));
        list22.add(new SecondClassItem(224, "皮肤科"));
        list22.add(new SecondClassItem(225, "其他科室"));
        firstList2.add(new FirstClassItem(2, "科室", list22));

        firstList2.addAll(firstList2);


        //初始化医生信息，从服务器端获得
        SearchUser allUser = new SearchUser(Types.USER_TYPE_DOCTOR);
        Sockets.socket_center.sendSearchUser(allUser);
//        List <Doctor> list_d = new ArrayList <Doctor>();
//
//        list_d.add(new Doctor("李医生", "内科", "副主任医师", "协和医院"));
//        list_d.add(new Doctor("张医生", "内科", "副主任医师", "第一医院"));
//        list_d.add(new Doctor("韩医生", "内科", "副主任医师", "协和医院"));
//        list_d.add(new Doctor("王医生", "内科", "副主任医师", "协和医院"));
//        list_d.add(new Doctor("赵医生", "内科", "副主任医师", "协和医院"));
//        list_d.add(new Doctor("安医生", "内科", "副主任医师", "协和医院"));
//
//        for(int i=0;i<list_d.size();i++)
//        {
//            Doctor doc = list_d.get(i);
//            list.add(doc);
////			temp.add(doc);
//        }
        doc_Adapter = new TabDoctorAdapter(getActivity().getApplicationContext(),list);
        doc_list_view.setAdapter(doc_Adapter);

        doc_list_view.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                curDoctor = (Doctor) doc_Adapter.getItem(position);
                //建立连接
                //new DocPhaRepo().connectDoctor(curDoctor.getDoctorName());
                new DocPhaRepo().connectDoctor(curDoctor.loginName);
            }
        });

        doc_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String loginNamethis = list.get(position).loginName;
                Users.sDefaultDoctor = loginNamethis;
                Sockets.socket_center.sendBindStoreDoctorInfo(false);
                Toast.makeText(getActivity().getApplicationContext(),"私人医生已设为"+loginNamethis,Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }


    private void findView() {
        tvCity = (TextView) view.findViewById(R.id.tv_curCity);
        mainTab1TV = (TextView) view.findViewById(R.id.fujin);
        mainTab2TV = (TextView) view.findViewById(R.id.doctor);
        mainTab3TV = (TextView) view.findViewById(R.id.sort);

        doc_list_view = (ListView) view.findViewById(R.id.lv_doc);

        darkView = view.findViewById(R.id.main_darkview);

        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);

        ViewGroup tableTitle = (ViewGroup) view.findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.GREEN);
    }

    //刷新右侧ListView
    private void updateSecondListView1(List<SecondClassItem> list2,
                                       SecondClassAdapter secondAdapter) {
        secondList1.clear();
        secondList1.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }
    //刷新右侧ListView
    private void updateSecondListView2(List<SecondClassItem> list2,
                                       SecondClassAdapter secondAdapter) {
        secondList2.clear();
        secondList2.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }
    //处理点击结果
    private void handleResult(int flag,int firstId, int secondId,String selectedName){
        //String text = "first id:" + firstId + ",second id:" + secondId;
        String text = "你选择的是：" + selectedName;
        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        //清空列表
        list.clear();

        doc_Adapter = new TabDoctorAdapter(getActivity().getApplicationContext(),list);
        doc_list_view.setAdapter(doc_Adapter);

        if(flag == 1)
            mainTab1TV.setText(selectedName);
        else if(flag == 2)
            mainTab2TV.setText(selectedName);
        else if(flag == 3)
            mainTab3TV.setText(selectedName);

        SearchUser searchUser = new SearchUser();
        try {
            searchUser.type = Types.USER_TYPE_DOCTOR;
            //tab1Str
            String tab1Str = mainTab1TV.getText().toString().trim();
            if(tab1Str.equals("附近") || tab1Str.endsWith("千米")){
                if(tab1Str.equals("附近"))
                    searchUser.distance = 40000;
                else {
                    address = tab1Str.substring(0, selectedName.length() - 2);
                    if(address.startsWith(">"))
                        searchUser.distance = 40000;
                    else
                        searchUser.distance = Integer.parseInt(address);
                }
            }
            else {
                address = district + tab1Str;
                searchUser.district = address.getBytes("GBK");
            }

            //tab2Str
            String tab2Str = mainTab2TV.getText().toString().trim();
            if(hospitalOrkeshiName.equals("医院")){
                if(tab2Str.equals("全部医院") || tab2Str.equals("医院"))
                    tab2Str = "";
                searchUser.companyName = tab2Str.getBytes("GBK");
            }
            else {
                if(tab2Str.equals("全部科室"))
                    tab2Str = "";
                searchUser.keshi = tab2Str.getBytes("GBK");
            }

            //tab3Str
            String tab3Str = mainTab3TV.getText().toString().trim();
            searchUser.sortType = Utils.changeSortMethodToInt(tab3Str);

            Sockets.socket_center.sendSearchUser(searchUser);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
}