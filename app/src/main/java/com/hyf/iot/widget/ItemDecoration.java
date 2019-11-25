package com.hyf.iot.widget;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 */
public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public ItemDecoration(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        int position = parent.getChildLayoutPosition(view);
//        if (spanSizeLookup.getSpanSize(position) < 4) {
        outRect.top = space;
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
//        }
    }

}
