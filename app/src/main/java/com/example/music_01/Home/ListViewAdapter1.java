package com.example.music_01.Home;
//RV适配器，只不过这个是横向的
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_01.R;

import java.util.List;

public class ListViewAdapter1 extends RecyclerView.Adapter<ListViewAdapter1.InnerHolder> {
    private final List<ItemBean> mData;

    public ListViewAdapter1 (List<ItemBean> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_list_view1,null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.setHolder(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private TextView text3;
        private TextView text2;
        private ImageView picture3;
        private ImageView picture2;
        private TextView text1;
        private TextView text;
        private ImageView picture1;
        private ImageView picture;
        private ImageView icon;
        private TextView title;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
//            icon = (ImageView) itemView.findViewById(R.id.list_view_icon);
            picture = (ImageView) itemView.findViewById(R.id.list_view1_picture);
            picture1 = (ImageView) itemView.findViewById(R.id.list_view1_picture1);
            picture2 = (ImageView) itemView.findViewById(R.id.list_view1_picture2);
            picture3 = (ImageView) itemView.findViewById(R.id.list_view1_picture3);
//            title = (TextView)itemView.findViewById(R.id.list_view_title);
            text = (TextView)itemView.findViewById(R.id.list_view1_text);
            text1 = (TextView)itemView.findViewById(R.id.list_view1_text1);
            text2 = (TextView)itemView.findViewById(R.id.list_view1_text2);
            text3 = (TextView)itemView.findViewById(R.id.list_view1_text3);

        }

        public void setHolder(ItemBean itemBean) {
//            icon.setImageResource(itemBean.icon);
            picture.setImageResource(itemBean.picture);
            picture1.setImageResource(itemBean.picture1);
            picture2.setImageResource(itemBean.picture2);
            picture2.setImageResource(itemBean.picrure3);
//            title.setText(itemBean.title);
            text.setText(itemBean.text);
            text.setEllipsize(TextUtils.TruncateAt.END);
            text1.setText(itemBean.text1);
            text1.setEllipsize(TextUtils.TruncateAt.END);
            text2.setText(itemBean.text1);
            text2.setEllipsize(TextUtils.TruncateAt.END);
            text3.setText(itemBean.text1);
            text3.setEllipsize(TextUtils.TruncateAt.END);

        }
    }
}