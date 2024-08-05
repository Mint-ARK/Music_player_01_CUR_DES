package com.example.music_01.Dashboard;
//Dashboard二号界面的主函数代码，主要还是处理RV的相关数据
import static android.content.Intent.getIntent;
import static android.content.Intent.makeMainActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_01.Home.Data1;
import com.example.music_01.Home.ItemBean;
import com.example.music_01.Home.ListViewAdapter1;
import com.example.music_01.LocalMusicBean;
import com.example.music_01.MainActivity;
import com.example.music_01.MusicActivity;
import com.example.music_01.MusicDataLoader;
import com.example.music_01.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Dashboard extends Fragment
{
    private RecyclerView list2;
    private RecyclerView list21;
    private RecyclerView list22;
    private List<ItemBean> data;
    private ImageView drawer2;
    private ImageView none;
    private TextView zhujiemiandicengwenben;
    private ArrayList<String> mp3FileNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        Bundle bundle = getArguments();
        list22 = inflate.findViewById(R.id.recycle_view3);
        drawer2 = inflate.findViewById(R.id.more1);
        none = inflate.findViewById(R.id.none);
        //zhujiemiandicengwenben = inflate.findViewById(R.id.zhujiemiandicengwenben);
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Toast.makeText(mainActivity, "亲，功能还没想好呢~", Toast.LENGTH_SHORT).show();
            }
        });
        drawer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开抽屉
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.openDrawer();
                Toast.makeText(mainActivity, "亲，别点了，还没做呢QAQ", Toast.LENGTH_SHORT).show();
            }
        });
        initData();
        return inflate;

    }
    private void accessSDCard() {
        // 在这里实现访问SD卡的逻辑
        //Toast.makeText(this, "访问SD卡", Toast.LENGTH_SHORT).show();
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

    private void initData()
    {
        // 获取从 Music_core_Activity 传递过来的 mp3FileNames
        //accessSDCardAndRetrieveMP3Files();
        //ArrayList<LocalMusicBean> musicData = MusicDataLoader.loadLocalMusicData(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mp3FileNames = bundle.getStringArrayList("mp3FileNames");
        }

        //if (mp3FileNames != null) {
            // 将 ArrayList 转换为数组
            //String[] mp3FileNamesArray = mp3FileNames.toArray(new String[0]);

            data = new ArrayList<>();
            for (int k=0;k<Data.face.length;k++) {
                ItemBean data1 = new ItemBean();
                data1.face = Data.face[k]; // 你可以根据需要设置 face 的值

                // 将 MP3 文件名设置为 instruct
                data1.instruct = music_name.music_name[k];

                data.add(data1);
            }

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            list22.setLayoutManager(layoutManager1);
            // 建立新的适配器
            ListViewAdapter adapter1 = new ListViewAdapter(data); // 使用新的适配器
            // 设置新的Adapter到Recycle View中去
            list22.setAdapter(adapter1);
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
            list22.addItemDecoration(new CustomItemDecoration(spacingInPixels));
       // }
        //else {
          //  MainActivity mainActivity = (MainActivity) getActivity();
          //  Toast.makeText(mainActivity, "亲，别点了，还没做呢QAQ", Toast.LENGTH_SHORT).show();
        //}
    }
    //这个是音乐播放的传入数据然后更新界面，但是这音乐的底层写废了没办法重构，先留着了
    /*public void updateViews(String text1, String text2, int imageResId) {
        TextView textView1 = (TextView) getView().findViewById(R.id.zhujiemiandicengwenben);
        TextView textView2 = (TextView) getView().findViewById(R.id.bofangyemianwenben);
        CircleImageView circleImageView1 = (CircleImageView) getView().findViewById(R.id.zhujiemiandicengtupian);
        CircleImageView circleImageView2 = (CircleImageView) getView().findViewById(R.id.bofangyemiantupian);

        textView1.setText(text1);
        textView2.setText(text2);
        circleImageView1.setImageResource(imageResId);
        circleImageView2.setImageResource(imageResId);
    }*/

}