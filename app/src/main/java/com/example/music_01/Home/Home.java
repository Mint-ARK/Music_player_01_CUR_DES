package com.example.music_01.Home;
//主界面函数，设置Banner,抽屉，和横向的RV
import static com.example.music_01.R.id.recycle_view;
import static com.example.music_01.R.id.recycle_view1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_01.MainActivity;
import com.example.music_01.R;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import com.example.music_01.Home.BannerViewAdapter;
import com.youth.banner.indicator.CircleIndicator;

public class Home extends Fragment {

    private ArrayList<infoBean> banners;
    private Banner banner;
    private List<ItemBean> data;
    private List<ItemBean> mData;
    private RecyclerView list; // 将list声明为类的成员变量
    private RecyclerView list1;
    private List<ItemBean> mData1;
    private ImageView drawer3;
    private ImageView cinema;

    public Home() {
        //v1,v2这些是轮播图片，可用自己本地的替换掉。
        banners = new ArrayList<>();
        banners.add(new infoBean(R.drawable.v2));
        banners.add(new infoBean(R.drawable.v3));
        banners.add(new infoBean(R.drawable.v4));
        banners.add(new infoBean(R.drawable.v5));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View inflate =  inflater.inflate(R.layout.fragment_home, container, false);

        //查找组件ID
        Banner banner = inflate.findViewById(R.id.banner);
        list = inflate.findViewById(recycle_view); // 在这里初始化list
        list1 = inflate.findViewById(recycle_view1);
        drawer3 = inflate.findViewById(R.id.more2);
        cinema = inflate.findViewById(R.id.cinema);

        cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Toast.makeText(mainActivity, "亲，听歌识曲开发中~", Toast.LENGTH_SHORT).show();

            }
        });

        drawer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开抽屉
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.openDrawer();
                Toast.makeText(mainActivity, "亲，别点了，还没做呢QAQ", Toast.LENGTH_SHORT).show();
            }
        });

        //添加生命周期
        banner.addBannerLifecycleObserver(this).setAdapter(new BannerViewAdapter(banners, this))
                //添加指示器
                .setIndicator(new CircleIndicator(getContext()));
        Banner banner1 = banner.setBannerRound(60);

        // 初始化数据
        initDta();
        initData1();

        return inflate;
    }

    private void initData1()
    {
        mData1 = new ArrayList<>();
        for (int k = 0;k<Data1.picture.length;k++)
        {
            ItemBean data1 = new ItemBean();
            data1.picture = Data1.picture[k];
            data1.picture1 = Data1.picture[k];
            data1.picture2 = Data1.picture[k];
            data1.picrure3 = Data1.picture[k];
            data1.text = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";
            data1.text1 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";
            data1.text2 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";
            data1.text3 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";

            mData1.add(data1);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            list1.setLayoutManager(layoutManager1);
            // 建立新的适配器
            ListViewAdapter1 adapter1 = new ListViewAdapter1(mData1); // 使用新的适配器
            // 设置新的Adapter到Recycle View中去
            list1.setAdapter(adapter1);
        }
    }


    private void initDta()
    {
        //List<DataBean>---->adapter---->setAdapter---->显示数据
        //创建模拟数据
        //创建数据集合
        mData = new ArrayList<>();
        for(int i = 0;i<Datas.icons.length;i++)
        {
            ItemBean data = new ItemBean();
            data.icon = Datas.icons[i];//此处为图像的重定义，图像或者文本框内容与设定的item中的设定是没有关系的，item中仅用于辅助识别控件。真正的图片来自Data数据类
            data.title = "第"+ i +"条";//此处重定义文本，原理同上，不过真正的文本来自此处。
            //添加到集合中
            mData.add(data);

        }
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        list.setLayoutManager(layoutManager);
        //建立适配器
        ListViewAdapter adapter = new ListViewAdapter(mData);
        //设置值Adapter到Recycle View中去
        list.setAdapter(adapter);
    }
}

