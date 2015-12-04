package com.example.ustc.healthreps;

/**
 * Created by HBL on 2015/11/21.
 */
public class DeviceBean {
    String name;
    String address;

    public DeviceBean(String name, String address) {
        this.name = name;
        this.address = address;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
