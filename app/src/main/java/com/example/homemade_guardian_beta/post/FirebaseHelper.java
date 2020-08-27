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

//파이어베이스에서 파이어스토어,파이어스토리지의 삭제에 관여한다.

public class FirebaseHelper {                                                                           // part19 : Firevasehelper로 이동 (61')
    private Activity activity;
    private OnPostListener Onpostlistener;
    private int SuccessCount;

    public FirebaseHelper(Activity activity) {
        this.activity = activity;
    }

    public void setOnpostlistener(OnPostListener Onpostlistener){ this.Onpostlistener = Onpostlistener; }

    //게시물의 경우에는 이미지가 파이어스토리지에 있기 때문에 파이어스토리지 또한 삭제해주어야한다.
    public void Post_Storagedelete(final PostModel postModel){                                                 // part16: 스토리지의 삭제 (13')
        FirebaseStorage Firebasestorage = FirebaseStorage.getInstance();                                        // part17 : 스토리지 삭제 (문서) (19'50")
        StorageReference Storagereference = Firebasestorage.getReference();
        final String Post_Uid = postModel.getPostModel_Post_Uid();
        ArrayList<String> Post_ImageList = postModel.getPostModel_ImageList();
        for (int i = 0; i < Post_ImageList.size(); i++) {
            String Image = Post_ImageList.get(i);
            if (isStorageUrl(Image)) {
                SuccessCount++;                                                                             // part17 : 사진의 개수가 여러개인 게시물의 경우 (23'35")
                StorageReference desertRef_POSTS_PostUid = Storagereference.child("POSTS/" + Post_Uid + "/" + storageUrlToName(Image));    // part17: (((파이어베이스에서 삭제))) 파이에베이스 스토리지는 폴더가 없다, 하나하나가 객체로서 저장 (13'30")
                desertRef_POSTS_PostUid.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SuccessCount--;
                        Post_Storedelete(Post_Uid, postModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) { showToast(activity, "Error");
                    }
                });
            }
        }
        Post_Storedelete(Post_Uid, postModel);
    }

    //파이어스토리지에서의 삭제가 끝난 후 파이어스토어에 있는 게시물의 데이터를 삭제한다.
    private void Post_Storedelete(final String Post_Uid, final PostModel Postmodel) {                                     // part15 : (((DB에서 삭제))) 스토리지에서는 삭제 x
        FirebaseFirestore Firebasefirestore = FirebaseFirestore.getInstance();
        if (SuccessCount == 0) {
            Firebasefirestore.collection("POSTS").document(Post_Uid)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(activity, "게시글을 삭제하였습니다.");
                            Onpostlistener.onDelete(Postmodel);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { showToast(activity, "게시글을 삭제하지 못하였습니다.");
                        }
                    });
        }
    }

    //댓글은 파이어스토리지를 이용하지 않으므로 파이어스토어 삭제만 진행한다.
    public void Comment_Storedelete(final CommentModel Commentmodel){                                                 // part16: 스토리지의 삭제 (13')
        final String Comment_Uid = Commentmodel.getCommentModel_Comment_Uid();
        FirebaseFirestore Firebasefirestore = FirebaseFirestore.getInstance();
        Firebasefirestore.collection("POSTS").document(Commentmodel.getCommentModel_Post_Uid()).collection("COMMENT").document(Comment_Uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(activity, "댓글을 삭제하였습니다.");
                        Onpostlistener.oncommentDelete(Commentmodel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { showToast(activity, "댓글을 삭제하지 못하였습니다.");
                    }
                });
    }
}