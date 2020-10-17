package com.example.homemade_guardian_beta.Main.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.homemade_guardian_beta.Main.bottombar.ChatroomListFragment;
import com.example.homemade_guardian_beta.Main.bottombar.HomeFragment;
import com.example.homemade_guardian_beta.Main.bottombar.MyInfoFragment;
import com.example.homemade_guardian_beta.Main.bottombar.WritePostFragment;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.activity.ChatActivity;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String LocalState = null;
    HomeFragment homeFragment;
    WritePostFragment writepostFragment;
    ChatroomListFragment chatroomFragment;
    MyInfoFragment myinfoFragment;
    ArrayList<String> UnReViewUserList = new ArrayList<>();
    ArrayList<String> UnReViewPostList = new ArrayList<>();


    Button Post_Write_Button;
    Button Market_Write_Button;

    private RelativeLayout Writen_ButtonsBackground_Layout;          //글쓰기 바텀 버튼 누를시

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Writen_ButtonsBackground_Layout = findViewById(R.id.Writen_ButtonsBackground_Layout);
        Writen_ButtonsBackground_Layout.setOnClickListener(onClickListener);
        Post_Write_Button = findViewById(R.id.Post_Write_Button);
        Post_Write_Button.setOnClickListener(onClickListener);
        Market_Write_Button = findViewById(R.id.Market_Write_Button);
        Market_Write_Button.setOnClickListener(onClickListener);

        homeFragment = new HomeFragment();
        writepostFragment = new WritePostFragment();
        chatroomFragment = new ChatroomListFragment();
        myinfoFragment = new MyInfoFragment();

        init();
    }

    //안드로이드 생명주기중 하나
    @Override
    protected void onResume() {
        super.onResume();
    }

    //안드로이드 생명주기중 하나
    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                init();
                break;
            case 0:
                init();
                break;
        }
    }
    //ChatRoomListFragment가 한번에 ChatActivity의 함수를 불러올수 없으므로 만든 험슈
    public void ChatRoomListUserGoOutArtichecture(){
        ((ChatActivity)ChatActivity.mcontext).ChatFragment_User_GoOut();
    }

    private void init() {
        final FirebaseUser currentUser_Uid = FirebaseAuth.getInstance().getCurrentUser();                                // part5 : 로그인 시        // part22 : 운래는 옮길때 homeFragment로 옮겨졌으나 매번 불러오는것이 비효율적이라 여기로 옮김

        if (currentUser_Uid == null) {
            myStartActivity(LoginActivity.class);                                                              // part5 : 로그인 정보 없으면 회원가입 화면으로
        } else {
            final String User_Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("USERS").document(User_Uid);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                userModel = document.toObject(UserModel.class);
                                UnReViewUserList = userModel.getUserModel_UnReViewUserList();
                                UnReViewPostList = userModel.getUserModel_UnReViewPostList();
                                if(UnReViewUserList.size()>0){
                                    ReviewActivity reviewActivity = new ReviewActivity(MainActivity.this);
                                    reviewActivity.callFunction(UnReViewUserList.get(0),UnReViewPostList.get(0));
                                }
                            }else {
                                myStartActivity(MemberInitActivity.class);
                            }
                        }
                    }
                }
            });
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, homeFragment)
                .commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);            // part22 : 바텀 네비게이션바  설정 (47'20")
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.writepost:
                        Writen_ButtonsBackground_Layout.setVisibility(View.VISIBLE);
                       // getSupportFragmentManager().beginTransaction().replace(R.id.container, writepostFragment).commit();
                        return true;
                    case R.id.chatroomlist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, chatroomFragment).commit();
                        return true;
                    case R.id.myinfo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myinfoFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Writen_ButtonsBackground_Layout:
                    Writen_ButtonsBackground_Layout.setVisibility(View.GONE);
                    break;
                case R.id.Post_Write_Button:
                    //Intent intent = new Intent( MainActivity.this, WritePostActivity.class);
                    //startActivity(intent);
                    myStartActivity(WritePostActivity.class);
                    break;
                case R.id.Market_Write_Button:
                    Writen_ButtonsBackground_Layout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void myStartActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

}
