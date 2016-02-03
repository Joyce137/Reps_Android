package com.example.ustc.healthreps.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.FileSelectAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class File_select extends ListActivity {

    private List<String> items = null;
    private List<String> paths = null;
    private String rootPath = "/";
    private String curPath = "/";
    private TextView mPath;

    private final static String TAG = "bb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.file_select);

        initView();
        getFileDir(rootPath);
    }

    public void initView(){
        mPath = (TextView) findViewById(R.id.mPath);
        Button btnConfirm = (Button)findViewById(R.id.buttonConfirm);
        Button btnCancle = (Button)findViewById(R.id.buttonCancle);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wirtefilePath(mPath.getText().toString());
                finish();
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void wirtefilePath(String filePath){

        try{
            FileOutputStream fout = new FileOutputStream("/sdcard/filename.txt");
            byte [] bytes = filePath.getBytes();
            fout.write(bytes);
            fout.close();

            Toast.makeText(getApplication(),"file name saved success!",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplication(),"write to file fail!!",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void getFileDir(String filePath) {
        mPath.setText(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();

        if (!filePath.equals(rootPath)) {
            items.add("b1");
            paths.add(rootPath);
            items.add("b2");
            paths.add(f.getParent());
        }
        if(files==null){
            Toast.makeText(getApplication(),"the directory is empty..",Toast.LENGTH_SHORT).show();
        }
        else{
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                items.add(file.getName());
                paths.add(file.getPath());
            }
            setListAdapter(new FileSelectAdapter(this, items, paths));
        }

    }
    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);

        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        startActivity(intent);
    }

    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(paths.get(position));
        if (file.isDirectory()) {
            curPath = paths.get(position);
            getFileDir(paths.get(position));
        } else {
            openFile(file);
        }
    }
}
