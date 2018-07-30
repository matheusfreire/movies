package com.msf.movies.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListItemDecoration extends RecyclerView.ItemDecoration {
 
        private int spanCount;
        private int spacing;

        public ListItemDecoration(int spanCount, int spacing) {
            this.spanCount = spanCount;
            this.spacing = spacing;
        }
 
        @Override
        public void getItemOffsets(Rect rect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column
 
//            if (includeEdge) {
            rect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            rect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
 
            if (position < spanCount) { // top edge
                rect.top = spacing;
            }
            rect.bottom = spacing; // item bottom
//            } else {
//                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
//                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//                if (position >= spanCount) {
//                    outRect.top = spacing; // item top
//                }
//            }
        }
    }