package com.example.homemade_guardian_beta.post.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.example.homemade_guardian_beta.post.PostModel;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.listener.OnPostListener;
import com.example.homemade_guardian_beta.post.view.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// 게시물을 클릭하여 들어온 게시물의 상세정보에 대한 액티비티이다.
// 게시물의 제목, 내용, 작성자, 작성자 이미지, 게시물에 추가한 이미지 등이 있고, 하단부에 채팅과 댓글을 달 수 있는 기능이 있다.
// Ex) 메인 프레그먼트에서 게시물을 클릭하였을 때 모두 이 액티비티가 발생한다.

public class PostActivity extends BasicActivity {                                                       // part19 : 메인에서 게시물 클릭해서 넘어온 페이지, ReadContentsVIew는 여기서 이루어지는 실행들 (44')
    private PostModel postModel;                    //PostModel 참조 선언
    private UserModel userModel;                    //UserModel 참조 선언
    private CommentModel commentModel;              //CommentModel 참조 선언
    private FirebaseHelper firebaseHelper;          //FirebaseHelper 참조 선언
    private FirestoreAdapter firestoreAdapter;      //FirestoreAdapter 참조 선언

    private String CurrentUid;                      //현재 사용자의 Uid
    private String Host_Name = null;                //(댓글,게시물)작성자의 이름 (현재사용자)
    private String Comment_Host_Image;              //댓글 작성자의 이미지
    private ArrayList<String> ImageList;            //게시물의 이미지 리스트

    private ViewPager viewPager;                    //이미지들을 보여주기 위한 ViewPager 선언
    private Button ChatButton;                      //채팅하기 버튼
    private Button WriteButton;                     //댓글 작성 버튼
    private TextView Post_Host_Name;                //게시물 작성자의 이름
    private ImageButton Post_Host_ImageButton;      //게시물 작성자의 이미지 버튼
    private EditText Comment;                       //댓글 내용
    private TextView Title;                         //게시물의 제목


    private FirebaseUser CurrentUser;               //현재 사용자를 받기 위한 FirebaseUser 선언

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
        ChatButton = (Button) findViewById(R.id.chattingroom);
        ChatButton.setOnClickListener(onClickListener);
        Post_Host_ImageButton = (ImageButton) findViewById(R.id.hostuserpageimage);
        Post_Host_ImageButton.setOnClickListener(onClickListener);
        postModel = (PostModel) getIntent().getSerializableExtra("postInfo");

        Comment = findViewById(R.id.comment_input);
        WriteButton = findViewById(R.id.writeBtn);
        findViewById(R.id.writeBtn).setOnClickListener(onClickListener);
        CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);
        TextView createdAtTextView = findViewById(R.id.createAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(postModel.getPostModel_DateOfManufacture()));

        DocumentReference docRefe_USERS_CurrentUid = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUid);
        docRefe_USERS_CurrentUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                Host_Name = userModel.getName();
                Comment_Host_Image = userModel.getphotoUrl();
            }
        });

        DocumentReference docRef_USERS_HostUid = FirebaseFirestore.getInstance().collection("USERS").document(postModel.getPostModel_Host_Uid());
        docRef_USERS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                if(userModel.getphotoUrl() != null){
                    Glide.with(PostActivity.this).load(userModel.getphotoUrl()).centerCrop().override(500).into(Post_Host_ImageButton);
                    Post_Host_Name = findViewById(R.id.user_name);
                    Post_Host_Name.setText(userModel.getName());
                }
                else{
                    Glide.with(getApplicationContext()).load(R.drawable.user).into(Post_Host_ImageButton);
                }
            }
        });

        //뷰페이져
        viewPager = findViewById(R.id.viewPager);
        ImageList = postModel.getPostModel_ImageList();
        viewPager.setAdapter(new ViewPagerAdapter(this, ImageList));

        //댓글 목록
        firestoreAdapter = new RecyclerViewAdapter(FirebaseFirestore.getInstance().collection("POSTS").document(postModel.getPostModel_Post_Uid()).collection("COMMENT"));
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
        recyclerView.setAdapter(firestoreAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                              // part19 : 수정하고 오면 수정된 정보 반영 (84')
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    postModel = (PostModel)data.getSerializableExtra("postinfo");
                    //contentsLayout.removeAllViews();
                    ImageList = postModel.getPostModel_ImageList();
                    viewPager.setAdapter(new ViewPagerAdapter(this, ImageList));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                         // part19 : 게시물 안에서의 수정 삭제 (58')
        if(CurrentUser.getUid().equals(postModel.getPostModel_Host_Uid())){
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
                firebaseHelper.Post_storageDelete(postModel);
                Intent intentpage = new Intent(PostActivity.this, MainActivity.class);
                startActivity(intentpage);
                return true;
            case R.id.modify:
                myStartActivity(ModifyPostActivity.class, postModel);
                Toast.makeText(getApplicationContext(), "수정 성공", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.report:
                Toast.makeText(getApplicationContext(), "신고 되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.chat:
                //버튼 눌러짐
                Intent Intent_ChatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                //상대방 uid 넘겨주기
                Intent_ChatActivity.putExtra("toUid", postModel.getPostModel_Host_Uid());
                startActivity(Intent_ChatActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hostuserpageimage:
                    Intent Intent_HostModelActivity = new Intent(getApplicationContext(), HostModelActivity.class);
                    Intent_HostModelActivity.putExtra("toUid", postModel.getPostModel_Host_Uid());
                    startActivity(Intent_HostModelActivity);
                    break;
                case R.id.chattingroom:
                    //버튼 눌러짐
                    Intent Intent_ChatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                    //상대방 uid 넘겨주기
                    Intent_ChatActivity.putExtra("toUid", postModel.getPostModel_Host_Uid());
                    startActivity(Intent_ChatActivity);
                    break;
                case R.id.writeBtn:
                    String Comment = PostActivity.this.Comment.getText().toString();
                    writecomment(Comment, Host_Name, Comment_Host_Image);
                    PostActivity.this.Comment.setText("");
                    break;
            }
        }
    };

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostModel postModel) {
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

    private void myStartActivity(Class c, PostModel postModel) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postModel);
        startActivityForResult(intent, 0);
    }

    // 댓글 작성 함수
    private void writecomment(final String Comment,final String Host_Name,final String Comment_Host_Image) {
        WriteButton.setEnabled(false);
        String Comment_Uid = null;
        Comment_Uid = FirebaseFirestore.getInstance().collection("POSTS").document(postModel.getPostModel_Post_Uid()).collection("COMMENT").document().getId();

        Date DateOfManufacture = new Date();
        commentModel = new CommentModel(CurrentUid, Comment,  DateOfManufacture, Host_Name, Comment_Uid, postModel.getPostModel_Post_Uid(),Comment_Host_Image);

        final DocumentReference docRef_POSTS_PostUid = FirebaseFirestore.getInstance().collection("POSTS").document(postModel.getPostModel_Post_Uid());
        final String CommentID = Comment_Uid;
        docRef_POSTS_PostUid.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                WriteBatch batch = FirebaseFirestore.getInstance().batch();
                batch.set(docRef_POSTS_PostUid.collection("COMMENT").document(CommentID), commentModel);
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //sendGCM();
                            WriteButton.setEnabled(true);
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

            viewHolder.Comment_Host_Name.setText(commentmodel.getCommentModel_Host_Name());
            viewHolder.Comment.setText(commentmodel.getCommentModel_Comment());

            if (commentmodel.getCommentModel_Host_Image()!=null) {
                Glide.with(PostActivity.this).load(commentmodel.getCommentModel_Host_Image()).centerCrop().override(500).into(viewHolder.Comment_Host_Image);
            } else{
                Glide.with(PostActivity.this).load(R.drawable.user).into(viewHolder.Comment_Host_Image);
            }

            viewHolder.Menu_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(PostActivity.this, view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {

                                case R.id.delete:
                                    firebaseHelper.Comment_storageDelete(commentmodel);
                                    return true;
                                case R.id.report:
                                    Toast.makeText(getApplicationContext(), "신고 되었습니다.", Toast.LENGTH_SHORT).show();
                                    return true;

                                case R.id.chat:
                                    //버튼 눌러짐
                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                    //상대방 uid 넘겨주기
                                    intent.putExtra("toUid", postModel.getPostModel_Host_Uid());
                                    startActivity(intent);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    MenuInflater inflater = popup.getMenuInflater();
                    if(CurrentUser.getUid().equals(commentmodel.getCommentModel_Host_Uid())){
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
        public ImageView Comment_Host_Image;
        public TextView Comment_Host_Name;
        public TextView Comment;
        public CardView Menu_Button;

        CustomViewHolder(View view) {
            super(view);
            Comment_Host_Image = view.findViewById(R.id.user_photo);
            Comment_Host_Name = view.findViewById(R.id.user_name);
            Comment = view.findViewById(R.id.user_comment);
            Menu_Button = view.findViewById(R.id.menu);
        }
    }
}
