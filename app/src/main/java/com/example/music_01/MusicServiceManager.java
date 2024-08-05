package com.example.music_01;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class MusicServiceManager {
    private static MusicServiceManager instance;
    private MediaPlayer mediaPlayer;

    private MusicServiceManager() {
        mediaPlayer = new MediaPlayer();
    }

    public static synchronized MusicServiceManager getInstance() {
        if (instance == null) {
            instance = new MusicServiceManager();
        }
        return instance;
    }

    public void startMusicService(ArrayList<String> mp3FileNames, Context context) {
        if (!mediaPlayer.isPlaying()) {
            // 启动音乐播放服务的代码
            Intent serviceIntent = new Intent(context, music_service.class);
            serviceIntent.putStringArrayListExtra("mp3FileNames", mp3FileNames);
            context.startService(serviceIntent);
        }
    }


}