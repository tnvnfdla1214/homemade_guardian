package com.example.homemade_guardian_beta.post.listener;

import com.example.homemade_guardian_beta.post.Comment;
import com.example.homemade_guardian_beta.post.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void oncommentDelete(Comment comment);
    void onModify();
}
