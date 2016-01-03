package com.example.flickr.ui;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Khắc Vỹ on 12/31/2015.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager)parent.getLayoutManager();
        int numOfCol = layoutManager.getSpanCount();

        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;
        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildLayoutPosition(view) < numOfCol){
            outRect.top = 0;
        }
        if(parent.getChildLayoutPosition(view) % numOfCol == 0){
            outRect.left = 0;
        }
        if(parent.getChildLayoutPosition(view) % numOfCol == (numOfCol - 1)){
            outRect.right = 0;
        }
    }
}
