package com.example.homemade_guardian_beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homemade_guardian_beta.activity.MemberInitActivity;
import com.example.homemade_guardian_beta.fragment.ChatroomListFragment;
import com.example.homemade_guardian_beta.fragment.HomeFragment;
import com.example.homemade_guardian_beta.fragment.MyInfoFragment;
import com.example.homemade_guardian_beta.fragment.WritePostFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    String strNickname;
    String strEmail;
    private FirebaseAuth mAuth ;
    private FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        }
    }

    private void init() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();                                // part5 : 로그인 시        // part22 : 운래는 옮길때 homeFragment로 옮겨졌으나 매번 불러오는것이 비효율적이라 여기로 옮김
        if (firebaseUser == null) {
            myStartActivity(LoginActivity.class);                                                              // part5 : 로그인 정보 없으면 회원가입 화면으로
        } else {
            //myStartActivity(CameraActivity.class);                                                            // part5 : test

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("user").document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {                                                            // part5 : 로그인 되있었어도 정보가 있으면 불러오고
                                Log.d("로그", "DocumentSnapshot data: " + document.getData());
                            } else {                                                                            // part5 : 아니면 입력받는다.   (18')
                                Log.d("로그", "No such document");
                                //myStartActivity(MemberInitActivity.class);
                            }
                        }
                    } else {
                        Log.d("로그", "get failed with ", task.getException());
                    }
                }
            });
        }
        HomeFragment homeFragment = new HomeFragment();
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
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, homeFragment)
                                .commit();
                        return true;
                    case R.id.writepost:
                        WritePostFragment writepostFragment = new WritePostFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, writepostFragment)
                                .commit();
                        return true;
                    case R.id.chatroomlist:
                        ChatroomListFragment chatroomFragment = new ChatroomListFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, chatroomFragment)
                                .commit();
                        return true;
                    case R.id.myinfo:
                        MyInfoFragment myinfoFragment = new MyInfoFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, myinfoFragment)
                                .commit();
                        return true;
                }
                return false;
            }
        });
        //여기에 들어가는지 확실x
        //sendRegistrationToServer();
    }

    //Firebase 클라우드 메시징
//    void sendRegistrationToServer() {
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        String token = FirebaseInstanceId.getInstance().getToken();
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", token);
//        FirebaseFirestore.getInstance().collection("user").document(uid).set(map, SetOptions.merge());
//    }

    private void myStartActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

}
