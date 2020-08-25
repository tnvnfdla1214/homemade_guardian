package com.example.homemade_guardian_beta.post.listener;

import com.example.homemade_guardian_beta.post.CommentModel;
import com.example.homemade_guardian_beta.post.PostModel;

//삭제를 할 때에 FirebaseHelper와 연결되어 사용되는 Interface이다.

public interface OnPostListener {
    void onDelete(PostModel postModel);
    void oncommentDelete(CommentModel commentModel);
    public void onModify();

}
