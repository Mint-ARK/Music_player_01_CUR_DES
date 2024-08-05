package com.example.music_01;

import static com.youth.banner.util.LogUtils.TAG;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MusicDataLoader {

    public static ArrayList<LocalMusicBean> loadLocalMusicData(Context context) {

        ArrayList<LocalMusicBean> mDatas = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        //2.获取本地音乐存储的Uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.i(TAG, "loadLocalMusicData: " + uri);
        //3 开始查询地址
        Cursor cursor = resolver.query(uri, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        Log.i(TAG, "loadLocalMusicData: " + cursor);
        //4.遍历Cursor
        int id = 0;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
            Log.i(TAG, "loadLocalMusicData:123   " + type);

            @SuppressLint("Range") String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            @SuppressLint("Range") String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            Log.i(TAG, "loadLocalMusicData:456   " + path);
            @SuppressLint("Range") long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));
            //将一行当中的数据封装到对象当中
            LocalMusicBean bean = new LocalMusicBean(sid, song, singer, album, time, path, "");
            mDatas.add(bean);
        }
        int num = id > 0 ? id - 1 : -2;
        Log.i(TAG, "loadLocalMusicData: " + mDatas);
        //数据源变化，提示适配器更新
        LocalMusicAdapter adapter = null;
        adapter.notifyDataSetChanged();
        return mDatas;
    }
}
