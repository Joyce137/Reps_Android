package com.example.ustc.healthreps.database.impl;

import android.content.Context;

import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.entity.User;
import com.example.ustc.healthreps.database.support.DaoSupportImpl;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.serverInterface.UserInfo;

import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 1/28/16.
 */
public class UserDaoImpl extends DaoSupportImpl<User> {
    private Context context;

    public UserDaoImpl(Context context){
        super(context,"user",false);
        //新建user表
        try {
            getDb().execSQL(DBConstants.USER_TABLE_CREATE);
        }catch (Exception e){
            e.printStackTrace();
        }

        this.context = context;
    }

    //判断某用户是否在user表中
    public User checkUsernameExistInUser(String username){
        String selector = DBConstants.USER_USERNAME + "=?";
        String[] selectorargs = new String[]{username};

        ArrayList<User> users= findEntity(selector,selectorargs);

        if(users.size() == 0)
            return null;
        return users.get(0);
    }

    //添加新用户
    public boolean addNewUserToUser(User user){
        if(checkUsernameExistInUser(user.username)==null)
            return insert(user);
        return false;
    }

    //删除用户
    public boolean removeUserFromUser(String username){
        int id = getIDByColumnValue(DBConstants.USER_USERNAME,username);
        if(id != -1)
            return delete(id);
        else
            return false;
    }

    //更新用户信息
    public boolean updateUserByUserInfo(UserInfo userInfo){
        User user = userInfo.changeUserInfoToUser();
        //如果存在
        User user1 = checkUsernameExistInUser(user.username);
        if(user1!=null){
            //将_idcopy到user
            user.id = user1.id;
            return update(user);
        }
        else{
            return addNewUserToUser(user);
        }
    }

    //查找用户
    public User searchUserFromUserTableByUsername(String username){
        String selector = DBConstants.USER_USERNAME + "=?";
        String[] selectorargs = new String[]{username};
        if(findEntity(selector,selectorargs).size() != 1)
            return null;
        else
            return findEntity(selector,selectorargs).get(0);
    }
}
