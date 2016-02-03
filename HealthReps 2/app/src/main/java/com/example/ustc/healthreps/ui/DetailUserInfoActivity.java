package com.example.ustc.healthreps.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.utils.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by CaoRuijuan on 1/28/16.
 */
public class DetailUserInfoActivity extends Activity{
    private TextView tv_username, tv_sex, tv_age, tv_type, tv_zhicheng, tv_realname, tv_keshi,
            tv_phone, tv_defaultStore, tv_id, tv_yibao, tv_introduction, tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydetail);

        initView();
        initData();
    }

    public void initView(){
        tv_username = (TextView) findViewById(R.id.myUsernameTV);
        tv_sex = (TextView) findViewById(R.id.mySexTV);
        tv_age = (TextView) findViewById(R.id.myAgeTV);
        tv_type = (TextView) findViewById(R.id.myTypeTV);
        tv_zhicheng = (TextView) findViewById(R.id.myZhiChengTV);
        tv_realname = (TextView) findViewById(R.id.myRealnameTV);
        tv_keshi = (TextView) findViewById(R.id.myKeshiTV);
        tv_phone = (TextView) findViewById(R.id.myPhoneTV);
        tv_defaultStore = (TextView) findViewById(R.id.myDefaultStoreTV);
        tv_id = (TextView) findViewById(R.id.myID_NumTV);
        tv_yibao = (TextView) findViewById(R.id.myYibao_NumTV);
        tv_introduction = (TextView) findViewById(R.id.myIntroductionTV);
        tv_address = (TextView) findViewById(R.id.myAddressTV);
    }

    public void initData(){
        try{
            tv_username.setText(new String(Users.sLoginUser.loginName,"GBK"));
            tv_sex.setText(new String(Users.sLoginUser.sex,"GBK"));
            tv_age.setText(new String(Users.sLoginUser.age,"GBK"));
            tv_type.setText(Utils.changeTypeToString(Users.sLoginUser.type));
            tv_zhicheng.setText(new String(Users.sLoginUser.zhicheng,"GBK"));
            tv_realname.setText(new String(Users.sLoginUser.realName,"GBK"));
            tv_keshi.setText(new String(Users.sLoginUser.keshi,"GBK"));
            tv_phone.setText(new String(Users.sLoginUser.phone,"GBK"));
            tv_defaultStore.setText(new String(Users.sLoginUser.defaultStore,"GBK"));
            tv_id.setText(new String(Users.sLoginUser.ID_Num,"GBK"));
            tv_yibao.setText(new String(Users.sLoginUser.yibao_Num,"GBK"));
            tv_introduction.setText(new String(Users.sLoginUser.pastDiseaseHistory,"GBK"));
            tv_address.setText(new String(Users.sLoginUser.address,"GBK"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

    }
}
