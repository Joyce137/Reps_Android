package com.example.ustc.healthreps.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.TabMedicineAdapter;
import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.impl.MedicineDaoImpl;
import com.example.ustc.healthreps.model.FirstClassItem;
import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.model.Medicine_Info_List;
import com.example.ustc.healthreps.model.SecondClassItem;
import com.example.ustc.healthreps.model.SelectPicPopupWindow;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.repo.SearchInfo;

/*
 * 药店列表项点开后，进行该药品清单界面
 * 2015/12/12
 * hzy
 */
public class MedicineList extends Fragment {
    View view;
    private TextView tv_storeName;
    private EditText et_search_medicine;
    private ImageView searchImg;
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
    private TabMedicineAdapter medic_Adapter;

    List <Medicine> list = new ArrayList <com.example.ustc.healthreps.model.Medicine>();
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
    private LinearLayout layout;
    private ListView listView;

    private int flag = -1;  //全部科室：0，内科：1，外科:2,其他：3

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.medicine_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
//        initPopup();

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
            popupWindow3 = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
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
                    mainTab3TV.setText(str);
                    popupWindow3.dismiss();
                    //指示图标归位
                    Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    mainTab3TV.setCompoundDrawables(null, null, nav_up, null);
                    Toast.makeText(getActivity().getApplication(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }

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
            final String title[] = {
                    "抗感染","感冒","解热镇痛","心脑",
                    "止咳化痰","胃肠道","镇静安神","糖尿病",
                    "内分泌","泌尿", "骨伤科","肝胆","甲状腺","肿瘤",
                    "五官","儿童","妇科","皮肤",
                    "清热解毒","维生素","滋补养颜","针剂",
                    "抗肿瘤","其他"
            };

            //内科
            final String nai[] = {
                    "抗感染","感冒","解热镇痛","心脑",
                    "止咳化痰","胃肠道","镇静安神","糖尿病",
                    "内分泌","泌尿"
            };
            //外科
            final String wai[] = {
                    "骨伤科","肝胆","甲状腺","肿瘤"
            };
            //其他
            final String other[] = {
                    "五官","儿童","妇科","皮肤",
                    "清热解毒","维生素","滋补养颜","针剂",
                    "抗肿瘤","其他"
            };
            String temp[] = null;
            switch (flag){
                case 0:
                    temp=title;
                    break;
                case 1:
                    temp=nai;
                    break;
                case 2:
                    temp=wai;
                    break;
                case 3:
                    temp=other;
                    break;
                default:
                    temp = title;
                    break;
            }

            //加载布局
            layout = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.more_setting_dialog, null);
            //找到布局控件
            listView = (ListView)layout.findViewById(R.id.lv_dialog);
            //设置适配器
            listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                    R.layout.more_text_setting,R.id.tv_text,temp));
            //实例化popupwindow
            popupWindow2 = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
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
                    String str = null;
                    popupWindow2.dismiss();
                    switch (flag){
                        case 0:
                            str = title[position];
                            break;
                        case 1:
                            str = nai[position];
                            break;
                        case 2:
                            str = wai[position];
                            break;
                        case 3:
                            str = other[position];
                            break;
                        default:
                            str = title[position];
                            break;
                    }
                    mainTab2TV.setText(str);
                    //指示图标归位
                    Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    mainTab2TV.setCompoundDrawables(null, null, nav_up, null);
                    Toast.makeText(getActivity().getApplication(), str, Toast.LENGTH_SHORT).show();

                    //按科室查询
                    searchMedicineByCategory(str);
                }
            });
        }

        private void tab1OnClick() {
            if(flag1){
                Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_up_black);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                mainTab1TV.setCompoundDrawables(null, null, nav_up, null);
            }
            else{
                Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                mainTab1TV.setCompoundDrawables(null, null, nav_up, null);
            }

            final String title[] = {"全部科室","内科","外科","其他"};
            //加载布局
            layout = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.more_setting_dialog, null);
            //找到布局控件
            listView = (ListView)layout.findViewById(R.id.lv_dialog);
            //设置适配器
            listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                    R.layout.more_text_setting,R.id.tv_text,title));
            //实例化popupwindow
            popupWindow1 = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            //控制键盘是否可以获得焦点
            popupWindow1.setFocusable(true);
            popupWindow1.setAnimationStyle(R.style.mystyle);
            popupWindow1.setBackgroundDrawable(new BitmapDrawable(null, ""));
            WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);

            //获取xoff
            int xpos = manager.getDefaultDisplay().getWidth()/2-popupWindow1.getWidth()/2;
            //xoff,yoff基于anchor的左下角进行偏移
            popupWindow1.showAsDropDown(mainTab1TV, xpos, 0);
            //监听
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String str = title[position];
                    flag = position;

                    popupWindow1.dismiss();
                    mainTab1TV.setText(str);
                    //指示图标归位
                    Drawable  nav_up=getResources().getDrawable(R.drawable.ic_arrow_down_black);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    mainTab1TV.setCompoundDrawables(null, null, nav_up, null);

                    Toast.makeText(getActivity().getApplication(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        }
    }

    private void initData() {
        //初始化药品信息
        //查询全部药品
        MedicineDaoImpl dao = new MedicineDaoImpl(getActivity().getApplicationContext());
//        ArrayList<Medicine> list_m = dao.findAll();
        SearchInfo info = new SearchInfo();
        info.drugStoreCategory = "感冒";
        ArrayList<Medicine> list = dao.queryMedicineBySearch(info);

        final Medicine_Info_List minfo_list = new Medicine_Info_List();  //传递到药品清单的封装对象
        final List<Medicine> list_medic = new ArrayList<com.example.ustc.healthreps.model.Medicine>();     //传递到药品清单的封装对象的数据

        medic_Adapter = new TabMedicineAdapter(getActivity().getApplicationContext(),list);
        medic_list_view.setAdapter(medic_Adapter);
        medic_list_view.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                final Medicine medicine = (com.example.ustc.healthreps.model.Medicine) medic_Adapter.getItem(position);

                //自定义弹出药店数量对话框
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
                builder.setTitle("药品清单");
                View v = LayoutInflater.from(getActivity().getApplication()).inflate(R.layout.medic_amount_dialog, null);
                builder.setView(v);

                Button btnAdd = (Button) v.findViewById(R.id.add);
                Button btnSub = (Button) v.findViewById(R.id.sub);
                final EditText etAmount = (EditText) v.findViewById(R.id.amount);
                final TextView etName = (TextView) v.findViewById(R.id.medic_name);

                etName.setText(medicine.name);

                btnAdd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = Integer.parseInt(etAmount.getText().toString());
                        etAmount.setText(String.valueOf(++num));
                    }
                });
                btnSub.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = Integer.parseInt(etAmount.getText().toString());
                        if (0 == num) {
                            etAmount.setText("0");
                        } else {
                            etAmount.setText(String.valueOf(--num));
                        }
                    }
                });
                builder.setPositiveButton("去看清单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int num = Integer.parseInt(etAmount.getText().toString());
                        if (num > 0) {
                            medicine.num = num;
                            list_medic.add(medicine);
                            minfo_list.setArrayList(list_medic);
                        }

                        Intent intent = new Intent(getActivity().getApplication(), MedicinePickList.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", minfo_list);
                        intent.putExtras(bundle);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("继续选药", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int num = Integer.parseInt(etAmount.getText().toString());
                        if (num > 0) {
                            medicine.num = num;
                            list_medic.add(medicine);
                        }

                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }

        });
    }

    private void initView() {
        String sName;
        Bundle bundle = getArguments();
        if(bundle != null){
            sName = bundle.getString("store_name");
            String sCate = bundle.getString("store_category");
        }
        else{
            sName = Users.sDefaultStore;
        }

        tv_storeName = (TextView) view.findViewById(R.id.storename);
        tv_storeName.setText(sName);

        et_search_medicine = (EditText) view.findViewById(R.id.et_searchMedicine);

        searchImg = (ImageView) view.findViewById(R.id.iv_search_medicine);
        searchImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                String text1 = "正在查询，请稍候......";
//                Toast.makeText(getActivity().getApplication(), text1, Toast.LENGTH_SHORT).show();
                //查询条件字符串
                String queryCategoryStr = et_search_medicine.getText().toString().trim();
                seachMedicineByInput(queryCategoryStr);
            }
        });

        mainTab1TV = (TextView) view.findViewById(R.id.dep);
        mainTab2TV = (TextView) view.findViewById(R.id.behavior);
        mainTab3TV = (TextView) view.findViewById(R.id.smartSort);

        medic_list_view = (ListView) view.findViewById(R.id.medic_list);
        darkView = view.findViewById(R.id.main_darkview);

        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);

        ViewGroup tableTitle = (ViewGroup) view.findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.GREEN);
    }

    //处理点击结果
    private void handleResult(int flag,int firstId, int secondId,String selectedName){
        //String text = "first id:" + firstId + ",second id:" + secondId;
        String text = "你选择的是：" + selectedName;
        Toast.makeText(getActivity().getApplication(), text, Toast.LENGTH_SHORT).show();

        if(flag == 1)
            mainTab1TV.setText(selectedName);
        else if(flag == 2)
            mainTab2TV.setText(selectedName);
    }

    //模糊查询
    public void seachMedicineByInput(String searchStr){
        MedicineDaoImpl dao = new MedicineDaoImpl(getActivity().getApplicationContext());
        ArrayList<Medicine> medicineList = dao.queryMedicinesByNameOrCategory(searchStr);
        int x = medicineList.size();
        String text = "查询到：" + x + "条记录";
        Toast.makeText(getActivity().getApplication(), text, Toast.LENGTH_LONG).show();
        refreshMedicineList(medicineList);
    }

    //按科室查询
    public void searchMedicineByCategory(String catagory){
        MedicineDaoImpl dao = new MedicineDaoImpl(getActivity().getApplicationContext());

        SearchInfo info = new SearchInfo();
        info.drugStoreCategory = catagory;
        ArrayList<Medicine> medicineList = dao.queryMedicineBySearch(info);
        int x = medicineList.size();
        String text = "查询到：" + x + "条记录";
        Toast.makeText(getActivity().getApplication(), text, Toast.LENGTH_LONG).show();
        refreshMedicineList(medicineList);
    }

    //刷新列表
    public void refreshMedicineList(ArrayList<Medicine> medicineList){
        list.clear();
        for(Medicine medicine : medicineList){
            list.add(medicine);
        }
        medic_Adapter = new TabMedicineAdapter(getActivity().getApplicationContext(),list);
        medic_list_view.setAdapter(medic_Adapter);
    }
}
