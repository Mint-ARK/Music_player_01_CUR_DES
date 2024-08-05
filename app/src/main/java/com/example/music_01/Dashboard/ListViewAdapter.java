package com.example.music_01.Dashboard;
//RV的适配器，基本大同小异，这个的OBVH方法里加了一个跳到详情页的功能
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_01.Home.ItemBean;
import com.example.music_01.LoginActivity;
import com.example.music_01.MainActivity;
import com.example.music_01.Music_core_Acivity;
import com.example.music_01.R;
import com.example.music_01.StartActivity;
import com.example.music_01.transplant.transplant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListViewAdapter extends RecyclerView.Adapter<com.example.music_01.Dashboard.ListViewAdapter.InnerHolder>
{
    private final List<ItemBean> mData;
    private Dashboard dashboard;
    private Context context;

    public ListViewAdapter(Context context, List<ItemBean> data) {
        this.context = context;
        this.mData = data;
    }

    public ListViewAdapter(List<ItemBean> data) {
        this.mData = data;
    }

    @NonNull//创建条目View
    @Override
    public com.example.music_01.Dashboard.ListViewAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //传入Item的布局

        //拿到View,建立ViewHolder
        View view = View.inflate(parent.getContext(), R.layout.songs_item, null);
        return new com.example.music_01.Dashboard.ListViewAdapter.InnerHolder(view);
    }

    @Override//用于绑定Holder，一般用来设置数据
    public void onBindViewHolder(@NonNull com.example.music_01.Dashboard.ListViewAdapter.InnerHolder holder, int position) {
        //设置数据
        holder.setHolder(mData.get(position));
        //处理每个Item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "你点击了第" + position + "项", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), Music_core_Acivity.class);
                v.getContext().startActivity(intent);

                // 找到你的TextView和CircleImageView

            }
        });

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
        private final CircleImageView face;
        private final TextView instruct;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            //找到条目控件
            face = (CircleImageView) itemView.findViewById(R.id.face);

            instruct = (TextView) itemView.findViewById(R.id.instruct);

        }


        //这个方法用于设置数据
        public void setHolder(ItemBean itemBean) {
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

