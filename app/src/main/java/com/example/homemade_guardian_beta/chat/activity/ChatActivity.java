package com.example.homemade_guardian_beta.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.fragment.ChatFragment;
import com.example.homemade_guardian_beta.chat.fragment.Chat_PostInfoFragment;
import com.example.homemade_guardian_beta.chat.fragment.GroupUserFragment;
import com.example.homemade_guardian_beta.model.chat.MessageModel;
import com.example.homemade_guardian_beta.Main.common.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


//채팅방안 액티비티 -chatFragment랑 연결됨
public class ChatActivity extends AppCompatActivity implements ChatFragment.RoomUidSetListener{

    private DrawerLayout drawerLayout;
    private ChatFragment chatFragment;
    private Chat_PostInfoFragment chat_postInfoFragment;
    String To_User_Uid;
    String ChatRoomListModel_RoomUid;
    String ChatRoomListModel_Title;
    String PostModel_Post_Uid;
    private FirebaseHelper Firebasehelper;          //FirebaseHelper 참조 선언
    private MessageModel MessageModel;                    //UserModel 참조 선언
    FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    public static Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        To_User_Uid = getIntent().getStringExtra("To_User_Uid");     //MyInfoFragment에서 받아옴 ,PostActivity 받아옴, ChatroomListFragment에서 받아옴, 포스트에선 아마 못받아올듯
        ChatRoomListModel_RoomUid = getIntent().getStringExtra("RoomUid");     //ChatRoomListFragment에서 받아옴
        ChatRoomListModel_Title = getIntent().getStringExtra("ChatRoomListModel_Title"); //ChatRoomListFragment에서 받아옴
        PostModel_Post_Uid = getIntent().getStringExtra("PostModel_Post_Uid"); //PostActivity에서 받아옴//ChatRoomListFragment에서 받아옴

        if (ChatRoomListModel_Title!=null) { actionBar.setTitle(ChatRoomListModel_Title); }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        // chatting area
        chatFragment = ChatFragment.getInstance(To_User_Uid, ChatRoomListModel_RoomUid,PostModel_Post_Uid);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, chatFragment )
                .commit();

        Firebasehelper = new FirebaseHelper(this);

        mcontext = this;

        //chat_postInfoFragment area
        chat_postInfoFragment = new Chat_PostInfoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.postinfoFragment, chat_postInfoFragment).commit();
        Bundle Postbundle = new Bundle();
        Postbundle.putString("PostModel_Post_Uid",PostModel_Post_Uid);
        chat_postInfoFragment.setArguments(Postbundle);

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
                RoomUidSet(ChatRoomListModel_RoomUid,To_User_Uid);
                chatFragment.User_GoOut();
                Firebasehelper.ROOMS_USERS_OUT_CHECK(ChatRoomListModel_RoomUid,firebaseCurrentUser.getUid(),To_User_Uid);

                Intent Intent_MainActivity = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(Intent_MainActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void RoomUidSet(String RoomUid,String ToUid){
        ChatRoomListModel_RoomUid = RoomUid;
        To_User_Uid = ToUid;
    }

    public void ChatFragment_User_GoOut(){
        chatFragment.User_GoOut();
    }
}