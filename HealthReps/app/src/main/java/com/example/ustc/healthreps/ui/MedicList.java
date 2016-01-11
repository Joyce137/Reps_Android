package com.example.ustc.healthreps.ui;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.FirstClassAdapter;
import com.example.ustc.healthreps.adapter.SecondClassAdapter;
import com.example.ustc.healthreps.model.FirstClassItem;
import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.model.SecondClassItem;
import com.example.ustc.healthreps.model.SelectPicPopupWindow;

/*
 * 药店列表项点开后，进行该药品清单界面
 * 2015/12/12
 * hzy
 */
public class MedicList extends Activity {

    private TextView tv_storeName;
    private TextView mainTab1TV,mainTab2TV,mainTab3TV;
    /**左侧一级分类的数据*/
    private List<FirstClassItem> firstList1,firstList2;
    /**右侧二级分类的数据*/
    private List<SecondClassItem> secondList1,secondList2;
    /**使用PopupWindow显示一级分类和二级分类*/
    private PopupWindow popupWindow1,popupWindow2,popupWindow3;

    /**左侧和右侧两个ListView*/
    private ListView leftLV1, rightLV1,leftLV2, rightLV2;

    //药店信息列表
    private ListView medic_list_view;
    ArrayAdapter<Medicine> medic_Adapter;
    List <Medicine> list = new ArrayList <Medicine>();
    //弹出PopupWindow时背景变暗
    private View darkView;

    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;

    //弹出PopupWindow时，箭头方向
    private boolean flag1=true,flag2=true,flag3=true;

    //智能排序
    private TextView smart_sort,nearest_sort,hot_sort,best_sort;

    //自定义弹出框类
    private SelectPicPopupWindow menuWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.medicine_list);

        init();
        initData();
        initPopup();

        OnClickListenerImpl l = new OnClickListenerImpl();
        mainTab1TV.setOnClickListener(l);
        mainTab2TV.setOnClickListener(l);
        mainTab3TV.setOnClickListener(l);
    }

    //点击事件
    class OnClickListenerImpl implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dep:
                    tab1OnClick();
                    break;
                case R.id.behavior:
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
            if (popupWindow3.isShowing()) {
                popupWindow3.dismiss();
            } else {
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
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                mainTab3TV.setCompoundDrawables(null, null, nav_up, null);

                popupWindow3.showAsDropDown(findViewById(R.id.main_div_line));
                popupWindow3.setAnimationStyle(-1);
                //背景变暗
                darkView.startAnimation(animIn);
                darkView.setVisibility(View.VISIBLE);
            }
        }

        private void tab2OnClick() {
            // TODO Auto-generated method stub
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

                popupWindow2.showAsDropDown(findViewById(R.id.main_div_line));
                popupWindow2.setAnimationStyle(-1);
                //背景变暗
                darkView.startAnimation(animIn);
                darkView.setVisibility(View.VISIBLE);
            }
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

                popupWindow1.showAsDropDown(findViewById(R.id.main_div_line));
                popupWindow1.setAnimationStyle(-1);
                //背景变暗
                darkView.startAnimation(animIn);
                darkView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initPopup() {
        // TODO Auto-generated method stub
        popupWindow1 = new PopupWindow(this);

        View view = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        leftLV1 = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV1 = (ListView) view.findViewById(R.id.pop_listview_right);

        popupWindow1.setContentView(view);
        popupWindow1.setBackgroundDrawable(new PaintDrawable());
        popupWindow1.setFocusable(true);

        popupWindow1.setHeight(ScreenUtils.getScreenH(this) * 2 / 3);
        popupWindow1.setWidth(ScreenUtils.getScreenW(this));

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
        final FirstClassAdapter firstAdapter1 = new FirstClassAdapter(this, firstList1);
        leftLV1.setAdapter(firstAdapter1);

        //加载左侧第一行对应右侧二级分类
        secondList1 = new ArrayList<SecondClassItem>();
        secondList1.addAll(firstList1.get(0).getSecondList());
        final SecondClassAdapter secondAdapter1 = new SecondClassAdapter(this, secondList1);
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
            //科别信息分类
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

        //症状信息
        popupWindow2 = new PopupWindow(this);

        View view2 = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        leftLV2 = (ListView) view2.findViewById(R.id.pop_listview_left);
        rightLV2 = (ListView) view2.findViewById(R.id.pop_listview_right);

        popupWindow2.setContentView(view2);
        popupWindow2.setBackgroundDrawable(new PaintDrawable());
        popupWindow2.setFocusable(true);

        popupWindow2.setHeight(ScreenUtils.getScreenH(this) * 2 / 3);
        popupWindow2.setWidth(ScreenUtils.getScreenW(this));

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
        final FirstClassAdapter firstAdapter2 = new FirstClassAdapter(this, firstList2);
        leftLV2.setAdapter(firstAdapter2);

        //加载左侧第一行对应右侧二级分类
        secondList2 = new ArrayList<SecondClassItem>();
        secondList2.addAll(firstList2.get(0).getSecondList());
        final SecondClassAdapter secondAdapter2 = new SecondClassAdapter(this, secondList2);
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

        //排序信息
        popupWindow3 = new PopupWindow(this);

        View view3 = LayoutInflater.from(this).inflate(R.layout.smart_sort, null);

        smart_sort = (TextView) view3.findViewById(R.id.tv_smart);
        nearest_sort = (TextView) view3.findViewById(R.id.tv_nearest);
        hot_sort = (TextView) view3.findViewById(R.id.tv_hotest);
        best_sort = (TextView) view3.findViewById(R.id.tv_best);

        popupWindow3.setContentView(view3);
        popupWindow3.setFocusable(true);

        popupWindow3.setHeight(ScreenUtils.getScreenH(this)*5/18);
        popupWindow3.setWidth(ScreenUtils.getScreenW(this));

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
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            mainTab3TV.setText(str);
            popupWindow3.dismiss();
            Drawable nav_up = null;
            nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
            flag3=true;
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            mainTab3TV.setCompoundDrawables(null, null, nav_up, null);
        }
    }

    private void initData() {
        // TODO Auto-generated method stub
		/*
		 * 初始化科别信息，后期数据应从数据库或者联网获取
		 */
        firstList1 = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> list11 = new ArrayList<SecondClassItem>();
        list11.add(new SecondClassItem(111, "全部科室1"));
        list11.add(new SecondClassItem(112, "全部科室2"));
        firstList1.add(new FirstClassItem(1, "全部科室", list11));

        ArrayList<SecondClassItem> list12 = new ArrayList<SecondClassItem>();
        list12.add(new SecondClassItem(211, "内科1"));
        list12.add(new SecondClassItem(212, "内科2"));
        firstList1.add(new FirstClassItem(2, "内科", list12));

        ArrayList<SecondClassItem> list13 = new ArrayList<SecondClassItem>();
        list13.add(new SecondClassItem(311, "外科1"));
        list13.add(new SecondClassItem(312, "外科2"));
        firstList1.add(new FirstClassItem(3, "外科", list13));

        ArrayList<SecondClassItem> list14 = new ArrayList<SecondClassItem>();
        list14.add(new SecondClassItem(411, "其他1"));
        list14.add(new SecondClassItem(412, "其他2"));
        firstList1.add(new FirstClassItem(4, "其他", list14));
        
        /*
         * 症状信息，后期从数据库或者联网获取
         */
        firstList2 = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> list21 = new ArrayList<SecondClassItem>();
        list21.add(new SecondClassItem(211, "感冒药1"));
        list21.add(new SecondClassItem(212, "感冒药2"));
        list21.add(new SecondClassItem(213, "感冒药3"));
        firstList2.add(new FirstClassItem(1, "感冒", list21));

        ArrayList<SecondClassItem> list22 = new ArrayList<SecondClassItem>();
        list22.add(new SecondClassItem(221, "解热镇痛药1"));
        list22.add(new SecondClassItem(222, "解热镇痛药2"));
        list22.add(new SecondClassItem(223, "解热镇痛药3"));
        firstList2.add(new FirstClassItem(2, "解热镇痛", list22));

        ArrayList<SecondClassItem> list23 = new ArrayList<SecondClassItem>();
        list23.add(new SecondClassItem(231, "止咳化痰药1"));
        list23.add(new SecondClassItem(232, "止咳化痰药2"));
        list23.add(new SecondClassItem(233, "止咳化痰药3"));
        firstList2.add(new FirstClassItem(3, "止咳化痰", list23));

        //初始化药品信息爱
        List <Medicine> list_m = new ArrayList <Medicine>();
        list_m.add(new Medicine("阿司匹林","非处方药","解热镇痛药"));

        for(int i=0;i<list_m.size();i++){
            Medicine med = list_m.get(i);
            list.add(med);
        }

        //为弹出窗口实现监听类
        final OnClickListener  itemsOnClick = new OnClickListener(){
            public void onClick(View v) {
                menuWindow.dismiss();
                switch (v.getId()) {
                    case R.id.sub:
                        break;
                    case R.id.add:
                        break;
                    case R.id.sumbit:
                        break;
                    default:
                        break;
                }
            }
        };

        medic_Adapter = new ArrayAdapter<Medicine>(this,android.R.layout.simple_expandable_list_item_1,list);
        medic_list_view.setAdapter(medic_Adapter);
        medic_list_view.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Medicine med = (Medicine)medic_Adapter.getItem(position);


                RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.medic_detail_popup,
                        null);
                final PopupWindow mPopupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);


                mPopupWindow.setAnimationStyle(R.style.AnimBottom);
                mPopupWindow.setFocusable(true);
                // 实例化一个ColorDrawable颜色为半透明
//			    ColorDrawable dw = new ColorDrawable(0x55000000);
//			    mPopupWindow.setBackgroundDrawable(dw);
                mPopupWindow.showAtLocation(findViewById(R.id.main), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);

                Button btnAdd = (Button) layout.findViewById(R.id.add);
                Button btnSub = (Button) layout.findViewById(R.id.sub);
                Button btnSummit = (Button) layout.findViewById(R.id.sumbit);

                final EditText edt_amount = (EditText) layout.findViewById(R.id.amount);
                //数量加1
                btnAdd.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        int num = Integer.parseInt(edt_amount.getText().toString());
                        edt_amount.setText(String.valueOf(++num));
                    }
                });
                //数量减1
                btnSub.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        int num = Integer.parseInt(edt_amount.getText().toString());
                        if(0 == num){
                            edt_amount.setText("0");
                        }
                        else{
                            edt_amount.setText(String.valueOf(--num));
                        }
                    }
                });
                //
                btnSummit.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        mPopupWindow.dismiss();
                    }

                });
            }

        });

    }

    private void init() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        String sName = intent.getStringExtra("store_name");
        String sCate = intent.getStringExtra("store_category");

        tv_storeName = (TextView) findViewById(R.id.storename);
        tv_storeName.setText(sName);

        mainTab1TV = (TextView) findViewById(R.id.dep);
        mainTab2TV = (TextView) findViewById(R.id.behavior);
        mainTab3TV = (TextView) findViewById(R.id.smartSort);

        medic_list_view = (ListView) findViewById(R.id.medic_list);
        darkView = findViewById(R.id.main_darkview);

        animIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_anim);
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
        Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();

        if(flag == 1)
            mainTab1TV.setText(selectedName);
        else if(flag == 2)
            mainTab2TV.setText(selectedName);
    }
}
