package com.example.ustc.healthreps.database.support;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 12/22/15.
 */
public interface IDaoSupport<DBEntity> {
    //接口-泛型，实现基本数据库操作

    //增
    boolean insert(DBEntity entity);

    //删---根据自增主键
    boolean delete(int id);

    //清空
    void clear();

    //改
    boolean update(DBEntity entity);

    //更新某些项
    boolean updateItems(int id,String[] columns,String[] values);

    //查
    //query(String table, String[] columns, String selection,String[] selectionArgs, String groupBy, String having,String orderBy)
    ArrayList<DBEntity> find(String[] columns, String selection,String[] selectionArgs, String orderBy);
    ArrayList<DBEntity> find(String[] columns, String selection,String[] selectionArgs);

    //搜索全部
    ArrayList<DBEntity> findAll();

    //通过某列查询实体
    ArrayList<DBEntity> findEntity(String selection, String[] selectionArgs);

    //通过某列value得到主键_id
    int getIDByColumnValue(String columnName, String value);


    //执行sql语句
    ArrayList<DBEntity> executeSql(String[] columns,String sql);
    ArrayList<DBEntity> executeSql(String sqlStr);
}
