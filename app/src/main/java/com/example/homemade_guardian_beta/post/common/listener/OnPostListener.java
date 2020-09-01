package com.example.homemade_guardian_beta.post.common.listener;

import com.example.homemade_guardian_beta.model.post.CommentModel;
import com.example.homemade_guardian_beta.model.post.PostModel;

//삭제를 할 때에 FirebaseHelper와 연결되어 사용되는 Interface이다.

public interface OnPostListener {
    void onDelete(PostModel Postmodel);
    void oncommentDelete(CommentModel Commentmodel);
    public void onModify();

}
