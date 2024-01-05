package com.farmerfirst.growagric.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EvenSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    public EvenSpacingItemDecoration(Context context, int spanCount) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        spacing = (int) (8 * displayMetrics.density); // Adjust the spacing as needed
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % 2; // Assuming 2 columns

        if (column == 0) {
            outRect.right = spacing / 2;
            outRect.left = spacing;
        } else {
            outRect.right = spacing;
            outRect.left = spacing / 2;
        }

        if (position < 2) {
            outRect.top = spacing;
        }
        outRect.bottom = spacing;
    }
}