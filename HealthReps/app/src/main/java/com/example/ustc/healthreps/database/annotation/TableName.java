package com.example.ustc.healthreps.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CaoRuijuan on 12/22/15.
 * 注解类-表名
 */

//放在具体实体类的脑袋上
@Target(ElementType.TYPE)

//在运行时的时候得到，生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {
    String value();
}
