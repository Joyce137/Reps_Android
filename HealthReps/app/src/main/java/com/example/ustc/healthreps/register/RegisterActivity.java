package com.example.ustc.healthreps.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.serverInterface.UserInfo;
import com.example.ustc.healthreps.ui.Picture_select;
import com.example.ustc.healthreps.utils.AppManager;
import com.example.ustc.healthreps.utils.Utils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.UnknownFormatConversionException;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private Button nextbutton;
    private CircleImageView imageView;
    EditText username, Email, Password1, Password2, Realname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        //添加到Activity集合
//        AppManager.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        username=(EditText)findViewById(R.id.usernameText);
        Email=(EditText)findViewById(R.id.EmailText);
        Password1=(EditText)findViewById(R.id.PasswordText1);
        Password2=(EditText)findViewById(R.id.PasswordText2);
        Realname=(EditText)findViewById(R.id.RealnameText);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.reg_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");

        imageView = (CircleImageView)findViewById(R.id.headPhoto);
        //获取资源文件的绝对路径
        Users.sRegisterUser.imagePath = Utils.copyDefaultHeadPhoto(this);    //默认头像

//        getResources().getDrawable(R.mipmap.ic_perimg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, RESULT_CANCELED);
            }
        });

        nextbutton=(Button)findViewById(R.id.reg_button);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check(Users.sRegisterUser) != null) {
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, RegisterActivityPh.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            String imgPath = uri.getPath();
//                    Utils.getRealFilePath(getApplicationContext(),uri);
            Users.sRegisterUser.imagePath = imgPath;
            Log.e("imgPath",imgPath.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                imageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                Log.e("Exception",e.getMessage(),e);
            }
        }
    }

    //检测RegisterActivity中的EditView内容是否合法
    private UserInfo check(UserInfo userInfo){
        int[] anArray;
        int[] bnArray;
        int i,m;
        int j=0,n=0,k=0,l;

        String t_Username= username.getText().toString().trim();
        String s =t_Username;
        bnArray=new int[s.length()];
        anArray=new int[s.length()];
        if(t_Username.length()<0||t_Username.length()>20)
        {
            Toast.makeText(getApplication(),"用户名过长，应该小于20",Toast.LENGTH_LONG).show();
            return null;
        }
        //转成ASCii码
        for(i=0;i<s.length();i++)
        {
            anArray[i]=(int) s.charAt(i);
        }
        //判断第一个字符是否是字母开头
        if(anArray[0]>=65&anArray[0]<=90||anArray[0]>=97&
                anArray[0]<=122)
            l=0;
        else l=1;
        //检查用户名中是否包含A~Z，a~z的字
        for(i=0;i<s.length();i++)
            if(anArray[i]<65||anArray[i]>90&anArray[i]<97
                    ||anArray[i]>122)
            {
                bnArray[j]=anArray[i];
                j++;

            }
        //检测数字
        for(m=0;m<bnArray.length;m++)
        {
            if(bnArray[m]!=0&(bnArray[m]>47&bnArray[m]<58))
                n++;
            else if(bnArray[m]!=0&(bnArray[m]<48||bnArray[m]>57))
                k++;
        }
        if(k>0)
        {
            Toast.makeText(getApplication(), "用户名非法，用户名应包含字母或者字母和数字", Toast.LENGTH_LONG).show();
            return null;
        }
        if(n==s.length())
        {
            Toast.makeText(getApplication(),"用户名非法，用户名不能全为数字",Toast.LENGTH_LONG).show();
            return null;
        }

        if(l==1)
        {
            Toast.makeText(getApplication(),"用户名非法，用户名需以字母为开始",Toast.LENGTH_LONG).show();
            return null;
        }
        //检查Email地址是否合法
        String t_Email=Email.getText().toString().trim();
        if(t_Email.length()==0||t_Email.length()>40)
        {
            Toast.makeText(getApplicationContext(),"Email地址不能为空或过长",Toast.LENGTH_LONG).show();
            return null;
        }

        //        if(Utils.checkEmail(t_Email)==false)

        //正则表达式
        String emailStr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(emailStr);
        if(!pattern.matcher(t_Email).matches()){
            Toast.makeText(getApplication(),"Email地址非法",Toast.LENGTH_SHORT).show();
            return null;
        }

        //检测密码合法性
        String t_Password1=Password1.getText().toString().trim();
        if(t_Password1.length()<=5||t_Password1.length()>20)
        {
            Toast.makeText(getApplication(),"密码长度需大于6位小于20位",Toast.LENGTH_LONG).show();
            return null;
        }
        if(Utils.checkUsername(t_Password1)==false)
        {
            Toast.makeText(getApplication(),"密码不合法",Toast.LENGTH_LONG).show();
            return null;
        }
        //检测确认密码和第一个密码是否一致
        String t_Password2=Password2.getText().toString().trim();
        if(t_Password1.equals(t_Password2)==false)
        {
            Toast.makeText(getApplication(),"两次密码不一致",Toast.LENGTH_LONG).show();
            return null;
        }
        //检测实际姓名是否为空
        String t_Realname=Realname.getText().toString().trim();
        if(t_Realname.length()==0||t_Realname.length()>40)
        {
            Toast.makeText(getApplication(),"真实姓名不能为空",Toast.LENGTH_LONG).show();
            return null;
        }

        //填充UserInfo
        try {
            userInfo.loginName = t_Username.getBytes("GBK");
            userInfo.email = t_Email;
            userInfo.password = t_Password2.getBytes("GBK");
            userInfo.realName = t_Realname.getBytes("GBK");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return userInfo;
    }
}
