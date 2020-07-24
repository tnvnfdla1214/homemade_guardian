package com.example.homemade_guardian_beta.listener;

import com.example.homemade_guardian_beta.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
