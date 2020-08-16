package com.example.homemade_guardian_beta.post.listener;

import com.example.homemade_guardian_beta.post.CommentModel;
import com.example.homemade_guardian_beta.post.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void oncommentDelete(CommentModel commentModel);
    public void onModify();

}
