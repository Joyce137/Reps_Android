package com.example.administrator.ustc_health;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;



/**
 * Created by Administrator on 2015/10/26.
 */
public class FriendsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.friends_detail_collapsing_toolbar);
        collapsingToolbar.setTitle("APP");

        final ImageView imageView = (ImageView) findViewById(R.id.friends_detail_backdrop);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
           getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
       }
        //collapsingToolbar.setStatusBarScrim(getBaseContext().getResources().getDrawable(R.mipmap.per_image));
      //  Glide.with(this).load(R.mipmap.per_image).centerCrop().into(imageView);

    }


}
