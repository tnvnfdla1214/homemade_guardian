package com.example.homemade_guardian_beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homemade_guardian_beta.fragment.ChatroomListFragment;
import com.example.homemade_guardian_beta.fragment.HomeFragment;
import com.example.homemade_guardian_beta.fragment.MyInfoFragment;
import com.example.homemade_guardian_beta.fragment.WritePostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.auth.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

public class MainActivity extends AppCompatActivity {

    String strNickname;
    String strEmail;
    private FirebaseAuth mAuth ;
    private FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}
