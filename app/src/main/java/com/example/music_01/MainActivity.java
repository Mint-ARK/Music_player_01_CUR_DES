package com.example.music_01;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.music_01.Dashboard.Dashboard;
import com.example.music_01.Home.Home;
import com.example.music_01.Notifications.Notifications;
import com.example.music_01.core.Core;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout; // 新添加的DrawerLayout
    private NavigationView navigationView; // 新添加的NavigationView

    private LinearLayout linearLayout;

    private Home home;
    private Core core;
    private Dashboard dashboard;
    private Notifications notifications;
    private LowBatteryReceiver lowBatteryReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //初始化控件
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout); // 初始化DrawerLayout
        navigationView = findViewById(R.id.nav_view); // 初始化NavigationView
        linearLayout = findViewById(R.id.linear_layout22); // 你需要替换为实际的ID
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(new LowBatteryReceiver(), filter);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理点击事件
                Intent intent= new Intent(MainActivity.this,Music_core_Acivity.class);
                startActivity(intent);
                //finish();//是否销毁主界面，暂时不确定，虽然可以重启节省性能，但是会导致临时数据丢失
            }
        });

        // 设置NavigationView的菜单项点击监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // 处理菜单项的点击事件
                // ...
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        selectedFragment(0); //默认第一个选中

        //点击事件
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.home) {
                    selectedFragment(0);
                } else if (menuItem.getItemId()==R.id.dashboard) {
                    selectedFragment(1);
                } else if (menuItem.getItemId()==R.id.notifications) {
                    selectedFragment(2);
                }else {
                    selectedFragment(3);
                }
                return true;
            }
        });
        //检查安卓等级，防止状态栏和显示控件冲突
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
        ArrayList<String> mp3FileNames = getIntent().getStringArrayListExtra("mp3FileNames");

        Dashboard dashboardFragment = new Dashboard();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("mp3FileNames", mp3FileNames);
        dashboardFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mp3Namebox, dashboardFragment);
        fragmentTransaction.commit();

    }
    protected void onDestroy() {
        super.onDestroy();

        // 取消注册广播接收器
        try {
            unregisterReceiver(lowBatteryReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    //Fragment切换逻辑
    private void selectedFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        if(position == 0) {
            if(home == null) {
                home = new Home();
                fragmentTransaction.add(R.id.fragment_container,home);
            } else {
                fragmentTransaction.show(home);
            }
        } else if (position == 1) {
            if (dashboard == null) {
                dashboard = new Dashboard();
                fragmentTransaction.add(R.id.fragment_container,dashboard);
            }else {
                fragmentTransaction.show(dashboard);
            }
        } else if (position==2) {
            if(notifications==null) {
                notifications = new Notifications();
                fragmentTransaction.add(R.id.fragment_container,notifications);
            }else {
                fragmentTransaction.show(notifications);
            }
        } else if (position==3) {
            if(core == null) {
                core = new Core();
                fragmentTransaction.add(R.id.fragment_container,core);
            }else {
                fragmentTransaction.show(core);
            }
        }
        //提交
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if(home != null) {
            fragmentTransaction.hide(home);
        }
        if(core!=null) {
            fragmentTransaction.hide(core);
        }
        if(dashboard!=null) {
            fragmentTransaction.hide(dashboard);
        }
        if(notifications!=null) {
            fragmentTransaction.hide(notifications);
        }
    }


    // 新添加的openDrawer方法
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

}
