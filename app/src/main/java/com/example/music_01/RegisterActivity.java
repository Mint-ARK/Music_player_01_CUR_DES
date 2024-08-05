package com.example.music_01;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_01.db.UserDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;

    //private SharedPreferences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        //初始化控件
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

       // mSharedPreference=getSharedPreferences("user",MODE_PRIVATE);

        findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //销毁本界面，否则炸内存
                finish();
            }
        });




        //点击注册
        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password))
                {
                    Toast.makeText(RegisterActivity.this, "请完全输入哦", Toast.LENGTH_SHORT).show();
                }else {
//                    SharedPreferences.Editor edit = mSharedPreference.edit();
//                    edit.putString("username",username);
//                    edit.putString("password",password);

                    int vew = UserDbHelper.getInstance(RegisterActivity.this).register(username, password, "功能未开发");
                    if(vew>0)
                    {

                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();//销毁页面
                    }


                }
            }
        });
    }
}