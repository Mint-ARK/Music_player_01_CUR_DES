package com.example.music_01.Notifications;
//就这个适配器类最纯真，我连点击事件都没做，哎，就是玩~（没时间了其实是）
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_01.Home.ItemBean;
import com.example.music_01.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListViewAdapter extends RecyclerView.Adapter<com.example.music_01.Notifications.ListViewAdapter.InnerHolder>
{
    private final List<ItemBean> mData;

    public ListViewAdapter (List<ItemBean> data )
    {
        this.mData = data;
    }

    @NonNull//创建条目View
    @Override
    public com.example.music_01.Notifications.ListViewAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //传入Item的布局

        //拿到View,建立ViewHolder
        View view = View.inflate(parent.getContext(), R.layout.item_plaza,null);
        return new com.example.music_01.Notifications.ListViewAdapter.InnerHolder(view);
    }

    @Override//用于绑定Holder，一般用来设置数据
    public void onBindViewHolder(@NonNull com.example.music_01.Notifications.ListViewAdapter.InnerHolder holder, int position) {
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

        private ImageView share_png;
        private TextView text_main;
        private TextView username;
        private CircleImageView recycle_image;
        private TextView text1;
        private TextView text;
        private ImageView picture1;
        private ImageView picture;
        private ImageView face;
        private TextView instruct;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            //找到条目控件
            recycle_image = (CircleImageView) itemView.findViewById(R.id.recycle_image);
            share_png = (ImageView) itemView.findViewById(R.id.share_png);
            text_main = (TextView) itemView.findViewById(R.id.text_main);
            username = (TextView) itemView.findViewById(R.id.username);

        }


        //这个方法用于设置数据
        public void setHolder(ItemBean itemBean)
        {
            //开始设置数据
            //face.setImageResource(itemBean.face);
//            picture.setImageResource(itemBean.picture);
//            picture1.setImageResource(itemBean.picture1);
            //instruct.setText(itemBean.instruct);
//            text.setText(itemBean.text);
//            text1.setText(itemBean.text1);
            recycle_image.setImageResource(itemBean.recycle_image);
            share_png.setImageResource(itemBean.share_png);
            text_main.setText(itemBean.text_main);
            username.setText(itemBean.username);

        }
    }
}