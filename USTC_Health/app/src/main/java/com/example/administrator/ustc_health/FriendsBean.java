package com.example.administrator.ustc_health;

import android.content.Context;

/**
 * Created by Administrator on 2015/10/25.
 */
public class FriendsBean {
    String name;
    String picName;

    public FriendsBean(String name, String picName) {
        this.name = name;
        this.picName = picName;
    }


    public String getPicName() {
        return picName;
    }

    public void setPicNames(String picName) {
        this.picName = picName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
