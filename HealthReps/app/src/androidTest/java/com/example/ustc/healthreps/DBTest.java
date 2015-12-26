package com.example.ustc.healthreps;

import android.test.AndroidTestCase;

import com.example.ustc.healthreps.database.impl.CookieDaoImpl;
import com.example.ustc.healthreps.database.entity.Cookie;

import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 12/22/15.
 * 数据库测试类
 */
public class DBTest extends AndroidTestCase{
    public void testDB(){
        CookieDaoImpl dao = new CookieDaoImpl(getContext());
        dao.insert(new Cookie("1","12","20151222"));
        dao.insert(new Cookie("2","是的","201512DD"));
        dao.insert(new Cookie("3","3222","201222"));

        ArrayList<Cookie> list = dao.findAll();

        for(Cookie cookie:list){
            System.out.println(cookie.toString());
        }

        System.out.println("------删除操作分割线------");
        dao.delete(2);
        list = dao.findAll();
        for(Cookie cookie:list){
            System.out.println(cookie.toString());
        }
    }
}
