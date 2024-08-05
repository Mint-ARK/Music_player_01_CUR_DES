package com.example.music_01.core;
//TL和VP实现的分页，滑动依旧要靠RV，所以这里时下半的新Fragment的配置文件，主要是功能实现，界面没什么好说的
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_01.Dashboard.CustomItemDecoration;
import com.example.music_01.Dashboard.Data;
import com.example.music_01.Dashboard.ListViewAdapter;
import com.example.music_01.Home.ItemBean;
import com.example.music_01.R;

import java.util.ArrayList;
import java.util.List;

public class TabNewsFragment extends Fragment {

    private static final String ARG_PARAM = "title";

    private String title;
    private RecyclerView list44;
    private List<ItemBean> data;

    public TabNewsFragment() {

    }


    // 定义一个静态方法用于创建TabNewsFragment实例
    public static TabNewsFragment newInstance(String param) {
        // 创建一个新的TabNewsFragment对象
        TabNewsFragment fragment = new TabNewsFragment();

        // 创建一个新的Bundle对象，用于传递数据
        Bundle args = new Bundle();

        // 将参数添加到bundle中，键为ARG_PARAM
        args.putString(ARG_PARAM, param);

        // 将bundle添加到fragment中
        fragment.setArguments(args);

        // 返回创建的fragment实例
        return fragment;
    }

    // 当fragment创建时执行以下操作
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 调用父类的onCreate方法
        super.onCreate(savedInstanceState);

        // 检查是否有传递的参数
        if (getArguments() != null) {
            // 从参数中获取标题，并保存到title变量中
            title = getArguments().getString(ARG_PARAM);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_tab_news, container, false);
        list44 = inflate.findViewById(R.id.recycle_view5);
        initData();
        return inflate;

    }

    private void initData() {
        data = new ArrayList<>();
        for (int k = 0; k < Data.face.length; k++) {
            ItemBean data1 = new ItemBean();
            data1.face = Data.face[k];
           /* data1.picture1 = Data1.picture[k];
            data1.picture2 = Data1.picture[k];
            data1.picrure3 = Data1.picture[k];*/
            data1.instruct = "这是第" + k + "一条数据，主要目的是填满整个的textview。测试进行中...";
            /*data1.text1 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";
            data1.text2 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";
            data1.text3 = "这是第"+k+"一条数据，主要目的是填满整个的textview。测试进行中...";*/

            data.add(data1);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            list44.setLayoutManager(layoutManager1);
            // 建立新的适配器
            com.example.music_01.Dashboard.ListViewAdapter adapter1 = new ListViewAdapter(data); // 使用新的适配器
            // 设置新的Adapter到Recycle View中去
            list44.setAdapter(adapter1);
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
            list44.addItemDecoration(new CustomItemDecoration(spacingInPixels));

        }
    }
}