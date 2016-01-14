package com.example.ustc.healthreps.repo;

import com.example.ustc.healthreps.model.Medicine;

import java.util.List;

/**
 * Created by CaoRuijuan on 12/21/15.
 */
public class PrelistContent {
    public List<Medicine> medicines;   //药品集合
    public String feibie;               //费别（自费、省医保、市医保、农合）
    public String contentPost;          //备注
}
