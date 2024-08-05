package com.example.music_01.Notifications;
//动态的主界面窗口啦~
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.music_01.Dashboard.CustomItemDecoration;
import com.example.music_01.Home.ItemBean;
import com.example.music_01.MainActivity;
import com.example.music_01.R;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends Fragment
{
    private RecyclerView liset33;
    private List<ItemBean> data;
    private ImageView drawer4;
    private ImageView dongtai;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_notifications, container, false);
        liset33 = inflate.findViewById(R.id.recycle_view4);
        drawer4 = inflate.findViewById(R.id.more3);
        dongtai = inflate.findViewById(R.id.dongtai);
        dongtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Toast.makeText(mainActivity, "亲，动态功能考试前做不完了...", Toast.LENGTH_SHORT).show();
            }
        });
        drawer4.setOnClickListener(new View.OnClickListener() {
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

    private void initData()
    {
        data = new ArrayList<>();
        for (int k = 0; k< Data.recycle_image.length;k++)
        {
            ItemBean data1 = new ItemBean();
            //ItemBean data2 = new ItemBean();
            data1.recycle_image = com.example.music_01.Notifications.Data.recycle_image[k];
            data1.share_png = Data1.share_png[k];
           /* data1.picture1 = Data1.picture[k];
            data1.picture2 = Data1.picture[k];
            data1.picrure3 = Data1.picture[k];*/
            data1.text_main= "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";
            data1.username = "这是一条用户名呀";
            /*data1.text1 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";
            data1.text2 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";
            data1.text3 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";*/

            data.add(data1);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            liset33.setLayoutManager(layoutManager1);
            // 建立新的适配器
            com.example.music_01.Notifications.ListViewAdapter adapter1 = new ListViewAdapter(data); // 使用新的适配器
            // 设置新的Adapter到Recycle View中去
            liset33.setAdapter(adapter1);
            /*int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
            liset33.addItemDecoration(new CustomItemDecoration(spacingInPixels));*/

        }

    }
}