package com.example.music_01;
//登录界面的相关代码和实现
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_01.db.UserDbHelper;
import com.example.music_01.db.UserInfo;

public class LoginActivity extends AppCompatActivity {

    private EditText login_username;
    private EditText login_password;
    private SharedPreferences mSharedPreference;
    private CheckBox checkBox;
    private boolean is_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //初始化控件
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        checkBox = findViewById(R.id.checkbox);

        mSharedPreference=getSharedPreferences("user",MODE_PRIVATE);

        //记住密码的判断
        is_login = mSharedPreference.getBoolean("is_login", false);
        if(is_login)
        {
            String username = mSharedPreference.getString("username",null);
            String password = mSharedPreference.getString("password",null);
            login_username.setText(username);
            login_password.setText(password);
            checkBox.setChecked(true);
        }

        //控件点击事件
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到指定Activity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //登录操作
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = login_username.getText().toString();
                String password = login_password.getText().toString();
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "亲，请全都输入哦", Toast.LENGTH_SHORT).show();
                }else {
                    //登录
//                    String name = mSharedPreference.getString("username", null);
//                    String pwd = mSharedPreference.getString("password",null);
                    UserInfo login = UserDbHelper.getInstance(LoginActivity.this).login(username);
                    if(login!=null)
                    {
                        if(username.equals(login.getUsername()) && password.equals(login.getPassword()))
                        {
                            SharedPreferences.Editor edit = mSharedPreference.edit();
                            edit.putBoolean("is_login",is_login);
                            edit.putString("username",username);
                            edit.putString("password",password);
                            edit.commit();

                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(LoginActivity.this,StartActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "亲，输错了呢", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        Toast.makeText(LoginActivity.this, "亲，您还没注册呢", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        //checkbox点击事件
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_login = isChecked;
            }
        });

    }
}