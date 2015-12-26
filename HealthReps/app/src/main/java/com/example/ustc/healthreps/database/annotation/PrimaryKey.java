package com.example.ustc.healthreps.database.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by CaoRuijuan on 12/22/15.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
    boolean autoincrement();
}
