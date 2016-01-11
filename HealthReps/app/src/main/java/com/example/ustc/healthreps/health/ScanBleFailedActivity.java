package com.example.ustc.healthreps.health;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ustc.healthreps.R;

/**
 * Created by HBL on 2015/11/10.
 */
public class ScanBleFailedActivity extends Activity{

    private Button rescanButton,helpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ble_failed);
        rescanButton=(Button)findViewById(R.id.scan_failed_rescan);
        helpButton=(Button)findViewById(R.id.scan_failed_help);

        rescanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        helpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }
}
