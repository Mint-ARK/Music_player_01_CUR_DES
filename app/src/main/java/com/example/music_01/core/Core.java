package com.example.music_01.core;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.music_01.MainActivity;
import com.example.music_01.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class Core extends Fragment {

    private ImageView viewById;
    private String[] titles = {"最近播放", "收藏"};
    private TabLayout tab_layout;

    private ViewPager2 viewPager1;
    private ImageView drawer1;
    private ImageView search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate =  inflater.inflate(R.layout.fragment_core, container, false);
        //初始化控件
        tab_layout = inflate.findViewById(R.id.tab_layout);
        viewPager1 = inflate.findViewById(R.id.viewPager);
        drawer1 = inflate.findViewById(R.id.more);
        search = inflate.findViewById(R.id.search_image_view);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            //做一下小图标的点击事件
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Toast.makeText(mainActivity, "亲，网络搜索还没开发，本地搜索不一定准，祝您好运哦", Toast.LENGTH_SHORT).show();
            }
        });
        drawer1.setOnClickListener(new View.OnClickListener() {
            @Override
            //这里加了个点击事件，和抽屉同步弹出，毕竟，没时间做了，先这样吧，知道有这功能就行了
            public void onClick(View v) {
                // 打开抽屉
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.openDrawer();
                Toast.makeText(mainActivity, "亲，别点了，还没做呢QAQ", Toast.LENGTH_SHORT).show();

            }
        });
        //这里搓了一个VP，用来分割core的下一半，做了个横切滑动，数据库随便绑了一个，没时间做，知道有这功能能显示就得了
        viewPager1.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                String title = titles[position];
                TabNewsFragment tabNewsFragment = TabNewsFragment.newInstance(title);
                return tabNewsFragment;

            }

            @Override
            public int getItemCount() {
                return titles.length;
            }
        });

        //tablayout点击事件

        tab_layout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager1.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //viewPager和tab_layout关联在一起
        //单纯的VP或者TL不能够实现，这部分时用TL做的滑动分区
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tab_layout, viewPager1, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        });

        //这几话不能少
        tabLayoutMediator.attach();


        //设置ellipsis
        //别看我，这玩意随便找了个库就用了
        viewById = (ImageView) inflate.findViewById(R.id.more);
        ImageView imageView = viewById; // 请将my_image_view替换为你的ImageView的ID
        Drawable drawable = getResources().getDrawable(R.drawable.ellipsis1);
        drawable.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
        imageView.setImageDrawable(drawable);
        // 新添加的search图标
        ImageView searchImageView = (ImageView) inflate.findViewById(R.id.search_image_view);
        Drawable searchDrawable = getResources().getDrawable(R.drawable.search_line1);
        searchDrawable.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
        searchImageView.setImageDrawable(searchDrawable);

        return inflate;
    }
}
