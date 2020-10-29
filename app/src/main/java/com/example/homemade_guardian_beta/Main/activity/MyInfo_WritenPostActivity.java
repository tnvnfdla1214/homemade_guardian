package com.example.homemade_guardian_beta.Main.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homemade_guardian_beta.Main.Fragment.My_Writen_Community_Fragment;
import com.example.homemade_guardian_beta.Main.Fragment.My_Writen_Market_Fragment;
import com.example.homemade_guardian_beta.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyInfo_WritenPostActivity extends AppCompatActivity {
    private My_Writen_Market_Fragment My_Writen_Market_Fragment;
    private My_Writen_Community_Fragment My_Writen_Community_Fragment;
    private String CurrentUid;
    private FirebaseUser CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_writenpost);

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentUid =CurrentUser.getUid();

        init();
    }

    private void init() {
        My_Writen_Market_Fragment = My_Writen_Market_Fragment.getInstance(CurrentUid);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, My_Writen_Market_Fragment).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.TopNavigationView);            // part22 : 바텀 네비게이션바  설정 (47'20")
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Market:
                        My_Writen_Market_Fragment = My_Writen_Market_Fragment.getInstance(CurrentUid);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, My_Writen_Market_Fragment).commit();
                        return true;
                    case R.id.Community:
                        My_Writen_Community_Fragment = My_Writen_Community_Fragment.getInstance(CurrentUid);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, My_Writen_Community_Fragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

}
