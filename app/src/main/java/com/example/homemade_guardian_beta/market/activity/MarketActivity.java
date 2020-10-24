package com.example.homemade_guardian_beta.market.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.homemade_guardian_beta.Main.activity.BasicActivity;
import com.example.homemade_guardian_beta.Main.activity.HostModelActivity;
import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.chat.activity.ChatActivity;
import com.example.homemade_guardian_beta.chat.common.FirestoreAdapter;
import com.example.homemade_guardian_beta.model.community.CommunityModel;
import com.example.homemade_guardian_beta.model.community.Community_CommentModel;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.example.homemade_guardian_beta.model.market.Market_CommentModel;
import com.example.homemade_guardian_beta.Main.common.BackPressEditText;
import com.example.homemade_guardian_beta.Main.common.FirebaseHelper;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.Main.common.listener.OnPostListener;
import com.example.homemade_guardian_beta.Main.common.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator;

// 게시물을 클릭하여 들어온 게시물의 상세정보에 대한 액티비티이다.
// 게시물의 제목, 내용, 작성자, 작성자 이미지, 게시물에 추가한 이미지 등이 있고, 하단부에 채팅과 댓글을 달 수 있는 기능이 있다.
// Ex) 메인 프레그먼트에서 게시물을 클릭하였을 때 모두 이 액티비티가 발생한다.

public class MarketActivity extends BasicActivity {                                                       // part19 : 메인에서 게시물 클릭해서 넘어온 페이지, ReadContentsVIew는 여기서 이루어지는 실행들 (44')
    private MarketModel marketmodel;                    //PostModel 참조 선언
    private UserModel Usermodel;                    //UserModel 참조 선언
    private Market_CommentModel commentmodel;              //CommentModel 참조 선언
    private FirebaseHelper Firebasehelper;          //FirebaseHelper 참조 선언
    private FirestoreAdapter Comment_Firestoreadapter;      //FirestoreAdapter 참조 선언
    private FirestoreAdapter Like_Firestoreadapter;      //FirestoreAdapter 참조 선언

    private String CurrentUid;                      //현재 사용자의 Uid
    private String Host_Name = null;                //(댓글,게시물)작성자의 이름 (현재사용자)
    private String Comment_Host_Image;              //댓글 작성자의 이미지
    private ArrayList<String> ImageList = new ArrayList<>();            //게시물의 이미지 리스트

    private ViewPager Viewpager;                    //이미지들을 보여주기 위한 ViewPager 선언
    private Button Chat_With_MarketHost_Button;                     //채팅하기 버튼
    private Button Comment_Write_Button;            //댓글 작성 버튼
    private TextView Market_Host_Name_TextView;       //게시물 작성자의 이름
    private ImageView Host_UserPage_ImageButton;      //게시물 작성자의 이미지 버튼
    private ImageButton Like_ImageButton;      //게시물 작성자의 이미지 버튼
    private BackPressEditText Comment_Input_EditText;              //댓글 내용
    private TextView Title_TextView;                //게시물의 제목
    private TextView TextContents_TextView;         //게시물의 내용
    private LinearLayout Scrollview;                  //하단바 외의 영역
    private TextView Like_TextView;
    private ConstraintLayout ViewPagerLayout;

    private FirebaseUser CurrentUser;               //현재 사용자를 받기 위한 FirebaseUser 선언

    @Override
    public void onStart() {
        super.onStart();
        if (Comment_Firestoreadapter != null) {
            Comment_Firestoreadapter.startListening();
        }
        if (Like_Firestoreadapter != null) {
            Like_Firestoreadapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Comment_Firestoreadapter != null) {
            Comment_Firestoreadapter.stopListening();
        }
        if (Like_Firestoreadapter != null) {
            Like_Firestoreadapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        Chat_With_MarketHost_Button = (Button) findViewById(R.id.Chat_With_PostHost_Button);
        Chat_With_MarketHost_Button.setOnClickListener(onClickListener);
        Host_UserPage_ImageButton = (ImageView) findViewById(R.id.Host_UserPage_ImageButton);
        Host_UserPage_ImageButton.setOnClickListener(onClickListener);
        Like_ImageButton = (ImageButton) findViewById(R.id.Like_ImageButton);
        Like_ImageButton.setOnClickListener(onClickListener);
        Comment_Input_EditText = (BackPressEditText) findViewById(R.id.Comment_Input_EditText);
        Comment_Input_EditText.setOnBackPressListener(onBackPressListener);
        Comment_Input_EditText.setOnClickListener(onClickListener);

        Scrollview = (LinearLayout) findViewById(R.id.Scrollbar);
        Scrollview.setOnClickListener(onClickListener);
        Comment_Write_Button = findViewById(R.id.Comment_Write_Button);
        Title_TextView = findViewById(R.id.Post_Title);
        TextContents_TextView = findViewById(R.id.Post_TextContents);
        Like_TextView = findViewById(R.id.Like_TextView);
        TextView Market_DateOfManufacture = findViewById(R.id.Post_DateOfManufacture);
        findViewById(R.id.Comment_Write_Button).setOnClickListener(onClickListener);

        findViewById(R.id.menu).setOnClickListener(onClickListener);


        marketmodel = (MarketModel) getIntent().getSerializableExtra("marketInfo");
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentUid =CurrentUser.getUid();

        Firebasehelper = new FirebaseHelper(this);
        Firebasehelper.setOnpostlistener(onPostListener);

        Title_TextView.setText(marketmodel.getMarketModel_Title());
        TextContents_TextView.setText(marketmodel.getMarketModel_Text());
        Market_DateOfManufacture.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(marketmodel.getMarketModel_DateOfManufacture()));

        DocRefe_USERS_CurrentUid(); //댓글을 쓰는 사람의 정보를 받는 함수
        DocRef_USERS_HostUid();     //게시물의 작성자의 정보를 받는 함수

        //뷰페이져
        ImageList = marketmodel.getMarketModel_ImageList();
        if(ImageList != null) {
            String ViewpagerState = "Enable";
            Viewpager = findViewById(R.id.ViewPager);
            Viewpager.setAdapter(new ViewPagerAdapter(this, ImageList, ViewpagerState));

            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(Viewpager);
//            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
//            tabLayout.setupWithViewPager(Viewpager, true);
            /////////////////Viewpager.setSaveFromParentEnabled(false);
        }else{
            Log.d("로그","이미지 없");
            ViewPagerLayout = (ConstraintLayout) findViewById(R.id.ViewPagerLayout);
            Log.d("로그","이미지 없");
            ViewPagerLayout.setVisibility(View.GONE);
            Log.d("로그","이미지 없");
        }

        //작성자일 때 채팅버튼 비활성화
        if(CurrentUid.equals(marketmodel.getMarketModel_Host_Uid())){
            Chat_With_MarketHost_Button.setVisibility(View.GONE);
        }

        // 좋아요 버튼의 활성화 상태 결정
        Like_TextView.setText(String.valueOf(marketmodel.getMarketModel_LikeList().size()));
        int Check_Like = 0;
        for(int count = 0; count < marketmodel.getMarketModel_LikeList().size() ; count ++){
            if(CurrentUid.equals(marketmodel.getMarketModel_LikeList().get(count))){
                Glide.with(getApplicationContext()).load(R.drawable.heart).into(Like_ImageButton);
                Check_Like = 1;
            }
        }
        if(Check_Like == 0) {
            Glide.with(getApplicationContext()).load(R.drawable.empty_heart).into(Like_ImageButton);
        }



        //댓글 목록
        Comment_Firestoreadapter = new RecyclerViewAdapter(FirebaseFirestore.getInstance().collection("MARKETS").document(marketmodel.getMarketModel_Market_Uid()).collection("COMMENT").orderBy("market_CommentModel_DateOfManufacture"));
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MarketActivity.this));
        recyclerView.setAdapter(Comment_Firestoreadapter);
        ////////
//        Like_Firestoreadapter = new Like_RecyclerViewAdapter(FirebaseFirestore.getInstance().collection("POSTS"));
//        final RecyclerView Like_recyclerView = findViewById(R.id.Like_recyclerView);
//        Like_recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
//        Like_recyclerView.setAdapter(Like_Firestoreadapter);
///////////////////////////////////////////Postmodel.setPostModel_LikeList(LikeList);
    }

    @Override
    public void onResume() { super.onResume(); }

    private void didBackPressOnEditText() { finish(); }
    private BackPressEditText.OnBackPressListener onBackPressListener = new BackPressEditText.OnBackPressListener()
    {
        @Override
        public void onBackPress() {
            if(!CurrentUid.equals(marketmodel.getMarketModel_Host_Uid())){
                Chat_With_MarketHost_Button.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                              // part19 : 수정하고 오면 수정된 정보 반영 (84')
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    marketmodel = (MarketModel)data.getSerializableExtra("marketInfo");
                    Title_TextView.setText(marketmodel.getMarketModel_Title());
                    TextContents_TextView.setText(marketmodel.getMarketModel_Text());
                    ImageList = marketmodel.getMarketModel_ImageList();
                    if(ImageList != null) {
                        String ViewpagerState = "Enable";
                        Viewpager = findViewById(R.id.ViewPager);
                        Viewpager.setAdapter(new ViewPagerAdapter(this, ImageList, ViewpagerState));
                        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                        indicator.setViewPager(Viewpager);
                    }else{
                        Log.d("로그","이미지 없");
                        ViewPagerLayout = (ConstraintLayout) findViewById(R.id.ViewPagerLayout);
                        Log.d("로그","이미지 없");
                        ViewPagerLayout.setVisibility(View.GONE);
                    }
                    onResume();
                }
                break;
        }
    }

    // 댓글을 쓰는 사람 (현재 이용자) 의 정보를 미리 받는다.
    public void DocRefe_USERS_CurrentUid(){
        DocumentReference docRefe_USERS_CurrentUid = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUid);
        docRefe_USERS_CurrentUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usermodel = documentSnapshot.toObject(UserModel.class);
                Host_Name = Usermodel.getUserModel_NickName();
                Comment_Host_Image = Usermodel.getUserModel_ProfileImage();
            }
        });
    }

    // 현재 게시물의 작성자 Uid를 받아와 작성자의 정보를 구한다.
    public void DocRef_USERS_HostUid(){
        DocumentReference docRef_USERS_HostUid = FirebaseFirestore.getInstance().collection("USERS").document(marketmodel.getMarketModel_Host_Uid());
        docRef_USERS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usermodel = documentSnapshot.toObject(UserModel.class);
                if(Usermodel.getUserModel_ProfileImage() != null){
                    Glide.with(MarketActivity.this).load(Usermodel.getUserModel_ProfileImage()).centerInside().override(500).into(Host_UserPage_ImageButton);
                    Market_Host_Name_TextView = findViewById(R.id.Post_Host_Name);
                    Market_Host_Name_TextView.setText(Usermodel.getUserModel_NickName());
                }
                else{
                    Glide.with(getApplicationContext()).load(R.drawable.none_profile_user).into(Host_UserPage_ImageButton);
                    Market_Host_Name_TextView = findViewById(R.id.Post_Host_Name);
                    Market_Host_Name_TextView.setText(Usermodel.getUserModel_NickName());
                }
            }
        });
    }

    private void showPopup(View v) {                                                // part15 : 오른쪽 상단의 점3개 (수정 삭제) (23'25")
        PopupMenu popup = new PopupMenu(MarketActivity.this, v);
        if(CurrentUser.getUid().equals(marketmodel.getMarketModel_Host_Uid())){
            getMenuInflater().inflate(R.menu.post_host, popup.getMenu());
        }
        else{
            getMenuInflater().inflate(R.menu.post_guest, popup.getMenu());
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Post_Delete_Button:
                        Firebasehelper.Market_Storagedelete(marketmodel);
                        Intent Intent_MainActivity = new Intent(MarketActivity.this, MainActivity.class);
                        startActivity(Intent_MainActivity);
                        return true;
                    case R.id.Post_Modify_Button:
                        myStartActivity(ModifyMarketActivity.class, marketmodel);
                        return true;

                    case R.id.Post_Report_Button:
                        Toast.makeText(getApplicationContext(), "신고 되었습니다.", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.Chat_With_PostHost_Button:
                        //버튼 눌러짐
                        Intent Intent_ChatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                        //상대방 uid, 현재 포스트 uid 정보를 chatActivity로 넘겨준다.
                        Intent_ChatActivity.putExtra("To_User_Uid", marketmodel.getMarketModel_Host_Uid());
                        Intent_ChatActivity.putExtra("MarketModel_Market_Uid", marketmodel.getMarketModel_Market_Uid());
                        startActivity(Intent_ChatActivity);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }


    //Activity에서 사용하는 버튼들의 OnClickListener
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Host_UserPage_ImageButton:
                    Intent Intent_HostModelActivity = new Intent(getApplicationContext(), HostModelActivity.class);
                    Intent_HostModelActivity.putExtra("toUid", marketmodel.getMarketModel_Host_Uid());
                    startActivity(Intent_HostModelActivity);
                    break;
                case R.id.Chat_With_PostHost_Button:
                    //버튼 눌러짐
                    Intent Intent_ChatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                    //상대방 uid, 현재 포스트 uid 정보를 chatActivity로 넘겨준다.
                    Intent_ChatActivity.putExtra("To_User_Uid", marketmodel.getMarketModel_Host_Uid());
                    Intent_ChatActivity.putExtra("MarketModel_Market_Uid", marketmodel.getMarketModel_Market_Uid());
                    startActivity(Intent_ChatActivity);
                    break;
                case R.id.Comment_Write_Button:
                    String Comment = MarketActivity.this.Comment_Input_EditText.getText().toString();
                    Write_Comment(Comment, Host_Name, Comment_Host_Image);
                    MarketActivity.this.Comment_Input_EditText.setText("");
                    //비행기 보양 눌렀을 때 사라졌던 채팅버튼
                    InputMethodManager immhide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    if (!CurrentUid.equals(marketmodel.getMarketModel_Host_Uid())) {
                        Chat_With_MarketHost_Button.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.Comment_Input_EditText:
                    Chat_With_MarketHost_Button.setVisibility(View.GONE);
                    Comment_Input_EditText.setFocusableInTouchMode(true);
                    Comment_Input_EditText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    break;
                case R.id.Scrollbar:
                    InputMethodManager immHide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    immHide.hideSoftInputFromWindow(Comment_Input_EditText.getWindowToken(), 0);
                    if (!CurrentUid.equals(marketmodel.getMarketModel_Host_Uid())) {
                        Chat_With_MarketHost_Button.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.Like_ImageButton:
                    int Check_Like = 0;
                    for (int count = 0; count < marketmodel.getMarketModel_LikeList().size(); count++) {
                        if (CurrentUid.equals(marketmodel.getMarketModel_LikeList().get(count))) {
                            Toast.makeText(getApplicationContext(), "이미 좋아요를 누르셨습니다.", Toast.LENGTH_SHORT).show();
                            Check_Like++;
                        }
                    }
                    if (Check_Like == 0) {
                        Glide.with(getApplicationContext()).load(R.drawable.heart).into(Like_ImageButton);
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        ArrayList<String> LikeList = new ArrayList<>();
                        final DocumentReference documentReference = firebaseFirestore.collection("MARKETS").document(marketmodel.getMarketModel_Market_Uid());
                        LikeList = marketmodel.getMarketModel_LikeList();
                        LikeList.add(CurrentUid);
                        if (LikeList.size() > 0) {
                            marketmodel.setMarketModel_HotMarket("O");
                        }
                        marketmodel.setMarketModel_LikeList(LikeList);

                        documentReference.set(marketmodel.getMarketInfo())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                        Like_TextView.setText(String.valueOf(marketmodel.getMarketModel_LikeList().size()));
                    }
                    break;
                case R.id.menu:
                    showPopup(v);
                    break;
            }
        }
    };

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(MarketModel marketModel) {
            Log.e("로그 ","삭제 성공");
        }
        public void oncommentDelete(Market_CommentModel market_commentModel) { Log.e("로그 ","댓글 삭제 성공"); }
        @Override
        public void oncommunityDelete(CommunityModel communityModel) { }
        @Override
        public void oncommnitycommentDelete(Community_CommentModel community_commentModel) { }
        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    private void myStartActivity(Class c, MarketModel marketModel) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent Intent_Market_Data = new Intent(this, c);
        Intent_Market_Data.putExtra("marketInfo", marketModel);
        startActivityForResult(Intent_Market_Data, 0);
    }

       // 댓글을 작성하는 함수
    private void Write_Comment(final String Comment, final String Host_Name, final String Comment_Host_Image) {
        Comment_Write_Button.setEnabled(false);
        String Comment_Uid = null;
        Comment_Uid = FirebaseFirestore.getInstance().collection("MARKETS").document(marketmodel.getMarketModel_Market_Uid()).collection("COMMENT").document().getId();

        Date DateOfManufacture = new Date();
        commentmodel = new Market_CommentModel(CurrentUid, Comment,  DateOfManufacture, Host_Name, Comment_Uid, marketmodel.getMarketModel_Market_Uid(),Comment_Host_Image);

        final DocumentReference docRef_MARKETS_MarketUid = FirebaseFirestore.getInstance().collection("MARKETS").document(marketmodel.getMarketModel_Market_Uid());
        final String CommentID = Comment_Uid;
        docRef_MARKETS_MarketUid.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                WriteBatch Batch_COMMENT_CommentUid = FirebaseFirestore.getInstance().batch();
                Batch_COMMENT_CommentUid.set(docRef_MARKETS_MarketUid.collection("COMMENT").document(CommentID), commentmodel);
                Batch_COMMENT_CommentUid.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //sendGCM();
                            Comment_Write_Button.setEnabled(true);
                        }
                    }
                });
            }

        });
    }

    //댓글을 화면에 생성해주는 RecyclerView
    class RecyclerViewAdapter extends FirestoreAdapter<CustomViewHolder> {
        final private RequestOptions Requestoptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        private StorageReference Storagereference;

        RecyclerViewAdapter(Query query) {
            super(query);
            Storagereference = FirebaseStorage.getInstance().getReference();
            //setUnread2Read();
        }


        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment, parent, false));
        }

        @Override
        public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
            DocumentSnapshot DocumentSnapshot = getSnapshot(position);
            final Market_CommentModel market_commentModel = DocumentSnapshot.toObject(Market_CommentModel.class);
            Log.d("로그","Commentmodel : "+ market_commentModel);
            viewHolder.Comment_UserName_TextView.setText(market_commentModel.getMarket_CommentModel_Host_Name());
            viewHolder.Comment_UserComment_TextView.setText(market_commentModel.getMarket_CommentModel_Comment());

            if (market_commentModel.getMarket_CommentModel_Host_Image()!=null) {
                Glide.with(MarketActivity.this).load(market_commentModel.getMarket_CommentModel_Host_Image()).centerInside().override(500).into(viewHolder.Comment_UserProfile_ImageView);
            } else{
                Glide.with(MarketActivity.this).load(R.drawable.none_profile_user).centerCrop().override(500).into(viewHolder.Comment_UserProfile_ImageView);
            }

            viewHolder.Comment_Menu_CardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu Menu_Popup = new PopupMenu(MarketActivity.this, view);
                    Menu_Popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {

                                case R.id.Comment_Delete_Button:
                                    Firebasehelper.Market_Comment_Storedelete(market_commentModel);
                                    return true;
                                case R.id.Comment_Report_Button:
                                    Toast.makeText(getApplicationContext(), "신고 되었습니다.", Toast.LENGTH_SHORT).show();
                                    return true;

                                case R.id.Chat_With_CommentHost_Button:
                                    //버튼 눌러짐
                                    Intent Intent_ChatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                                    //상대방 uid, 현재 포스트 uid 정보를 chatActivity로 넘겨준다.
                                    Intent_ChatActivity.putExtra("To_User_Uid", marketmodel.getMarketModel_Host_Uid());
                                    Intent_ChatActivity.putExtra("MarketModel_Market_Uid", marketmodel.getMarketModel_Market_Uid());
                                    startActivity(Intent_ChatActivity);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    MenuInflater Menu_Inflater = Menu_Popup.getMenuInflater();
                    if(CurrentUser.getUid().equals(market_commentModel.getMarket_CommentModel_Host_Uid())){
                        Menu_Inflater.inflate(R.menu.comment_host, Menu_Popup.getMenu());
                    }
                    else{
                        Menu_Inflater.inflate(R.menu.comment_guest, Menu_Popup.getMenu());
                    }
                    Menu_Popup.show();}
                }
            );

        }
    }

    // 댓글의 Data
    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView Comment_UserProfile_ImageView;
        public TextView Comment_UserName_TextView;
        public TextView Comment_UserComment_TextView;
        public CardView Comment_Menu_CardView;

        CustomViewHolder(View view) {
            super(view);
            Comment_UserProfile_ImageView = view.findViewById(R.id.Comment_UserProfile);
            Comment_UserName_TextView = view.findViewById(R.id.Comment_UserName);
            Comment_UserComment_TextView = view.findViewById(R.id.Comment_UserComment);
            Comment_Menu_CardView = view.findViewById(R.id.Comment_Menu);

        }
    }






    //좋아요를 화면에 생성해주는 RecyclerView
    class Like_RecyclerViewAdapter extends FirestoreAdapter<LikeViewHolder> {
        Like_RecyclerViewAdapter(Query query) {
            super(query);
        }
        @Override
        public LikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LikeViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_like, parent, false));
        }
        @Override
        public void onBindViewHolder(LikeViewHolder viewHolder, int position) {
            DocumentSnapshot DocumentSnapshot = getSnapshot(position);
            final MarketModel marketModel = DocumentSnapshot.toObject(MarketModel.class);

            String LikeCount = String.valueOf(marketModel.getMarketModel_LikeList().size());
            viewHolder.Like_Count_TextView.setText(LikeCount);
            Log.d("로그","LikeCount :" + LikeCount);
            }
    }
    // 댓글의 Data
    private class LikeViewHolder extends RecyclerView.ViewHolder {
        public TextView Like_Count_TextView;
        LikeViewHolder(View view) {
            super(view);
            Like_Count_TextView = view.findViewById(R.id.Like_Count_TextView);
        }
    }

}
