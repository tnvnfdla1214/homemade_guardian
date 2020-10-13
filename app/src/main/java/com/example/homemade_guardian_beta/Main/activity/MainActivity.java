package com.example.homemade_guardian_beta.Main.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.homemade_guardian_beta.Main.bottombar.ChatroomListFragment;
import com.example.homemade_guardian_beta.Main.bottombar.HomeFragment;
import com.example.homemade_guardian_beta.Main.bottombar.MyInfoFragment;
import com.example.homemade_guardian_beta.Main.bottombar.WritePostFragment;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.activity.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private String LocalState = null;
    HomeFragment homeFragment;
    WritePostFragment writepostFragment;
    ChatroomListFragment chatroomFragment;
    MyInfoFragment myinfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();                                // part5 : 로그인 시        // part22 : 운래는 옮길때 homeFragment로 옮겨졌으나 매번 불러오는것이 비효율적이라 여기로 옮김
        if (firebaseUser == null) {
            myStartActivity(LoginActivity.class);                                                              // part5 : 로그인 정보 없으면 회원가입 화면으로
        } else {
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("USERS").document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {                                                            // part5 : 로그인 되있었어도 정보가 있으면 불러오고
                            } else {                                                                            // part5 : 아니면 입력받는다.   (18')
                                myStartActivity(MemberInitActivity.class);
                            }
                        }
                    } else {
                        //Log.d("태그", "get failed with ", task.getException());
                    }
                }
            });
        }
        //HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, homeFragment)
                .commit();
        LocalState = "home";
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);            // part22 : 바텀 네비게이션바  설정 (47'20")
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        if(LocalState.equals("writepost")){
                            if(writepostFragment.WritePostFragmentDataCheck()){
                                showMessage(homeFragment);
                            };
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        }
                        LocalState = "home";
                        return true;
                    case R.id.writepost:
                        if(LocalState.equals("writepost")){
                            return false;
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, writepostFragment).commit();
                        }
                        LocalState = "writepost";
                        return true;
                    case R.id.chatroomlist:
                        if(LocalState.equals("writepost")){
                            if(writepostFragment.WritePostFragmentDataCheck()){
                                showMessage(homeFragment);
                            };
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, chatroomFragment).commit();
                        }
                        LocalState = "chatroomlist";
                        return true;
                    case R.id.myinfo:
                        if(LocalState.equals("writepost")){
                            if(writepostFragment.WritePostFragmentDataCheck()){
                                showMessage(homeFragment);
                            };
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, myinfoFragment).commit();
                        }
                        LocalState = "myinfo";
                        return true;
                }
                return false;
            }
        });
        //여기에 들어가는지 확실x
        //sendRegistrationToServer();
    }

    private void myStartActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }
    public void showMessage(final Fragment fragment){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("다른 화면으로 넘어갈 시 내용이 저장 되지 않습니다.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Snackbar.make(textView,"예 버튼이 눌렸습니다.",Snackbar.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.writepost);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
