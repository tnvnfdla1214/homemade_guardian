package com.example.homemade_guardian_beta.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.fragment.ChatFragment;
import com.example.homemade_guardian_beta.chat.fragment.Guest_Chat_MarketInfoFragment;
import com.example.homemade_guardian_beta.chat.fragment.Host_Chat_MarketInfoFragment;
import com.example.homemade_guardian_beta.chat.fragment.Nonepost_chat_MarketInfoFragment;
import com.example.homemade_guardian_beta.model.chat.MessageModel;
import com.example.homemade_guardian_beta.Main.common.FirebaseHelper;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


//채팅방안 액티비티 -chatFragment랑 연결됨
public class ChatActivity extends AppCompatActivity implements ChatFragment.RoomUidSetListener{

    private DrawerLayout drawerLayout;
    private ChatFragment chatFragment;
    private Host_Chat_MarketInfoFragment hostChat_MarketInfoFragment;
    private Guest_Chat_MarketInfoFragment guestChat_MarketInfoFragment;
    private Nonepost_chat_MarketInfoFragment nonepost_chat_marketInfoFragment;
    private String currentUser_Uid =null;
    String To_User_Uid;
    String ChatRoomListModel_RoomUid;
    String ChatRoomListModel_Title;
    String MarketModel_Market_Uid;
    private FirebaseHelper Firebasehelper;          //FirebaseHelper 참조 선언
    private MessageModel MessageModel;                    //UserModel 참조 선언
    FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    public static Context mcontext;
    MarketModel marketModel;

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
        MarketModel_Market_Uid = getIntent().getStringExtra("MarketModel_Market_Uid"); //PostActivity에서 받아옴//ChatRoomListFragment에서 받아옴
        currentUser_Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  //현재 누구냐의 따라 포스트 정보 프레그먼트가 달라진다.


        if (ChatRoomListModel_Title!=null) { actionBar.setTitle(ChatRoomListModel_Title); }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // chatting area
        chatFragment = ChatFragment.getInstance(To_User_Uid, ChatRoomListModel_RoomUid, MarketModel_Market_Uid,currentUser_Uid);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, chatFragment )
                .commit();

        Firebasehelper = new FirebaseHelper(this);

        mcontext = this;

        //Chat_postInfoFragment area
        DocumentReference docRef_USERS_HostUid = FirebaseFirestore.getInstance().collection("MARKETS").document(MarketModel_Market_Uid);
        docRef_USERS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                marketModel = documentSnapshot.toObject(MarketModel.class);
                Log.d("이석규","marketModel.getMarketModel_Host_Uid() : " + marketModel);
                //if문 : 주어진 MarketModel_Market_Uid로ㅓ 찾아간  marketModel이 null이면 없는 게시물이라는 것을 알려주기 위한 Nonepost_chat_MarketInfoFragment를 띄운다.
                //else, else if문 : currentUser_Uid == PostModel_Host_Uid 일 경우 Host_Chat_PostInfoFragment를 띄우고 아니라면 guest_Chat_PostInfoFragment를 띄운다.
                if(marketModel == null){
                    nonepost_chat_marketInfoFragment = new Nonepost_chat_MarketInfoFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.postinfoFragment, nonepost_chat_marketInfoFragment).commit();
                } else if(currentUser_Uid.equals(marketModel.getMarketModel_Host_Uid())){

                    hostChat_MarketInfoFragment = new Host_Chat_MarketInfoFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.postinfoFragment, hostChat_MarketInfoFragment).commit();
                    Bundle Marketbundle = new Bundle();
                    Marketbundle.putString("MarketModel_Market_Uid", MarketModel_Market_Uid);
                    Marketbundle.putString("To_User_Uid",To_User_Uid);
                    Marketbundle.putString("currentUser_Uid",currentUser_Uid);
                    hostChat_MarketInfoFragment.setArguments(Marketbundle);

                }
                else{
                    guestChat_MarketInfoFragment = new Guest_Chat_MarketInfoFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.postinfoFragment, guestChat_MarketInfoFragment).commit();
                    Bundle Marketbundle = new Bundle();
                    Marketbundle.putString("MarketModel_Market_Uid", MarketModel_Market_Uid);
                    guestChat_MarketInfoFragment.setArguments(Marketbundle);
                }
            }
        });
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
                chatFragment.User_GoOut(currentUser_Uid,MarketModel_Market_Uid,ChatRoomListModel_RoomUid);
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

    public void ChatFragment_User_GoOut(String Roomuid){
        Log.d("라라라","RoomUid4 : " + Roomuid);
        chatFragment.User_GoOut(currentUser_Uid,MarketModel_Market_Uid,Roomuid);
    }
}