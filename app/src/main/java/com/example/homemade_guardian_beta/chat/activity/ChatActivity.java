package com.example.homemade_guardian_beta.chat.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.fragment.ChatFragment;
import com.example.homemade_guardian_beta.chat.fragment.GroupUserFragment;


//채팅방안 액티비티 -chatFragment랑 연결됨
public class ChatActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ChatFragment chatFragment;
    private GroupUserFragment groupUserFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String To_User_Uid = getIntent().getStringExtra("To_User_Uid");     //MyInfoFragment에서 받아옴 ,PostActivity에서 받아옴
        final String ChatRoomListModel_RoomUid = getIntent().getStringExtra("RoomUid");     //ChatRoomListFragment에서 받아옴
        String ChatRoomListModel_Title = getIntent().getStringExtra("ChatRoomListModel_Title"); //ChatRoomListFragment에서 받아옴

        if (ChatRoomListModel_Title!=null) { actionBar.setTitle(ChatRoomListModel_Title); }

        // left drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        findViewById(R.id.rightMenuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    if (groupUserFragment ==null) {
                        groupUserFragment = GroupUserFragment.getInstance(ChatRoomListModel_RoomUid, chatFragment.getUserModel_UserList());
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.drawerFragment, groupUserFragment)
                                .commit();
                    }
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
        // chatting area
        chatFragment = ChatFragment.getInstance(To_User_Uid, ChatRoomListModel_RoomUid);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, chatFragment )
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        chatFragment.backPressed();
        finish();
    }
}
