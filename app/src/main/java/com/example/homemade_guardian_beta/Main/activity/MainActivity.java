package com.example.homemade_guardian_beta.Main.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.homemade_guardian_beta.Main.bottombar.ChatroomListFragment;
import com.example.homemade_guardian_beta.Main.bottombar.CommunityFragment;
import com.example.homemade_guardian_beta.Main.bottombar.MarketFragment;
import com.example.homemade_guardian_beta.Main.bottombar.MyInfoFragment;
import com.example.homemade_guardian_beta.Main.bottombar.WriteMarketFragment;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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


    Button Market_Write_Button;
    Button Community_Write_Button;

    UserModel userModel = new UserModel();

    private RelativeLayout Writen_ButtonsBackground_Layout;          //글쓰기 바텀 버튼 누를시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        Intent intent = getIntent();
        UserModel userModel = (UserModel)intent.getSerializableExtra("userModel");
        Bundle bundle = new Bundle();
        bundle.putSerializable("userModel",userModel);
        myinfoFragment.setArguments(bundle);
        Bottomnavigate();
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
            case 0:
                Bottomnavigate();
                break;
        }
    }

    private void Bottomnavigate() {

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
                    myStartFinishActivity(WriteMarketActivity.class);
                    break;
                case R.id.Community_Write_Button:
                    myStartFinishActivity(WriteCommunityActivity.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }
    private void myStartFinishActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
        finish();
    }
}
