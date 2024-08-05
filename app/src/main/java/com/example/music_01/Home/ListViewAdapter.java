package com.example.music_01.Home;
//RV适配器，只不过这个是横向的
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.music_01.Home.Home;
import com.example.music_01.R;

//RecycleView的数据适配器

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.InnerHolder>
{
    private final List<ItemBean> mData;

    public ListViewAdapter (List<ItemBean> data )
    {
        this.mData = data;
    }

    @NonNull//创建条目View
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //传入Item的布局

        //拿到View,建立ViewHolder
        View view = View.inflate(parent.getContext(), R.layout.item_list_view,null);
        return new InnerHolder(view);
    }

    @Override//用于绑定Holder，一般用来设置数据
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
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
        private ImageView icon;
        private TextView title;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            //找到条目控件
            icon = (ImageView) itemView.findViewById(R.id.list_view_icon);

            title = (TextView)itemView.findViewById(R.id.list_view_title);

        }


        //这个方法用于设置数据
        public void setHolder(ItemBean itemBean)
        {
            //开始设置数据
            icon.setImageResource(itemBean.icon);
//            picture.setImageResource(itemBean.picture);
//            picture1.setImageResource(itemBean.picture1);
            title.setText(itemBean.title);
//            text.setText(itemBean.text);
//            text1.setText(itemBean.text1);

        }
    }
}

