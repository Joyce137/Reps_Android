package com.example.ustc.healthreps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.ustc.healthreps.model.Users;

public class MyProFileActivity extends AppCompatActivity implements View.OnClickListener {
    TextView nameText;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        nameText = (TextView)findViewById(R.id.usernameText);
        nameText.setText(Users.sLoginUsername);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("我");
    }

    @Override
    public void onClick(View view) {

    }
}
