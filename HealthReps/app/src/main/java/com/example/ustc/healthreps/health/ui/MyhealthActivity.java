package com.example.ustc.healthreps.health.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.LoginActivity;
import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.ProFileActivity;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.database.impl.UserDaoImpl;
import com.example.ustc.healthreps.health.SleepMode;
import com.example.ustc.healthreps.health.ViewFragment.DayAmountFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.DayHeartFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.DayKcalFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.DaySleepmodeFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.DayStepFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.MonthHeartFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.MonthKcalFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.MonthSleepmodeFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.MonthStepFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.WeekHeartFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.WeekKcalFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.WeekSleepmodeFragActivity;
import com.example.ustc.healthreps.health.ViewFragment.WeekStepFragActivity;
import com.example.ustc.healthreps.health.baidumap.MapActivity;
import com.example.ustc.healthreps.health.ble.BluetoothLeService;
import com.example.ustc.healthreps.health.ble.DeviceControlService;
import com.example.ustc.healthreps.health.ble.SampleGattAttributes;
import com.example.ustc.healthreps.health.ble.ScanBleActivity;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.ui.PersonSetting;
import com.example.ustc.healthreps.ui.SearchMedicine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2015/10/21.
 */
public class MyhealthActivity extends Fragment {
    private final static String TAG = MyhealthActivity.class
            .getSimpleName();
    View view;
    ViewPager mViewPagerAbove, mViewPager;

    TabLayout tabLayout;
    static String str01 = "0", str02 = "0";
    FragmentManager fragmentManager;
    //数据库

    private Fragment dayFragment;
    private Fragment weekFragment;
    private Fragment monthFragment;

    private Fragment dayHeartFrag;
    private Fragment dayStepFrag;
    private Fragment dayKcalFrag;
    private Fragment dayAmountFrag;
    private Fragment daySleepmodeFrag;

    private Fragment weekHeartFrag;
    private Fragment weekStepFrag;
    private Fragment weekKcalFrag;
    private Fragment weekSleepmodeFrag;

    private Fragment monthHeartFrag;
    private Fragment monthStepFrag;
    private Fragment monthKcalFrag;
    private Fragment monthSleepmodeFrag;

    private SleepMode sleepMode;
    private Button mapButton, sleepmodeButton;
    private TextView dwmText;

    public final static String ACTION_VIEW_STEP = "com.example.bluetooth.le.ACTION_VIEW_STEP";
    public final static String ACTION_VIEW_HEART = "com.example.bluetooth.le.ACTION_VIEW_HEART";
    public final static String ACTION_VIEW_KCAL = "com.example.bluetooth.le.ACTION_VIEW_KCAL";
    public final static String ACTION_VIEW_AMOUNT = "com.example.bluetooth.le.ACTION_VIEW_AMOUNT";

    public final static String ACTION_VIEW_LINE_HEART = "com.example.bluetooth.le.ACTION_VIEW_LINE_HEART";
    public final static String ACTION_VIEW_COLUMN_HEART = "com.example.bluetooth.le.ACTION_VIEW_LINE_COLUMN_HEART";
    public final static String ACTION_VIEW_BUBBLE_HEART = "com.example.bluetooth.le.ACTION_VIEW_BUBBLE_HEART";
    public final static String ACTION_VIEW_SLEEPMODEDATA = "com.example.bluetooth.le.ACTION_VIEW_SLEEPMODEDATA";

    public final static String ACTION_VIEW_SLIDELINE_HEART = "com.example.bluetooth.le.ACTION_VIEW_SLIDELINE_HEART";
    public final static String ACTION_VIEW_SLIDECOLUMN_HEART = "com.example.bluetooth.le.ACTION_VIEW_SLIDECOLUMN_HEART";


    private static TextView tv_toolbar_state;
    CircleImageView myImage;
    static ImageView iv_toolbar_refresh;
    static BluetoothLeService mBluetoothLeService ;
    public static Context context;
    private static String mydate;
    private static int count;
    TextView myNameTV;
    PopupWindow popupWindow;
    ImageView imageView;
    LinearLayout layout_popup;
    String title[] = {"搜索设备","个人设置","注销登录","退出"};
    ListView listView_popup;
    String dwm[] = {"日", "周", "月"};
    private int dwm_i = 0;

    //手势滑动
    private FragmentAdapter adapter_above;
    public static int mode = 1;
    public static int curPosition = 0;
    public static List<Fragment> fragments1;
    public static List<Fragment> fragments2;
    public static List<Fragment> fragments3;
    public static List<Fragment> fragments4;

    /**定义手势检测实例*/
    public static GestureDetector detector;
    /**做标签，记录当前是哪个fragment*/
    public int MARK=0;
    /**定义手势两点之间的最小距离*/
    final int DISTANT=50;

    public void setFragment(){
        fragments1 = new ArrayList<>();
        dayHeartFrag = new DayHeartFragActivity();
        weekHeartFrag = new WeekHeartFragActivity();
        monthHeartFrag = new MonthHeartFragActivity();
        fragments1.add(dayHeartFrag);
        fragments1.add(weekHeartFrag);
        fragments1.add(monthHeartFrag);

        fragments2 = new ArrayList<>();
        daySleepmodeFrag = new DaySleepmodeFragActivity();
        weekSleepmodeFrag = new WeekSleepmodeFragActivity();
        monthSleepmodeFrag = new MonthSleepmodeFragActivity();
        fragments2.add(daySleepmodeFrag);
        fragments2.add(weekSleepmodeFrag);
        fragments2.add(monthSleepmodeFrag);

        fragments3 = new ArrayList<>();
        dayKcalFrag = new DayKcalFragActivity();
        weekKcalFrag = new WeekKcalFragActivity();
        monthKcalFrag = new MonthKcalFragActivity();
        fragments3.add(dayKcalFrag);
        fragments3.add(weekKcalFrag);
        fragments3.add(monthKcalFrag);

        fragments4 = new ArrayList<>();
        dayStepFrag = new DayStepFragActivity();
        weekStepFrag = new WeekStepFragActivity();
        monthStepFrag = new MonthStepFragActivity();
        fragments4.add(dayStepFrag);
        fragments4.add(weekStepFrag);
        fragments4.add(monthStepFrag);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myhealth, container, false);
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        context = view.getContext();

        myNameTV = (TextView)view.findViewById(R.id.usernameText);
        if(Users.sLoginUsername != null)
            myNameTV.setText(Users.sLoginUsername);
        else
            myNameTV.setText("HealthReps");
//        myNameTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Users.sISDetailUserInfo) {
//                    //更新user数据库
//                    new UserDaoImpl(getActivity().getApplicationContext()).updateUserByUserInfo(Users.sLoginUser);
//
//                    Intent intent = new Intent(getActivity().getApplicationContext(), DetailUserInfoActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getActivity().getApplicationContext(), "正在搜索个人信息，请稍后", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        //顶层状态显示
        tv_toolbar_state = (TextView) view.findViewById(R.id.tv_toolbar_state);
        myImage = (CircleImageView) view.findViewById(R.id.iv_toolbar_my);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), ProFileActivity.class);
                startActivity(intent);
            }
        });
        iv_toolbar_refresh = (ImageView) view.findViewById(R.id.iv_toolbar_refresh);
        iv_toolbar_refresh.setVisibility(View.GONE);
        iv_toolbar_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//        imageView = (ImageView)view.findViewById(R.id.iv_toolbar_set) ;
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //加载布局
//                layout_popup = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.more_setting_dialog, null);
//                //找到布局控件
//                listView_popup = (ListView)layout_popup.findViewById(R.id.lv_dialog);
//                //设置适配器
//                listView_popup.setAdapter(new ArrayAdapter<String>(getActivity(),
//                        R.layout.more_text_setting, R.id.tv_text, title));
//                //实例化popupwindow
//                popupWindow = new PopupWindow(layout_popup, 350,
//                        ViewGroup.LayoutParams.WRAP_CONTENT, false);
//
//                //popupWindow = new PopupWindow(layout,370,570);
//                //控制键盘是否可以获得焦点
//                popupWindow.setFocusable(true);
//                popupWindow.setAnimationStyle(R.style.mystyle);
//                popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
//                WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
//
//                //获取xoff
//                int xpos = manager.getDefaultDisplay().getWidth()/2-popupWindow.getWidth()/2;
//                //xoff,yoff基于anchor的左下角进行偏移
//                popupWindow.showAsDropDown(imageView, xpos, 0);
//                //监听
//                listView_popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent intent = null;
//                        popupWindow.dismiss();
//                        switch (position) {
//                            case 0:
//                                //搜索设备
//                                intent = new Intent();
//                                intent.setClass(context, ScanBleActivity.class);
//                                startActivity(intent);
//                                break;
//                            case 1:
//                                //个人设置
//                                intent = new Intent();
//                                intent.setClass(context, BleSettingActivity.class);
//                                startActivity(intent);
//                                break;
//
//                            case 2:
//                                //注销登录
//                                Users.sISSignout = true;
//                                getActivity().finish();
//                                intent = new Intent();
//                                intent.setClass(context, LoginActivity.class);
//                                startActivity(intent);
//                                break;
//
//                            case 3:
//                                //退出
//                                AllThreads.sReceiveThread = null;
//                                AllThreads.sHeartBeatTask = null;
//                                AllThreads.sHeatBeatTimer = null;
//                                AllThreads.sSendFileThread = null;
////                AppManager.getInstance().exit();
//                                System.exit(0);
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                break;
//                        }
//                    }
//                });
//            }
//        });

        mapButton = (Button) view.findViewById(R.id.bt_myhealth_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MapActivity.class);
                startActivity(intent);
            }
        });

        //睡眠模式
        sleepMode = new SleepMode(context);
        sleepmodeButton = (Button) view.findViewById(R.id.bt_sleepmode);
        sleepmodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleepMode.testConnect();
//                testConnect();

                //当前为睡眠模式
                if (MainActivity.isSleepBMode) {
                    //关闭睡眠模式
//                    closeSleepMode();
                    sleepMode.closeSleepMode();
                    SleepMode.closeSleepModeSuccess = true;
                    MainActivity.isSleepBMode = false;
//                    sleepmodeButton.setText("开启睡眠模式");
                    sleepmodeButton.setBackground(getResources().getDrawable(R.drawable.sleep));
                }
                //当前非睡眠模式
                else {
                    //开启睡眠模式
//                    openSleepMode();
                    sleepMode.openSleepMode();
                    SleepMode.openSleepModeSuccess = true;
                    MainActivity.isSleepBMode = true;
//                    sleepmodeButton.setText("关闭睡眠模式");
                    sleepmodeButton.setBackground(getResources().getDrawable(R.drawable.sleep2));
                }

                //disconnect
                if(!SampleGattAttributes.connectedState){
                    DeviceControlService.mBluetoothLeService.disconnect();
                }
            }
        });


        dwmText = (TextView) view.findViewById(R.id.day_week_month);
        dwmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dwm_i = (++dwm_i)%3;
                dwmText.setText(dwm[dwm_i]);
                curPosition = dwm_i;
                mViewPagerAbove.setCurrentItem(curPosition);
            }
        });


        fragmentManager = getChildFragmentManager();
        mViewPagerAbove = (ViewPager) view.findViewById(R.id.myhealth_frame_content);
        setFragment();
        switch (mode){
            case 1:
                adapter_above = new FragmentAdapter(fragmentManager,fragments1);
                break;
            case 2:
                adapter_above = new FragmentAdapter(fragmentManager,fragments2);
                break;
            case 3:
                adapter_above = new FragmentAdapter(fragmentManager,fragments3);
                break;
            case 4:
                adapter_above = new FragmentAdapter(fragmentManager,fragments4);
                break;
        }

        mViewPagerAbove.setAdapter(adapter_above);
        mViewPagerAbove.setOnPageChangeListener(new FragmentChangeListener());


//        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        //查询新的ble数据

        dayHeartFrag = new DayHeartFragActivity();
//        fragmentManager = getChildFragmentManager();
//        // 开启事务
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.myhealth_frame_content, dayHeartFrag);
//        transaction.commit();
        //   heartrateDetail();
//        List<String> titles = new ArrayList<>();
//        titles.add("日");
//        titles.add("周");
//        titles.add("月");

//        tabLayout.addTab(tabLayout.newTab().setText(" "));
//        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
//        tabLayout.addTab(tabLayout.newTab().setText(" "));
//        List<Fragment> fragments = new ArrayList<>();
        dayFragment = DayInforFragment.newInstance();

        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.hsksView, dayFragment);
        transaction.commit();

//        weekFragment = WeekInforFragment.newInstance();
//        monthFragment = MonthInforFragment.newInstance();
//        fragments.add(dayFragment);
//        fragments.add(weekFragment);
//        fragments.add(monthFragment);
//        FragmentAdapter adapter =
//                new FragmentAdapter(getChildFragmentManager(), titles, fragments);
//        mViewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(mViewPager);
//        tabLayout.setTabsFromPagerAdapter(adapter);
        return view;
    }

    //service通知Activity扫描
    public static class MyBroadcastReceiver01 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Toast.makeText(context, "已经连接到设备", Toast.LENGTH_SHORT).show();
            tv_toolbar_state.setText("已连接");
            checkfirstrunaday();

//            //检查当前是否为睡眠模式
//            DeviceControlService.mBluetoothLeService.readMessage(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW));//

            iv_toolbar_refresh.setVisibility(View.GONE);
        }

    }

    public static void  checkfirstrunaday() {
        System.out.println("查询每天第一次");
        SharedPreferences sharedPreferences = context.getSharedPreferences("share", context.MODE_PRIVATE);
        String isFirstRunAday= sharedPreferences.getString("isFirstRunAday", null);
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        mydate=(""+mYear+mMonth+mDay);
        if (isFirstRunAday==null||!isFirstRunAday.equals(mydate)) {
            count=0;
            System.out.println("赋值");
            mBluetoothLeService = DeviceControlService.mBluetoothLeService;
            if (!mBluetoothLeService.initialize()) {//引用服务类的初始化
                return;
            }

            if(SampleGattAttributes.isinsetactivity){
                return;
            }
            if(SampleGattAttributes.connectedState)return;

            SampleGattAttributes.whichactivityconnect=true;//允许七天数据

            if (!mBluetoothLeService.connect(MainActivity.mDeviceAddress)) {
                ;
            } else {SampleGattAttributes.connectedState=true;
                //Toast.makeText(MainActivity.this, "已经连接", Toast.LENGTH_LONG).show();

            }
            //注册广播接收器

        }
        else {

            SampleGattAttributes.whichactivityconnect=false;
            Log.d(TAG, "不是today第一次运行！");
            //   Toast.makeText(MainActivity.this, "不是第一次运行！", Toast.LENGTH_LONG).show();
        }
    }
    public static class MyBroadcastReceiver02 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Toast.makeText(context, "已经连接到设备", Toast.LENGTH_SHORT).show();
            tv_toolbar_state.setText("连接中");
            //iv_toolbar_refresh.setVisibility(View.VISIBLE);
            iv_toolbar_refresh.setVisibility(View.GONE);

        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (MyhealthActivity.ACTION_VIEW_STEP.equals(action)) {
//                if (dayStepFrag == null) {
//                    dayStepFrag = new DayStepFragActivity();
//                }
//                transaction.replace(R.id.myhealth_frame_content, dayStepFrag);
//                transaction.commitAllowingStateLoss();
//                dayStepFrag = null;
                adapter_above = new FragmentAdapter(getChildFragmentManager(),fragments4);
                if(mViewPagerAbove == null){
                    mViewPagerAbove = (ViewPager) view.findViewById(R.id.myhealth_frame_content);
                }
                mViewPagerAbove.setAdapter(adapter_above);
                mViewPagerAbove.setOnPageChangeListener(new FragmentChangeListener());
                mViewPagerAbove.setCurrentItem(curPosition);
            } else if (MyhealthActivity.ACTION_VIEW_HEART.equals(action)) {
//                if (dayHeartFrag == null) {
//                    dayHeartFrag = new DayHeartFragActivity();
//                }
//                transaction.replace(R.id.myhealth_frame_content, dayHeartFrag);
//                transaction.commitAllowingStateLoss();
//                dayHeartFrag = null;
                adapter_above = new FragmentAdapter(getChildFragmentManager(),fragments1);
                if(mViewPagerAbove == null){
                    mViewPagerAbove = (ViewPager) view.findViewById(R.id.myhealth_frame_content);
                }
                mViewPagerAbove.setAdapter(adapter_above);
                mViewPagerAbove.setOnPageChangeListener(new FragmentChangeListener());
                mViewPagerAbove.setCurrentItem(curPosition);
            } else if (MyhealthActivity.ACTION_VIEW_AMOUNT
                    .equals(action)) {
                if (dayAmountFrag == null) {
                    dayAmountFrag = new DayAmountFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, dayAmountFrag);
                transaction.commitAllowingStateLoss();
                dayAmountFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_KCAL.equals(action)) {
                adapter_above = new FragmentAdapter(getChildFragmentManager(),fragments3);
                if(mViewPagerAbove == null){
                    mViewPagerAbove = (ViewPager) view.findViewById(R.id.myhealth_frame_content);
                }
                mViewPagerAbove.setAdapter(adapter_above);
                mViewPagerAbove.setOnPageChangeListener(new FragmentChangeListener());
                mViewPagerAbove.setCurrentItem(curPosition);
//                if (dayKcalFrag == null) {
//                    dayKcalFrag = new DayKcalFragActivity();
//                }
//                transaction.replace(R.id.myhealth_frame_content, dayKcalFrag);
//                transaction.commitAllowingStateLoss();
//                dayKcalFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_LINE_HEART.equals(action)) {
                if (weekHeartFrag == null) {
                    weekHeartFrag = new WeekHeartFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, weekHeartFrag);
                transaction.commitAllowingStateLoss();
                weekHeartFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_COLUMN_HEART.equals(action)) {
                if (weekStepFrag == null) {
                    weekStepFrag = new WeekStepFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, weekStepFrag);
                transaction.commitAllowingStateLoss();
                weekStepFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_BUBBLE_HEART.equals(action)) {
                if (weekKcalFrag == null) {
                    weekKcalFrag = new WeekKcalFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, weekKcalFrag);
                transaction.commitAllowingStateLoss();
                weekKcalFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_SLIDELINE_HEART.equals(action)) {
                if (monthHeartFrag == null) {
                    monthHeartFrag = new MonthHeartFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, monthHeartFrag);
                transaction.commitAllowingStateLoss();
                monthHeartFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_SLIDECOLUMN_HEART.equals(action)) {
                if (monthStepFrag == null) {
                    monthStepFrag = new MonthStepFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, monthStepFrag);
                transaction.commitAllowingStateLoss();
                monthStepFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_SLEEPMODEDATA.equals(action)) {
//                if (daySleepmodeFrag == null) {
//                    daySleepmodeFrag = new DaySleepmodeFragActivity();
//                }
//                transaction.replace(R.id.myhealth_frame_content, daySleepmodeFrag);
//                transaction.commitAllowingStateLoss();
//                daySleepmodeFrag = null;
                adapter_above = new FragmentAdapter(getChildFragmentManager(),fragments2);
                if(mViewPagerAbove == null){
                    mViewPagerAbove = (ViewPager) view.findViewById(R.id.myhealth_frame_content);
                }
                mViewPagerAbove.setAdapter(adapter_above);
                mViewPagerAbove.setOnPageChangeListener(new FragmentChangeListener());
                mViewPagerAbove.setCurrentItem(curPosition);
            }
            //已经连接上BLE

            //没有连接上BLE
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                SampleGattAttributes.connectedState=false;

            }

            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {


                if (intent.getStringExtra("DataType").equals("SevenDayData")) {
                    count++;
                    Log.d(TAG,"hhhhhhhhhh--------");
                    byte[] sevenData=intent.getByteArrayExtra("data");
                    String a1=SampleGattAttributes.dataDivider(sevenData,1,4) + "年";
                    String a2=SampleGattAttributes.dataDivider(sevenData,1,6) + "月";
                    String a3=SampleGattAttributes.dataDivider(sevenData,1,7) + "日";
                 /*   ((TextView) findViewById(R.id.name1)).setText(SampleGattAttributes.dataDivider(sevenData,1,4) + "年");
                    ((TextView) findViewById(R.id.data1)).setText(SampleGattAttributes.dataDivider(sevenData,1,6) + "月");
                    ((TextView) findViewById(R.id.name2)).setText(SampleGattAttributes.dataDivider(sevenData,1,7) + "日");
                    ((TextView) findViewById(R.id.data2)).setText(SampleGattAttributes.dataDivider(sevenData,1,8) + "步");
                    ((TextView) findViewById(R.id.name3)).setText(SampleGattAttributes.dataDivider(sevenData,1,10) + "次/分");*/
                    Log.d(TAG,"hhhhhhhhhh--------"+a1+a2+a3+""+SampleGattAttributes.dataDivider(sevenData,1,10));
                    Log.d(TAG,"hhhhhhhhhh--------"+SampleGattAttributes.dataDivider(sevenData,1,8));
                    if(count==7){
                        SampleGattAttributes.whichactivityconnect=false;
                        SharedPreferences sharedPreferences = context.getSharedPreferences("share", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("isFirstRunAday", mydate);
                        editor.commit();

                        if(!SampleGattAttributes.isinsetactivity){
                            mBluetoothLeService.disconnect();//不在设置界面就断开
                            SampleGattAttributes.connectedState=false;        }
                    }
                }

                else if (intent.getStringExtra("DataType").equals("CollectPeriod")) {
                    int mode = SampleGattAttributes.dataGetter(intent.getByteArrayExtra("data"), 0, 2);
                    if(mode == 480){
                        MainActivity.isSleepBMode = true;
                    }
                    if(mode == 12){
                        MainActivity.isSleepBMode = false;
                    }
                }

            }
        }

};

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_AMOUNT);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_KCAL);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_STEP);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_COLUMN_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_LINE_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_BUBBLE_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_SLEEPMODEDATA);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_SLIDELINE_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_SLIDECOLUMN_HEART);
        return intentFilter;
    }

    class FragmentAdapter extends FragmentStatePagerAdapter{
        private List<Fragment> mFragments;
        private List<String> mTitles;
        FragmentManager fm;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments){
            super(fm);
            this.fm = fm;
            mFragments = fragments;
        }

        public FragmentAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            if(position == 0) {
                position = getCount() - 1;
            }
            else if(position == getCount() + 1) {
                position = 0;
            }
            else {
                position -= 1;
            }
            return super.instantiateItem(container, position);
        }
        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    /**
     *  ViewPager 页面改变监听器
     */
    private final class FragmentChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            curPosition = position;
            dwmText.setText(dwm[position]);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
