package com.example.homemade_guardian_beta.post.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.homemade_guardian_beta.chat.ChatActivity;
import com.example.homemade_guardian_beta.post.FirebaseHelper;
import com.example.homemade_guardian_beta.MainActivity;
import com.example.homemade_guardian_beta.post.PostInfo;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.view.ReadContentsView;
import com.example.homemade_guardian_beta.post.listener.OnPostListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

// 게시물 안에 들어가서 안의 액티비티
public class PostActivity extends BasicActivity {                                                       // part19 : 메인에서 게시물 클릭해서 넘어온 페이지, ReadContentsVIew는 여기서 이루어지는 실행들 (44')
    private PostInfo postInfo;
    private FirebaseHelper firebaseHelper;
    private ReadContentsView readContentsView;
    private LinearLayout contentsLayout;
    private Button chattingroom;
    private Activity activity;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        chattingroom = (Button) findViewById(R.id.chattingroom);
        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");
        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsView = findViewById(R.id.readContentsView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);
        uiUpdate();


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
    }

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
}
