package com.app.legend.time.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.legend.time.R;

public class AlbumSpace extends RecyclerView.ItemDecoration {


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int space=TimeApp.getContext().getResources().getDimensionPixelSize(R.dimen.album_item_space);

        int position=parent.getChildAdapterPosition(view);

        if (position%3!=2){

            outRect.right=space;

        }

        else if (position%3==0){
            outRect.left=space;
        }

        outRect.bottom=space;

    }
}
