package com.example.music_01;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Music_core_Acivity extends AppCompatActivity {

    private Toolbar toolbar_back;
    private ImageView pause;
    private ImageView next;
    private ImageView previous;
    private SeekBar seekBar;
    private RecyclerView music_list;
    private ArrayList mSongArrayList;
    private int curSongIndex;
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 101;
    private boolean isMusicPlaying = false; // 播放状态变量
    private boolean isPlaying = false;
    private MediaPlayer mediaPlayer;
    private int currentSongIndex = 0;

    private music_service musicService;
    private boolean isBound = false;
    private ObjectAnimator rotationAnimator;

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    private ArrayList<String> mp3FileNames = new ArrayList<>();
    AlertDialog mPermissionDialog;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            music_service.LocalBinder binder = (music_service.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;

            // 现在你可以调用 Service 中的方法了
            //这里什么都不要做，保证回调是空的，这样就不会混响了
            //musicService.Next();
            startRotationAnimation(); // 启动旋转动画
            musicService.startUpdatingProgress(); // 启动更新进度的线程
            musicService.setSongInfoListener(new music_service.SongInfoListener() {
                @Override
                public void onSongInfoChanged(String title, String artist) {
                    TextView songTitleTextView = findViewById(R.id.bofangyemianwenben); // 你需要替换为实际的ID
                    songTitleTextView.setText(title);
                    TextView artistTextView = findViewById(R.id.artist); // 你需要替换为实际的ID
                    artistTextView.setText(artist);
                }
                @Override
                public void onDurationChanged(String duration) {
                    TextView timerEndTextView = findViewById(R.id.timer_end); // 你需要替换为实际的ID
                    timerEndTextView.setText(duration);
                    int durationInMilliseconds = convertDurationToMilliseconds(duration);
                    seekBar.setMax(durationInMilliseconds);
                }

                @Override
                public void onPositionChanged(String position) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView timerStartTextView = findViewById(R.id.timer_start); // 你需要替换为实际的ID
                            timerStartTextView.setText(position);
                        }
                    });
                }
                @Override
                public void onProgressChanged(final int progress) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(progress);
                        }
                    });
                }

            });
            // 获取当前播放的歌曲的标题
            String currentMP3FileName = mp3FileNames.get(currentSongIndex);
            File mp3File = new File(Environment.getExternalStorageDirectory(), currentMP3FileName);
            musicService.getSongInfo(mp3File.getPath());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    /*public Music_core_Acivity(SeekBar seekBar) {
        this.seekBar = seekBar;
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        // 恢复当前播放的歌曲的索引
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentSongIndex = prefs.getInt("currentSongIndex", 0);
        Intent intent = new Intent(this, music_service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 保存当前播放的歌曲的索引
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentSongIndex", currentSongIndex);
        editor.apply();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music_core_acivity);
        music_list = findViewById(R.id.recycle_view3);
        toolbar_back = findViewById(R.id.toolbar_back); // 你需要替换为实际的ID
        pause = findViewById(R.id.pause);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        //SeekBar seekBar = findViewById(R.id.seekbar); // 你需要替换为实际的ID
        seekBar = findViewById(R.id.seekbar);
        TextView songTitleTextView = findViewById(R.id.bofangyemianwenben); // 你需要替换为实际的ID
        songTitleTextView.setSelected(true);
        Intent intent = getIntent();
        curSongIndex = intent.getIntExtra("key_song_index", 0);
        mSongArrayList = (ArrayList) intent.getSerializableExtra("key_song_list");
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理点击事件
//                Intent intent= new Intent(MainActivity.this,Music_core_Acivity.class);
//                startActivity(intent);
                finish();//通过销毁来回到主界面
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && musicService != null) {
                    musicService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (musicService != null) {
                    musicService.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (musicService != null) {
                    musicService.resume();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService != null) {
                    if (isPlaying) {
                        musicService.pause();
                        isPlaying = false;
                        // 这里你可以更新pause ImageView为播放图标
                        pause.setImageResource(R.drawable.baseline_play_arrow_24);
                        // 暂停旋转动画
                        if (rotationAnimator == null) {
                            startRotationAnimation();
                        }
                        rotationAnimator.pause();
                    } else {
                        musicService.resume();
                        isPlaying = true;
                        // 这里你可以更新pause ImageView为暂停图标
                        pause.setImageResource(R.drawable.baseline_pause_24);
                        // 恢复或启动旋转动画
                        if (rotationAnimator == null) {
                            startRotationAnimation();
                        } else if (rotationAnimator.isPaused()) {
                            rotationAnimator.resume();
                        }
                    }
                }
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService != null) {
                    musicService.Next();
                }

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService != null) {
                    musicService.previous();
                }

            }
        });

        mediaPlayer = new MediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        } else if(!isServiceRunning(music_service.class))
        {
            accessSDCard();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }
        accessSDCardAndRetrieveMP3Files();
        Intent serviceIntent = new Intent(this,music_service.class);
        //stopService(serviceIntent);
        /*Dashboard dashboardFragment = new Dashboard();
        Bundle bundle = new Bundle();*/
        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.putStringArrayListExtra("mp3FileNames", mp3FileNames);

        if(isServiceRunning(music_service.class))
        {
            /*Intent band = new Intent(this, music_service.class);
            bindService(band, connection, Context.BIND_AUTO_CREATE);*/
            //bundle.putStringArrayList("mp3FileNames", mp3FileNames);
            //dashboardFragment.setArguments(bundle);
        }else {
            //传入参数，服务，启动！
            serviceIntent.putStringArrayListExtra("mp3FileNames", mp3FileNames);
            startService(serviceIntent);
            //bundle.putStringArrayList("mp3FileNames", mp3FileNames);
            //dashboardFragment.setArguments(bundle);
        }


    }
    //立大功的方法，用于进行检测当前的服务是否运行
    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
            if (runningServices != null) {
                for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
                    if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //请求权限
    private void checkAndRequestPermissions() {
        mPermissionList.clear();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                requestManageExternalStoragePermission();
            } else {
                accessSDCard();
            }
        } else if (!mPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, mPermissionList.toArray(new String[0]), REQUEST_CODE_PERMISSIONS);
        } else {
            accessSDCard();
        }

    }
    private void requestManageExternalStoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CODE_MANAGE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MANAGE_EXTERNAL_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "权限已获取", Toast.LENGTH_SHORT).show();
                    accessSDCard();
                } else {
                    showPermissionDialog();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;

        if (REQUEST_CODE_PERMISSIONS == requestCode) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    hasPermissionDismiss = true;
                    break;
                }
            }

            if (hasPermissionDismiss) {
                showPermissionDialog();
            } else {
                accessSDCard();
            }
        }
    }

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setCancelable(false)
                    .setPositiveButton("设置", (dialog, which) -> {
                        cancelPermissionDialog();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    })
                    .setNegativeButton("取消", (dialog, which) -> finish())
                    .create();
        }
        mPermissionDialog.show();
    }

    private void cancelPermissionDialog() {
        if (mPermissionDialog != null && mPermissionDialog.isShowing()) {
            mPermissionDialog.dismiss();
        }
    }

    private void accessSDCard() {
        // 在这里实现访问SD卡的逻辑
        Toast.makeText(this, "访问SD卡", Toast.LENGTH_SHORT).show();
        // 示例代码：列出SD卡上的文件
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            File[] files = sdCardDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // 打印文件名
                    Log.d("SDCardAccess", "File: " + file.getName());
                }
            }
        }
    }
    private void accessSDCardAndRetrieveMP3Files() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            File[] files = sdCardDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) {
                        // 找到了一个MP3文件，将其文件名存入数组
                        mp3FileNames.add(file.getName());
                    }
                }
            }
        }

        // 现在，mp3FileNames 数组中存储了SD卡中所有MP3文件的文件名
        for (String fileName : mp3FileNames) {
            Log.d("MP3Files", "MP3 File: " + fileName);
        }
    }
    private void startRotationAnimation() {
        CircleImageView circleView = findViewById(R.id.bofangyemiantupian); // 你需要替换为实际的ID
        rotationAnimator = ObjectAnimator.ofFloat(circleView, "rotation", 0f, 360f);
        rotationAnimator.setDuration(20000); // 设置动画持续时间为20秒
        rotationAnimator.setInterpolator(new LinearInterpolator()); // 设置动画插值器为线性
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE); // 设置动画重复次数为无限
        rotationAnimator.start(); // 启动动画
    }
    private int convertDurationToMilliseconds(String duration) {
        String[] parts = duration.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return (minutes * 60 + seconds) * 1000;
    }



}