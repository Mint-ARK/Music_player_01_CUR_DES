package com.example.music_01.core;
//这里时CORE的Adapter也是用的RecycleView，用来实现滑动，基本上所有的RV都使用的同一个RV模板，自动生成，最多在
//OBVH方法里做一下点击事件，意思意思就得了。实在没时间了
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_01.Home.ItemBean;
import com.example.music_01.R;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<com.example.music_01.core.ListViewAdapter.InnerHolder>
{
    private final List<ItemBean> mData;

    public ListViewAdapter (List<ItemBean> data )
    {
        this.mData = data;
    }

    @NonNull//创建条目View
    @Override
    public com.example.music_01.core.ListViewAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //传入Item的布局

        //拿到View,建立ViewHolder
        View view = View.inflate(parent.getContext(), R.layout.fragment_fragment_itme,null);
        return new com.example.music_01.core.ListViewAdapter.InnerHolder(view);
    }

    @Override//用于绑定Holder，一般用来设置数据
    public void onBindViewHolder(@NonNull com.example.music_01.core.ListViewAdapter.InnerHolder holder, int position) {
        //设置数据
        holder.setHolder(mData.get(position));

    }

    @Override
    //返回条目个数
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder
    {

        private TextView text1;
        private TextView text;
        private ImageView picture1;
        private ImageView picture;
        private ImageView face;
        private TextView instruct;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            //找到条目控件
            face = (ImageView) itemView.findViewById(R.id.ffragment_image);

            instruct = (TextView)itemView.findViewById(R.id.ffragment_text);

        }


        //这个方法用于设置数据
        public void setHolder(ItemBean itemBean)
        {
            //开始设置数据
            face.setImageResource(itemBean.face);
//            picture.setImageResource(itemBean.picture);
//            picture1.setImageResource(itemBean.picture1);
            instruct.setText(itemBean.instruct);
//            text.setText(itemBean.text);
//            text1.setText(itemBean.text1);

        }
    }
}
