package com.example.music_01.Dashboard;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
//额，说实话，我忘了这个是干嘛的了.....
public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpaceHeight;

    public CustomItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}

