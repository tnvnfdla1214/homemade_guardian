package com.example.homemade_guardian_beta.post;

import android.app.Activity;

import androidx.annotation.NonNull;
import com.example.homemade_guardian_beta.post.listener.OnPostListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import static com.example.homemade_guardian_beta.post.PostUtil.isStorageUrl;
import static com.example.homemade_guardian_beta.post.PostUtil.showToast;
import static com.example.homemade_guardian_beta.post.PostUtil.storageUrlToName;

public class FirebaseHelper {                                                                           // part19 : Firevasehelper로 이동 (61')
    private Activity activity;
    private OnPostListener onPostListener;
    private int successCount;

    public FirebaseHelper(Activity activity) {
        this.activity = activity;
    }

    public void setOnPostListener(OnPostListener onPostListener){ this.onPostListener = onPostListener; }

    public void Post_storageDelete(final PostModel postModel){                                                 // part16: 스토리지의 삭제 (13')
        FirebaseStorage storage = FirebaseStorage.getInstance();                                        // part17 : 스토리지 삭제 (문서) (19'50")
        StorageReference storageRef = storage.getReference();
        final String id = postModel.getPostModel_Post_Uid();
        ArrayList<String> contentsList = postModel.getPostModel_ImageList();
        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUrl(contents)) {
                successCount++;                                                                             // part17 : 사진의 개수가 여러개인 게시물의 경우 (23'35")
                StorageReference desertRef = storageRef.child("POSTS/" + id + "/" + storageUrlToName(contents));    // part17: (((파이어베이스에서 삭제))) 파이에베이스 스토리지는 폴더가 없다, 하나하나가 객체로서 저장 (13'30")
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        Post_storeDelete(id, postModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) { showToast(activity, "Error");
                    }
                });
            }
        }
        Post_storeDelete(id, postModel);
    }

    private void Post_storeDelete(final String id, final PostModel postModel) {                                     // part15 : (((DB에서 삭제))) 스토리지에서는 삭제 x
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if (successCount == 0) {
            firebaseFirestore.collection("POSTS").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(activity, "게시글을 삭제하였습니다.");
                            onPostListener.onDelete(postModel);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { showToast(activity, "게시글을 삭제하지 못하였습니다.");
                        }
                    });
        }
    }
    public void Comment_storageDelete(final CommentModel commentModel){                                                 // part16: 스토리지의 삭제 (13')
        final String id = commentModel.getCommentModel_Comment_Uid();
        Comment_storeDelete(id, commentModel);
    }
    private void Comment_storeDelete(final String id, final CommentModel commentModel) {                                     // part15 : (((DB에서 삭제))) 스토리지에서는 삭제 x
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("POSTS").document(commentModel.getCommentModel_Post_Uid()).collection("COMMENT").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(activity, "댓글을 삭제하였습니다.");
                            onPostListener.oncommentDelete(commentModel);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { showToast(activity, "댓글을 삭제하지 못하였습니다.");
                        }
                    });
    }
}
