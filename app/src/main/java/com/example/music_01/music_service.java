package com.example.music_01;
import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class music_service extends Service
{
    private MediaPlayer mediaPlayer;
    private ArrayList<String> mp3FileNames;
    private int currentSongIndex = 0;
    private final IBinder binder = new LocalBinder();
    private Thread progressUpdateThread;

    public class LocalBinder extends Binder {
        music_service getService() {
            return music_service.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取传递给 Service 的数据，比如 MP3 文件列表
        mp3FileNames = new ArrayList<>();
        mp3FileNames = intent.getStringArrayListExtra("mp3FileNames");
        if (mp3FileNames == null) {
            // 如果 MP3 文件名列表为空，处理错误或者采取其他操作
            Toast.makeText(this, "列表为空哦", Toast.LENGTH_SHORT).show();
            return START_NOT_STICKY;
        }

        // 初始化 MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextMP3File();
            }
        });

        // 开始播放音乐

        //playFirstMP3File();
        if (!mediaPlayer.isPlaying()) {
            // 如果音乐尚未播放，则开始播放第一首歌曲
            playFirstMP3File();
        } /*else {
            // 如果音乐正在播放，直接播放下一首歌曲
            playNextMP3File();
        }*/

        //启动更新进度线程
        startUpdatingPosition();
        // 当 Service 被系统杀死时不会重新启动
        return START_NOT_STICKY;
    }
    //引用的函数
    public void Next()
    {
        playNextMP3File();
    }
    public void previous(){playPreviousMP3File();}
    public void pause(){pauseMP3File();}
    public void resume(){resumeMP3File();}

    public interface SongInfoListener {
        void onSongInfoChanged(String title, String artist);
        void onDurationChanged(String duration);
        void onPositionChanged(String position);
        void onProgressChanged(int progress);
    }

    private SongInfoListener songInfoListener;

    public void setSongInfoListener(SongInfoListener listener) {
        this.songInfoListener = listener;
    }

    public void getSongInfo(String songPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(songPath);
        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        // 通知监听器歌曲标题已改变
        if (songInfoListener != null) {
            songInfoListener.onSongInfoChanged(title, artist);
        }
    }



    private void playFirstMP3File() {
        if (mp3FileNames == null || mp3FileNames.isEmpty()) {
            // 如果没有找到任何 MP3 文件，则不执行播放操作
            return;
        }

        // 获取第一个 MP3 文件的文件名
        String firstMP3FileName = mp3FileNames.get(0);

        try {
            // 设置要播放的 MP3 文件路径
            File mp3File = new File(Environment.getExternalStorageDirectory(), firstMP3FileName);
            mediaPlayer.setDataSource(mp3File.getPath());

            // 获取歌曲信息
            getSongInfo(mp3File.getPath());



            // 准备播放
            mediaPlayer.prepare();

            // 开始播放
            mediaPlayer.start();


            // 当前播放的歌曲索引为0
            currentSongIndex = 0;
            // 获取歌曲的总时长
            int duration = mediaPlayer.getDuration();
            String durationStr = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
            // 通知监听器歌曲总时长已改变
            if (songInfoListener != null) {
                songInfoListener.onDurationChanged(durationStr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playNextMP3File() {
        if (mp3FileNames.isEmpty()) {
            // 如果没有找到任何 MP3 文件，则不执行播放操作
            return;
        }

        currentSongIndex++;
        if (currentSongIndex >= mp3FileNames.size()) {
            // 如果当前索引超出了数组范围，则从头开始播放
            currentSongIndex = 0;
        }

        String nextMP3FileName = mp3FileNames.get(currentSongIndex);

        try {
            mediaPlayer.reset();
            File mp3File = new File(Environment.getExternalStorageDirectory(), nextMP3FileName);
            mediaPlayer.setDataSource(mp3File.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            // 获取歌曲信息
            getSongInfo(mp3File.getPath());
            int duration = mediaPlayer.getDuration();
            String durationStr = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
            // 通知监听器歌曲总时长已改变
            if (songInfoListener != null) {
                songInfoListener.onDurationChanged(durationStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void playPreviousMP3File()
    {
        if (mp3FileNames.isEmpty()) {
            // 如果没有找到任何 MP3 文件，则不执行播放操作
            return;
        }

        currentSongIndex--;
        if (currentSongIndex < 0) {
            // 如果当前索引小于0，则从最后一首歌开始播放
            currentSongIndex = mp3FileNames.size() - 1;
        }

        String previousMP3FileName = mp3FileNames.get(currentSongIndex);

        try {
            mediaPlayer.reset();
            File mp3File = new File(Environment.getExternalStorageDirectory(), previousMP3FileName);
            mediaPlayer.setDataSource(mp3File.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            // 获取歌曲信息
            getSongInfo(mp3File.getPath());
            // 获取歌曲的总时长
            int duration = mediaPlayer.getDuration();
            String durationStr = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
            // 通知监听器歌曲总时长已改变
            if (songInfoListener != null) {
                songInfoListener.onDurationChanged(durationStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //暂停播放
    private void pauseMP3File() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        // 停止更新播放位置
        if (progressUpdateThread != null) {
            progressUpdateThread.interrupt();
        }

    }

    // 继续播放
    private void resumeMP3File() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // 开始更新播放位置
            startUpdatingProgress();
        }
    }
    private Thread positionUpdateThread;

    private void startUpdatingPosition() {
        positionUpdateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        String currentPositionStr = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                                TimeUnit.MILLISECONDS.toSeconds(currentPosition) % TimeUnit.MINUTES.toSeconds(1));
                        if (songInfoListener != null) {
                            songInfoListener.onPositionChanged(currentPositionStr);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        positionUpdateThread.start();
    }
    public void startUpdatingProgress() {
        progressUpdateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int progress = mediaPlayer.getCurrentPosition();
                    if (songInfoListener != null) {
                        songInfoListener.onProgressChanged(progress);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        progressUpdateThread.start();
    }
    public void seekTo(int progress) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(progress);
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
