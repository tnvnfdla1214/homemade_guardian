package com.example.homemade_guardian_beta.post.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.homemade_guardian_beta.MainActivity;
import com.example.homemade_guardian_beta.chat.ChatActivity;
import com.example.homemade_guardian_beta.chat.common.FirestoreAdapter;
import com.example.homemade_guardian_beta.chat.model.UserModel;
import com.example.homemade_guardian_beta.post.CommentModel;
import com.example.homemade_guardian_beta.post.FirebaseHelper;
import com.example.homemade_guardian_beta.post.PostInfo;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.listener.OnPostListener;
import com.example.homemade_guardian_beta.post.view.ReadContentsView;
import com.example.homemade_guardian_beta.post.view.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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

    private ReadContentsView readContentsView;
    private LinearLayout contentsLayout;
    private FirebaseUser currentUser;
    private FirebaseHelper firebaseHelper;
    private TextView title;
    private TextView user_name;
    private String username;
    private ArrayList<String> Contents;

    //사진 viewpager
    private ArrayList<String> imageList = new ArrayList<String>();
    private static final int DP = 24;
    ViewPager viewPager;
    //constant
    final int PICTURE_REQUEST_CODE = 100;

    //test
    ImageView test;



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

        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsView = findViewById(R.id.readContentsView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);
        TextView createdAtTextView = findViewById(R.id.createAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(postInfo.getCreatedAt()));

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
                    username = userModel.getName();
                    user_name = findViewById(R.id.user_name);
                    user_name.setText(username);
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
        //게시물 보여주는 함수
        //uiUpdate();

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

        //뷰페이져
        viewPager = findViewById(R.id.viewPager);

        int i=0;
        Log.d("민규","민규2");
        Contents=postInfo.getContents();
        ArrayList<String> contentsList = postInfo.getContents();
        
        Log.d("민규","민규Contents2"+Contents);
        imageList.addAll(Contents);
        Log.d("민규","민규imageList2"+imageList);
        viewPager.setAdapter(new ViewPagerAdapter(this, imageList));

        //댓글 목록
        firestoreAdapter = new RecyclerViewAdapter(FirebaseFirestore.getInstance().collection("posts").document(postInfo.getId()).collection("comments"));
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
        //recyclerView.setLayoutManager( new LinearLayoutManager((getContext())));
        recyclerView.setAdapter(firestoreAdapter);


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                              // part19 : 수정하고 오면 수정된 정보 반영 (84')
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    postInfo = (PostInfo)data.getSerializableExtra("postinfo");
                    contentsLayout.removeAllViews();
                    //uiUpdate();
                    Log.d("민규","민규2");
                    Contents=postInfo.getContents();
                    imageList.addAll(Contents);
                    viewPager.setAdapter(new ViewPagerAdapter(this, imageList));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                         // part19 : 게시물 안에서의 수정 삭제 (58')
        if(currentUser.getUid().equals(postInfo.getuid())){
            getMenuInflater().inflate(R.menu.post_host, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.post_guest, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    //수정삭제 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                firebaseHelper.storageDelete(postInfo);
                Intent intentpage = new Intent(PostActivity.this, MainActivity.class);
                startActivity(intentpage);
                Toast.makeText(getApplicationContext(), "삭제 성공", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.modify:
                myStartActivity(ModifyPostActivity.class, postInfo);
                Toast.makeText(getApplicationContext(), "수정 성공", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.report:
                Toast.makeText(getApplicationContext(), "신고 되었습니다.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.chat:
                //버튼 눌러짐
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                //상대방 uid 넘겨주기
                intent.putExtra("toUid", postInfo.getuid());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            Log.e("로그 ","삭제 성공");
        }
        public void oncommentDelete(CommentModel commentModel) {
            Log.e("로그 ","댓글 삭제 성공");
        }
        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    /*
    private void uiUpdate(){                                                                             // part19 : 함수로 만들어서 관리(92')
        //setToolbarTitle(postInfo.getTitle());
        readContentsView.setPostPageInfo(postInfo);
        title = findViewById(R.id.title);
        title.setText(postInfo.getTitle());
    }
     */

    private void myStartActivity(Class c, PostInfo postInfo) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent, 0);
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

    //댓글용
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
            final CommentModel commentmodel = documentSnapshot.toObject(CommentModel.class);


            viewHolder.user_name.setText(commentmodel.getName());
            viewHolder.user_comment.setText(commentmodel.getComment());

            if (commentmodel.getcommentphotoUrl()!=null) {
                Glide.with(PostActivity.this).load(commentmodel.getcommentphotoUrl()).centerCrop().override(500).into(viewHolder.user_photo);
            } else{
                Glide.with(PostActivity.this).load(R.drawable.user).into(viewHolder.user_photo);
            }

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(PostActivity.this, view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {

                                case R.id.delete:
                                    Log.d("로그","삭제 11111");
                                    firebaseHelper.commentDelete(commentmodel);
                                    Log.d("로그","삭제 22222");
                                    return true;
                                case R.id.report:
                                    Toast.makeText(getApplicationContext(), "신고 되었습니다.", Toast.LENGTH_SHORT).show();
                                    return true;

                                case R.id.chat:
                                    //버튼 눌러짐
                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                    //상대방 uid 넘겨주기
                                    intent.putExtra("toUid", postInfo.getuid());
                                    startActivity(intent);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });


                    MenuInflater inflater = popup.getMenuInflater();
                    if(currentUser.getUid().equals(commentmodel.getUid())){
                        inflater.inflate(R.menu.comment_host, popup.getMenu());
                    }
                    else{
                        inflater.inflate(R.menu.post_guest, popup.getMenu());
                    }
                    popup.show();}
                }
            );



        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView user_photo;
        public TextView user_name;
        public TextView user_comment;
        public CardView cardView;

        CustomViewHolder(View view) {
            super(view);
            user_photo = view.findViewById(R.id.user_photo);
            user_name = view.findViewById(R.id.user_name);
            user_comment = view.findViewById(R.id.user_comment);
            cardView = view.findViewById(R.id.menu);
        }
    }



}
