package com.example.homemade_guardian_beta.post.listener;

import com.example.homemade_guardian_beta.post.CommentModel;
import com.example.homemade_guardian_beta.post.PostModel;

public interface OnPostListener {
    void onDelete(PostModel postModel);
    void oncommentDelete(CommentModel commentModel);
    public void onModify();

}
