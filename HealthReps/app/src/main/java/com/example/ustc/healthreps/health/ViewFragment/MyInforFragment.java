package com.example.ustc.healthreps.health.ViewFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by CaoRuijuan on 4/15/16.
 */
public class MyInforFragment extends Fragment{
    private TextView tv_username, tv_sex, tv_age, tv_type, tv_zhicheng, tv_realname, tv_keshi,
            tv_phone, tv_defaultStore, tv_id, tv_yibao, tv_introduction, tv_address;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfor, null);

        initView(view);
        initData();
        return view;
    }

    public void initView(View view ){
        tv_username = (TextView) view.findViewById(R.id.myUsernameTV);
        tv_sex = (TextView) view.findViewById(R.id.mySexTV);
        tv_age = (TextView) view.findViewById(R.id.myAgeTV);
        tv_type = (TextView) view.findViewById(R.id.myTypeTV);
        tv_zhicheng = (TextView) view.findViewById(R.id.myZhiChengTV);
        tv_realname = (TextView) view.findViewById(R.id.myRealnameTV);
        tv_keshi = (TextView) view.findViewById(R.id.myKeshiTV);
        tv_phone = (TextView) view.findViewById(R.id.myPhoneTV);
        tv_defaultStore = (TextView) view.findViewById(R.id.myDefaultStoreTV);
        tv_id = (TextView) view.findViewById(R.id.myID_NumTV);
        tv_yibao = (TextView) view.findViewById(R.id.myYibao_NumTV);
        tv_introduction = (TextView) view.findViewById(R.id.myIntroductionTV);
        tv_address = (TextView) view.findViewById(R.id.myAddressTV);
    }

    public void initData(){
        try{
            tv_username.setText(new String(Users.sLoginUser.loginName,"GBK").trim());
            tv_sex.setText(new String(Users.sLoginUser.sex,"GBK").trim());
            tv_age.setText(new String(Users.sLoginUser.age,"GBK").trim());
//            tv_type.setText(Utils.changeTypeToString(Users.sLoginUser.type));
//            tv_zhicheng.setText(new String(Users.sLoginUser.zhicheng,"GBK").trim());
            tv_realname.setText(new String(Users.sLoginUser.realName,"GBK").trim());
//            tv_keshi.setText(new String(Users.sLoginUser.keshi,"GBK").trim());
            tv_phone.setText(new String(Users.sLoginUser.phone,"GBK").trim());
            tv_defaultStore.setText(new String(Users.sLoginUser.defaultStore,"GBK").trim());
            tv_id.setText(new String(Users.sLoginUser.ID_Num,"GBK").trim());
            tv_yibao.setText(new String(Users.sLoginUser.yibao_Num,"GBK").trim());
            tv_introduction.setText(new String(Users.sLoginUser.pastDiseaseHistory,"GBK").trim());
            tv_address.setText(new String(Users.sLoginUser.address,"GBK").trim());
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

    }
}
