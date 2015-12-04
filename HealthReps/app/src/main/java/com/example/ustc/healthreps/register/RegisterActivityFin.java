package com.example.ustc.healthreps.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.ustc.healthreps.LoginActivity;
import com.example.ustc.healthreps.R;

public class RegisterActivityFin extends AppCompatActivity {
    private Button loginBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_fin);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.reg_fin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");
        loginBtn = (Button)findViewById(R.id.loginIn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivityFin.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
