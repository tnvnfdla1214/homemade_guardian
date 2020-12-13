package com.example.homemade_guardian_beta.Main.common;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    void onItemSwipe(int position);
    void onRightClick(int position, RecyclerView.ViewHolder viewHolder);
}
