package com.example.homemade_guardian_beta.Main.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.homemade_guardian_beta.Main.bottombar.ChatroomListFragment;
import com.example.homemade_guardian_beta.Main.bottombar.CommunityFragment;
import com.example.homemade_guardian_beta.Main.bottombar.MarketFragment;
import com.example.homemade_guardian_beta.Main.bottombar.MyInfoFragment;
import com.example.homemade_guardian_beta.Main.bottombar.WriteMarketFragment;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {

    private String LocalState = null;
    MarketFragment marketFragment;
    WriteMarketFragment writeMarketFragment;
    ChatroomListFragment chatroomFragment;
    MyInfoFragment myinfoFragment;
    CommunityFragment communityFragment;
    FrameLayout container;
    public static Context mContext;


    Button Market_Write_Button;
    Button Community_Write_Button;

    UserModel userModel = new UserModel();

    private RelativeLayout Writen_ButtonsBackground_Layout;          //글쓰기 바텀 버튼 누를시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;


        Writen_ButtonsBackground_Layout = findViewById(R.id.Writen_ButtonsBackground_Layout);
        Writen_ButtonsBackground_Layout.setOnClickListener(onClickListener);
        Market_Write_Button = findViewById(R.id.Market_Write_Button);
        Market_Write_Button.setOnClickListener(onClickListener);
        Community_Write_Button = findViewById(R.id.Community_Write_Button);
        Community_Write_Button.setOnClickListener(onClickListener);

        marketFragment = new MarketFragment();
        writeMarketFragment = new WriteMarketFragment();
        chatroomFragment = new ChatroomListFragment();
        myinfoFragment = new MyInfoFragment();
        communityFragment = new CommunityFragment();

        container = findViewById(R.id.container);

        Bottomnavigate();


//
    }

    //안드로이드 생명주기중 하나
    @Override
    protected void onResume() { super.onResume(); }

    //안드로이드 생명주기중 하나
    @Override
    protected void onPause(){ super.onPause(); }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                Bottomnavigate();
                break;
            case 1:
                Writen_ButtonsBackground_Layout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, writeMarketFragment).commit();
                break;
        }
    }

    private void Bottomnavigate() {

        final FirebaseUser currentUser_Uid = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser_Uid == null) {
            myStartActivity(LoginActivity.class);
        } else {
            final String CurruntUser_Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("USERS").document(CurruntUser_Uid);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {  //데이터의 존재여부
                                UserModel userModel = document.toObject(UserModel.class);
                                if(userModel.getUserModel_UnReViewUserList().size()>0){
                                    ReviewActivity reviewActivity = new ReviewActivity(MainActivity.this);
                                    reviewActivity.callFunction(userModel.getUserModel_UnReViewUserList().get(0), userModel.getUserModel_UnReViewPostList().get(0),userModel);
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
                .replace(R.id.container, marketFragment)
                .commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);            // part22 : 바텀 네비게이션바  설정 (47'20")
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Market:
                        MarketFragment marketFragment = new MarketFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, marketFragment).commit();
                        Writen_ButtonsBackground_Layout.setVisibility(View.GONE);
                        return true;
                    case R.id.Community:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, communityFragment).commit();
                        Writen_ButtonsBackground_Layout.setVisibility(View.GONE);
                        return true;

                    case R.id.Writepost:
                        Writen_ButtonsBackground_Layout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, writeMarketFragment).commit();
                        return true;
                    case R.id.Chatroomlist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, chatroomFragment).commit();
                        Writen_ButtonsBackground_Layout.setVisibility(View.GONE);
                        return true;
                    case R.id.Myinfo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myinfoFragment).commit();
                        Writen_ButtonsBackground_Layout.setVisibility(View.GONE);
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
                case R.id.Market_Write_Button:
                    myStartActivity(WriteMarketActivity.class);
                    break;
                case R.id.Community_Write_Button:
                    myStartActivity(WriteCommunityActivity.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }
    public void myStartFinishActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
        finish();
    }
    public void Logout_dialog(){
        Logout_Dialog Logout_Dialog = new Logout_Dialog(this);
        Logout_Dialog.callFunction();
    }
}