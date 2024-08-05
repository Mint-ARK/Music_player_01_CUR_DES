package com.example.music_01;
//这部分实现的是广播功能哦，虽然不能雨露均沾，但是用广播做了一个小低电量提示
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class LowBatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("电量警告");
            builder.setMessage("电池电量低，请及时充电！此过程通过广播接收器完成。");
            builder.setPositiveButton("确定", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}