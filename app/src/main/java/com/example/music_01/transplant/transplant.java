package com.example.music_01.transplant;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_01.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class transplant extends AppCompatActivity implements View.OnClickListener
{

    ImageView nextIv,playIv,lastIv,albumIv;
    TextView singerTv,songTv;
    RecyclerView musicRv;
    //    数据源
    List<LocalMusicBean> mDatas;
    private LocalMusicAdapter adapter;

    //    记录当前正在播放的音乐的位置
    int currnetPlayPosition = -1;
    //    记录暂停音乐时进度条的位置
    int currentPausePositionInSong = 0;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mediaPlayer = new MediaPlayer();
        mDatas = new ArrayList<>();
//     创建适配器对象
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
//        设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
//        加载本地数据源
        loadLocalMusicData();
//        设置每一项的点击事件
        setEventListener();
    }

    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currnetPlayPosition = position;
                LocalMusicBean musicBean = mDatas.get(position);
                playMusicInMusicBean(musicBean);
            }
        });
    }

    public void playMusicInMusicBean(LocalMusicBean musicBean) {
        /*根据传入对象播放音乐*/
        //设置底部显示的歌手名称和歌曲名
        singerTv.setText(musicBean.getSinger());
        songTv.setText(musicBean.getSong());
        stopMusic();
//                重置多媒体播放器
        mediaPlayer.reset();
//                设置新的播放路径
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            String albumArt = musicBean.getAlbumArt();
            Log.i("lsh123", "playMusicInMusicBean: albumpath=="+albumArt);
            Bitmap bm = BitmapFactory.decodeFile(albumArt);
            Log.i("lsh123", "playMusicInMusicBean: bm=="+bm);
            albumIv.setImageBitmap(bm);
            playMusic();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 点击播放按钮播放音乐，或者暂停从新播放
     * 播放音乐有两种情况：
     * 1.从暂停到播放
     * 2.从停止到播放
     * */
    private void playMusic() {
        /* 播放音乐的函数*/
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            if (currentPausePositionInSong == 0) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
//                从暂停到播放
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
            }
            playIv.setImageResource(R.mipmap.icon_pause);
        }
    }
    private void pauseMusic() {
        /* 暂停音乐的函数*/
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            currentPausePositionInSong = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }
    private void stopMusic() {
        /* 停止音乐的函数*/
        if (mediaPlayer!=null) {
            currentPausePositionInSong = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.mipmap.icon_play);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void loadLocalMusicData() {
        // 1.获取ContentResolver对象
        ContentResolver resolver = getContentResolver();
        // 2.获取本地音乐存储的Uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // 3 开始查询地址
        Cursor cursor = resolver.query(uri, null, null, null, null);
        // 4.遍历Cursor
        int id = 0;
        while (cursor.moveToNext()) {
            int columnIndex;

            // 检查 TITLE 列
            columnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            String song = "";
            if (columnIndex >= 0) {
                song = cursor.getString(columnIndex);
            }

            // 检查 ARTIST 列
            columnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            String singer = "";
            if (columnIndex >= 0) {
                singer = cursor.getString(columnIndex);
            }

            // 检查 ALBUM 列
            columnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            String album = "";
            if (columnIndex >= 0) {
                album = cursor.getString(columnIndex);
            }

            id++;
            String sid = String.valueOf(id);

            // 检查 DATA 列
            columnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            String path = "";
            if (columnIndex >= 0) {
                path = cursor.getString(columnIndex);
            }

            // 检查 DURATION 列
            columnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            long duration = 0;
            if (columnIndex >= 0) {
                duration = cursor.getLong(columnIndex);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));

            // 检查 ALBUM_ID 列
            columnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            String album_id = "";
            if (columnIndex >= 0) {
                album_id = cursor.getString(columnIndex);
            }
            String albumArt = getAlbumArt(album_id);

            // 将一行当中的数据封装到对象当中
            LocalMusicBean bean = new LocalMusicBean(sid, song, singer, album, time, path,albumArt);
            mDatas.add(bean);
        }
        // 数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
    }



    private String getAlbumArt(String album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + album_id),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }
    private void initView() {
        /* 初始化控件的函数*/
        nextIv = findViewById(R.id.local_music_bottom_iv_next);
        playIv = findViewById(R.id.local_music_bottom_iv_play);
        lastIv = findViewById(R.id.local_music_bottom_iv_last);
        albumIv = findViewById(R.id.local_music_bottom_iv_icon);
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        musicRv = findViewById(R.id.local_music_rv);
        nextIv.setOnClickListener((View.OnClickListener) this);
        lastIv.setOnClickListener((View.OnClickListener) this);
        playIv.setOnClickListener((View.OnClickListener) this);
    }


    public void onClick(View v) {

                if (currnetPlayPosition == -1) {
//                    并没有选中要播放的音乐
                    Toast.makeText(this,"请选择想要播放的音乐",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()) {
//                    此时处于播放状态，需要暂停音乐
                    pauseMusic();
                }else {
//                    此时没有播放音乐，点击开始播放音乐
                    playMusic();

        }
    }


}