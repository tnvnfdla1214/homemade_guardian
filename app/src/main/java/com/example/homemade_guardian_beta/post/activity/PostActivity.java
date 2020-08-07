package com.example.homemade_guardian_beta.post.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.chat.ChatActivity;
import com.example.homemade_guardian_beta.chat.model.UserModel;
import com.example.homemade_guardian_beta.post.Comment;
import com.example.homemade_guardian_beta.post.FirebaseHelper;
import com.example.homemade_guardian_beta.MainActivity;
import com.example.homemade_guardian_beta.post.PostInfo;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.adapter.CommentAdapter;
import com.example.homemade_guardian_beta.post.view.ReadContentsView;
import com.example.homemade_guardian_beta.post.listener.OnPostListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 게시물 안에 들어가서 안의 액티비티
public class PostActivity extends BasicActivity {                                                       // part19 : 메인에서 게시물 클릭해서 넘어온 페이지, ReadContentsVIew는 여기서 이루어지는 실행들 (44')
    private PostInfo postInfo;
    private FirebaseHelper firebaseHelper;
    private ReadContentsView readContentsView;
    private LinearLayout contentsLayout;
    private Button chattingroom; //채팅하기 버튼
    private ImageButton hostuserpageimage; //게시물 작성자 버튼
    private Activity activity;
    private FirebaseUser currentUser;
    private UserModel userModel;

    private Button writeBtn;
    private EditText comment_input;
    private String myUid;
    private FirebaseFirestore firestore=null;
    private ArrayList<Comment> commentList;
    private CommentAdapter commentAdapter;
    private boolean updating;
    private FirebaseFirestore firebaseFirestore;
    String writerName = null;
    private boolean topScrolled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        chattingroom = (Button) findViewById(R.id.chattingroom);
        hostuserpageimage = (ImageButton) findViewById(R.id.hostuserpageimage);
        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");
        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsView = findViewById(R.id.readContentsView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);

        comment_input = findViewById(R.id.comment_input);
        writeBtn = findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(writeBtnClickListener);
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(postInfo.getuid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                Log.d("태그","민규3");
                if(userModel.getphotoUrl() != null){
                    Log.d("태그","민규4");
                    Glide.with(PostActivity.this).load(userModel.getphotoUrl()).centerCrop().override(500).into(hostuserpageimage);
                    Log.d("태그","민규5");

                }
                else{
                    Glide.with(getApplicationContext()).load(R.drawable.user).into(hostuserpageimage);
                }

            }
        });

        uiUpdate();

        //게시물 작성자 버튼
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
        ///
        firebaseFirestore = FirebaseFirestore.getInstance();

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(PostActivity.this, commentList);
        //commentAdapter.setOnPostListener(onPostListener);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        Log.d("로그w","3");
        recyclerView.setHasFixedSize(true);
        Log.d("로그w","4");
        recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
        Log.d("로그w","5");
        recyclerView.setAdapter(commentAdapter);
        Log.d("로그w","6");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {                          // part21 : 스크롤로 새로고침 (29'10")
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {        //part21 : 스크롤 손을 뗏을때(31')
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();

                if(newState == 1 && firstVisibleItemPosition == 0){                                      // part21 : 위로 새로고침 (39'40")
                    topScrolled = true;
                }
                if(newState == 0 && topScrolled){
                    postsUpdate(true);
                    topScrolled = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){                          //part21 : 스크롤 되는 내내(31')
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                if(totalItemCount - 8 <= lastVisibleItemPosition && !updating){                         // part21 : 아래에서 3번쩨 일때 && 로딩중일 때는 이벤트 작용 안하게 (35'10")
                    postsUpdate(false);
                }

                if(0 < firstVisibleItemPosition){
                    topScrolled = false;
                }
            }
        });

        postsUpdate(false);

        //댓글 쓸 때에 누구인지 먼저 판별 (댓글 올리기 버튼에 이 이벤트를 넣으면 너무 느려서 시작할 때 부터 지정하고 시작)
        DocumentReference docRefe = firebaseFirestore.collection("users").document(myUid);
        docRefe.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("로그w","1111" + writerName);
                userModel = documentSnapshot.toObject(UserModel.class);
                if (userModel.getName() != null) {
                    Log.d("로그w","2222" + writerName);
                    writerName = userModel.getName();
                    Log.d("로그w","3333" + writerName);

                }
            }
        });

    }
    //버튼을 눌렀을 떄 writecomment 이벤트 실행
    Button.OnClickListener writeBtnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            String comment = comment_input.getText().toString();
            writecomment(comment);

            comment_input.setText("");
        }
    };
    // 댓글 작성 이벤트
    private void writecomment(final String comment) {
        writeBtn.setEnabled(false);

        String commentID = null;
        commentID = firebaseFirestore.collection("posts").document(postInfo.getId()).collection("comments").document().getId();

        Log.d("로그w","4444" + writerName);
        final Map<String,Object> comments = new HashMap<>();
        comments.put("uid", myUid);
        comments.put("comment", comment);
        comments.put("timestamp", FieldValue.serverTimestamp());
        comments.put("name", writerName);
        comments.put("commentID", commentID);
        comments.put("postID", postInfo.getId());

        Log.d("로그w","5555");

        final DocumentReference docRefe = firestore.collection("posts").document(postInfo.getId());
        final String CommentID = commentID;
        docRefe.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                WriteBatch batch = firestore.batch();
                batch.set(docRefe.collection("comments").document(CommentID), comments);
                Log.d("로그w","6666");
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("로그w","7777");
                            //sendGCM();
                            writeBtn.setEnabled(true);
                        }
                    }
                });
            }

        });
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                              // part19 : 수정하고 오면 수정된 정보 반영 (84')
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    postInfo = (PostInfo)data.getSerializableExtra("postinfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
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
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
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
        @Override
        public void oncommentDelete(Comment comment) {
            Log.e("로그 ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    private void uiUpdate(){                                                                             // part19 : 함수로 만들어서 관리(92')
        //setToolbarTitle(postInfo.getTitle());
        readContentsView.setPostInfo(postInfo);
    }

    private void myStartActivity(Class c, PostInfo postInfo) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent, 0);
    }
    //post내의 댓글 표현
    private void postsUpdate(final boolean clear) {
        updating = true;

        Date date = commentList.size() == 0 || clear ? new Date() : commentList.get(commentList.size() - 1).getTimestamp();  //part21 : 사이즈가 없으면 현재 날짜 아니면 최근 말짜의 getCreatedAt로 지정 (27'40")
        CollectionReference collectionReference = firebaseFirestore.collection("posts").document(postInfo.getId()).collection("comments");                // 파이어베이스의 posts에서
        Log.d("로그w","스크롤 333");
        collectionReference.orderBy("timestamp", Query.Direction.DESCENDING).whereLessThan("timestamp", date).limit(10).get()       // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){                      //part22 : clear를 boolean으로 써서 업데이트 도중에 게시물 클릭시 발생하는 오류 해결 (3'30")   // part15 : MainAdapter에서 setOnClickListener에서 시작 (35'30")
                                commentList.clear();                                                           // part16 : List 안의 데이터 초기화
                            }                                                                               // part16 : postsUpdate로 이동 (15'50")
                            Log.d("로그w","스크롤 555");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("로그w","스크롤 3333");
                                commentList.add(new Comment(                                                          //postList로 데이터를 넣는다.
                                        document.getData().get("comment").toString(),
                                        document.getData().get("uid").toString(),
                                        new Date(document.getDate("timestamp").getTime()),
                                        document.getData().get("name").toString(),
                                        document.getData().get("commentID").toString(),
                                        document.getData().get("postID").toString()

                                ));
                            }
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d("로그","실패?");
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }
}
