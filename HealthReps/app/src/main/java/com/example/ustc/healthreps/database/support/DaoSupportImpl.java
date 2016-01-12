package com.example.ustc.healthreps.database.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.DBManagerForExist;
import com.example.ustc.healthreps.database.DBManagerForNew;
import com.example.ustc.healthreps.database.annotation.ColumnName;
import com.example.ustc.healthreps.database.annotation.PrimaryKey;
import com.example.ustc.healthreps.database.annotation.TableName;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 12/22/15.
 */
public class DaoSupportImpl<DBEntity> implements IDaoSupport<DBEntity> {
    private String TAG = "DaoSupportImpl";
    private Context context;
    private DBManagerForNew dbManagerForNew;
    private DBManagerForExist dbManagerForExist;
    private SQLiteDatabase db;

    public DaoSupportImpl(Context context, String dbName, boolean isExist) {
        this.context = context;

        if(isExist){
            dbManagerForExist = new DBManagerForExist(context);
            dbManagerForExist.openDatabase();
            db = dbManagerForExist.getDatabase();
        }
        else{
            dbManagerForNew = new DBManagerForNew(context, dbName);
            db = dbManagerForNew.getWritableDatabase();
        }
    }

    //获取实体对象
    public DBEntity getInstance() {
        // ①哪个孩子调用的该方法
        Class clazz = getClass();// 获取到了正在运行时的那个类，这里就会拿到实际在跑的那个impl类

        Log.i(TAG, clazz.toString());

        // ②获取该孩子的父类(是支持泛型的父类)
        // clazz.getSuperclass();// 这个方法不行，拿不到泛型
        Type genericSuperclass = clazz.getGenericSuperclass();// 可以拿到泛型

        // jdk会让泛型实现一个接口(参数化的类型--这个接口)，所有的泛型都会实现这个接口(ParameterizedType)，规定了泛型的通用操作

        if (genericSuperclass != null
                && genericSuperclass instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();

            try {
                return ((Class<DBEntity>) arguments[0]).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        // ③获取到泛型中的参数
        return null;
    }


    //获取表名
    public String getTableName() {
        //获取对象实例
        DBEntity entity = getInstance();

        //根据注释获取表名
        if (entity != null) {
            TableName tableName = entity.getClass().getAnnotation(TableName.class);
            if (tableName != null) {
                return tableName.value();
            }
        }
        return null;
    }

    //得到_id值
    private String getIdValue(DBEntity entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field item : fields) {
            item.setAccessible(true);
            PrimaryKey primaryKey = item.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                try {
                    return item.get(entity).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //填充数据项
    private void fillColumn(DBEntity entity, ContentValues values, boolean containsPrimaryKey) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field item : fields) {
            ColumnName columnName = item.getAnnotation(ColumnName.class);
            if (columnName != null) {
                String key = columnName.value();
                String value;
                try {
                    //赋予私有项权限
                    item.setAccessible(true);

                    //主键id
                    PrimaryKey primaryKey = item.getAnnotation(PrimaryKey.class);
                    //自增长的主键不insert
                    if (primaryKey != null && primaryKey.autoincrement()) {
                        if(!containsPrimaryKey)
                            continue;
                    }

                    value = item.get(entity).toString();
                    values.put(key, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean insert(DBEntity entity) {
        ContentValues values = new ContentValues();
        fillColumn(entity, values, false);
        if(db.insert(getTableName(), null, values)==-1)
            return false;
        else
            return true;
    }

    //通过主键_id
    @Override
    public boolean delete(int id) {
        if(db.delete(getTableName(), DBConstants.TABLE_KEY + "=?", new String[]{String.valueOf(id)})== -1)
            return false;
        else
            return true;
    }

    @Override
    public boolean update(DBEntity entity) {
        ContentValues values = new ContentValues();
        fillColumn(entity, values, true);
        String id = getIdValue(entity);
        if(db.update(getTableName(), values, DBConstants.TABLE_KEY + "=?", new String[]{id}) == -1){
            return false;
        }
        else
            return true;
    }

    @Override
    public boolean updateItems(int id, String[] columns, String[] values) {
        ContentValues values1 = new ContentValues();
        for (int i = 0;i<columns.length;i++){
            values1.put(columns[i],values[i]);
        }
        if(db.update(getTableName(), values1, DBConstants.TABLE_KEY + "=?", new String[]{String.valueOf(id)}) == -1){
            return false;
        }
        else
            return true;
    }

    //搜索到填充实体
    private void fillField(Cursor cursor, DBEntity entity) {

        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field item : fields) {

            item.setAccessible(true);
            ColumnName columnName = item.getAnnotation(ColumnName.class);
            if (columnName != null) {
                String key = columnName.value();
                int columnIndex = cursor.getColumnIndex(key);
                String value = cursor.getString(columnIndex);
                PrimaryKey primaryKey = item.getAnnotation(PrimaryKey.class);
                try {
                    if (primaryKey != null) {
                        item.set(entity, Integer.parseInt(value));
                    } else {
                        item.set(entity, value);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ArrayList<DBEntity> find(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        ArrayList<DBEntity> result = new ArrayList<>();

        //query(String table, String[] columns, String selection,String[] selectionArgs, String groupBy, String having,String orderBy)
        Cursor cursor = db.query(getTableName(), columns, selection, selectionArgs, null, null, orderBy);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DBEntity entity = getInstance();
                fillField(cursor, entity);
                result.add(entity);
            }
        }

        cursor.close();
        return result;
    }

    @Override
    public ArrayList<DBEntity> find(String[] columns, String selection, String[] selectionArgs) {
        return find(columns,selection,selectionArgs,null);
    }

    @Override
    public ArrayList<DBEntity> findAll() {
        return find(null, null, null);
    }

    @Override
    public ArrayList<DBEntity> findEntity(String selection, String[] selectionArgs) {
        return find(null,selection,selectionArgs);
    }

    @Override
    public int getIDByColumnValue(String columnName, String value) {
        Cursor cursor = db.query(getTableName(), new String[]{DBConstants.TABLE_KEY}, columnName+"=?", new String[]{value}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                return cursor.getInt(0);
            }
        }
        return -1;
    }
}
