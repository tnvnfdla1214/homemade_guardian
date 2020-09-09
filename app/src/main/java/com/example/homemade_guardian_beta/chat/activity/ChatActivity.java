package com.example.homemade_guardian_beta.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.fragment.ChatFragment;
import com.example.homemade_guardian_beta.chat.fragment.GroupUserFragment;
import com.example.homemade_guardian_beta.model.chat.MessageModel;
import com.example.homemade_guardian_beta.post.common.FirebaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.homemade_guardian_beta.post.PostUtil.showToast;


//채팅방안 액티비티 -chatFragment랑 연결됨
public class ChatActivity extends AppCompatActivity implements ChatFragment.RoomUidSetListener{
    private DrawerLayout drawerLayout;
    private ChatFragment chatFragment;
    private GroupUserFragment groupUserFragment = null;
    String To_User_Uid;
    String ChatRoomListModel_RoomUid;
    String ChatRoomListModel_Title;
    private FirebaseHelper Firebasehelper;          //FirebaseHelper 참조 선언
    private MessageModel MessageModel;                    //UserModel 참조 선언
    int Java_MessageModel_ImageCount;                         //string형을 int로 형변환

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        To_User_Uid = getIntent().getStringExtra("To_User_Uid");     //MyInfoFragment에서 받아옴 ,PostActivity에서 받아옴
        ChatRoomListModel_RoomUid = getIntent().getStringExtra("RoomUid");     //ChatRoomListFragment에서 받아옴
        ChatRoomListModel_Title = getIntent().getStringExtra("ChatRoomListModel_Title"); //ChatRoomListFragment에서 받아옴

        if (ChatRoomListModel_Title!=null) { actionBar.setTitle(ChatRoomListModel_Title); }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*
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

         */
        // chatting area
        chatFragment = ChatFragment.getInstance(To_User_Uid, ChatRoomListModel_RoomUid);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, chatFragment )
                .commit();

        Firebasehelper = new FirebaseHelper(this);

    }


    @Override
    public void onBackPressed() {
        chatFragment.backPressed();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                         // part19 : 게시물 안에서의 수정 삭제 (58')
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //메뉴 안에 있는 버튼들의 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Chat_Delete_Button:
                RoomUidSet(ChatRoomListModel_RoomUid);
                Firebasehelper.ROOMS_Storagedelete(ChatRoomListModel_RoomUid);
                Intent Intent_MainActivity = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(Intent_MainActivity);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void RoomUidSet(String RoomUid){
        ChatRoomListModel_RoomUid = RoomUid;
    }
}