package com.example.ustc.healthreps.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ustc.healthreps.R;

import java.io.FileNotFoundException;

public class Picture_select extends Activity {

    private String picPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.picture_select);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RESULT_CANCELED);

        Button send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       // Toast.makeText(getApplication(),"onResult",Toast.LENGTH_SHORT).show();
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Log.e("uri",uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);

                //get the picture path
                String []proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri,proj,null,null,null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                picPath = cursor.getString(column_index);
                Toast.makeText(getApplication(), picPath, Toast.LENGTH_SHORT).show();
            }catch (FileNotFoundException e){
                Log.e("Exception",e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
