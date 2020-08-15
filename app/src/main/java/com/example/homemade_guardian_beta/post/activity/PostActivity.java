package com.example.homemade_guardian_beta.post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.homemade_guardian_beta.chat.ChatActivity;
import com.example.homemade_guardian_beta.chat.common.FirestoreAdapter;
import com.example.homemade_guardian_beta.chat.model.UserModel;
import com.example.homemade_guardian_beta.post.CommentModel;
import com.example.homemade_guardian_beta.post.PostInfo;
import com.example.homemade_guardian_beta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

// 게시물 안에 들어가서 안의 액티비티
public class PostActivity extends BasicActivity {                                                       // part19 : 메인에서 게시물 클릭해서 넘어온 페이지, ReadContentsVIew는 여기서 이루어지는 실행들 (44')
    private PostInfo postInfo;
    private Button chattingroom; //채팅하기 버튼
    private ImageButton hostuserpageimage; //게시물 작성자 버튼
    private UserModel userModel;

    private Button writeBtn;
    private EditText comment_input;
    private String myUid;
    String writerName = null;
    private FirestoreAdapter firestoreAdapter;
    final Map<String,Object> CommentModel = new HashMap<>();
    private String commentphotoUrl;



    @Override
    public void onStart() {
        super.onStart();
        if (firestoreAdapter != null) {
            firestoreAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firestoreAdapter != null) {
            firestoreAdapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        chattingroom = (Button) findViewById(R.id.chattingroom);
        hostuserpageimage = (ImageButton) findViewById(R.id.hostuserpageimage);
        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");

        comment_input = findViewById(R.id.comment_input);
        writeBtn = findViewById(R.id.writeBtn);
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRefe2 = FirebaseFirestore.getInstance().collection("users").document(myUid);
        docRefe2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                writerName = userModel.getName();
                commentphotoUrl = userModel.getphotoUrl();
            }
        });

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(postInfo.getuid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                if(userModel.getphotoUrl() != null){
                    Glide.with(PostActivity.this).load(userModel.getphotoUrl()).centerCrop().override(500).into(hostuserpageimage);
                }
                else{
                    Glide.with(getApplicationContext()).load(R.drawable.user).into(hostuserpageimage);
                }

            }
        });

        //게시물 작성자 사진 버튼
        hostuserpageimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Host_infoActivity.class);
                intent.putExtra("toUid", postInfo.getuid());
                startActivity(intent);
            }
        });

        //채팅버튼
        chattingroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼 눌러짐
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                //상대방 uid 넘겨주기
                intent.putExtra("toUid", postInfo.getuid());
                startActivity(intent);

            }
        });

        //댓글버튼
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = comment_input.getText().toString();
                writecomment(comment,writerName,commentphotoUrl);
                comment_input.setText("");

            }
        });

        //댓글 목록
        firestoreAdapter = new RecyclerViewAdapter(FirebaseFirestore.getInstance().collection("posts").document(postInfo.getId()).collection("comments"));
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
        //recyclerView.setLayoutManager( new LinearLayoutManager((getContext())));
        recyclerView.setAdapter(firestoreAdapter);

    }

    // 댓글 작성 함수
    private void writecomment(final String comment,final String writerName,final String commentphotoUrl) {
        writeBtn.setEnabled(false);

        String commentID = null;
        commentID = FirebaseFirestore.getInstance().collection("posts").document(postInfo.getId()).collection("comments").document().getId();

        CommentModel.put("uid", myUid);
        CommentModel.put("comment", comment);
        CommentModel.put("timestamp", FieldValue.serverTimestamp());
        CommentModel.put("name", writerName);
        CommentModel.put("commentID", commentID);
        CommentModel.put("postID", postInfo.getId());
        CommentModel.put("commentphotoUrl", commentphotoUrl);


        final DocumentReference docRefe = FirebaseFirestore.getInstance().collection("posts").document(postInfo.getId());
        final String CommentID = commentID;
        docRefe.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                WriteBatch batch = FirebaseFirestore.getInstance().batch();
                batch.set(docRefe.collection("comments").document(CommentID), CommentModel);
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //sendGCM();
                            writeBtn.setEnabled(true);
                        }
                    }
                });
            }

        });

    }

    class RecyclerViewAdapter extends FirestoreAdapter<CustomViewHolder> {
        final private RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        private StorageReference storageReference;

        RecyclerViewAdapter(Query query) {
            super(query);
            storageReference  = FirebaseStorage.getInstance().getReference();
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment, parent, false));
        }

        @Override
        public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
            DocumentSnapshot documentSnapshot = getSnapshot(position);
            CommentModel commentmodel = documentSnapshot.toObject(CommentModel.class);


            viewHolder.user_name.setText(commentmodel.getName());
            viewHolder.user_comment.setText(commentmodel.getComment());

            if (commentmodel.getcommentphotoUrl()!=null) {
                Glide.with(PostActivity.this).load(commentmodel.getcommentphotoUrl()).centerCrop().override(500).into(viewHolder.user_photo);
            } else{
                Glide.with(PostActivity.this).load(R.drawable.user).into(viewHolder.user_photo);
            }




        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView user_photo;
        public TextView user_name;
        public TextView user_comment;

        CustomViewHolder(View view) {
            super(view);
            user_photo = view.findViewById(R.id.user_photo);
            user_name = view.findViewById(R.id.user_name);
            user_comment = view.findViewById(R.id.user_comment);
        }
    }


}
