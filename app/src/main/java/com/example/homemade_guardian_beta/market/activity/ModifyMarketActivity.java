package com.example.homemade_guardian_beta.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.Main.activity.BasicActivity;
import com.example.homemade_guardian_beta.photo.PhotoUtil;
import com.example.homemade_guardian_beta.photo.activity.PhotoPickerActivity;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//게시물의 수정을 위한 액티비티 이다. 형태는 WritePostFragment와 비슷하지만, 수정은 Activity라는 차이가 있다.
//      Ex) 게시물에서 수정을 눌렀을 때 실행되는 액티비티이다.
// +++수정을 추가해야함, 추가한 사진이 보이게 해주는 기능 추가해야함

public class ModifyMarketActivity extends BasicActivity {
    private MarketModel Marketmodel;                                        //UserModel 참조 선언

    private int PathCount;                                              //이미지 리스트 중 몇번째인지 나타내는 변수
    public  ArrayList<String> ArrayList_SelectedPhoto = new ArrayList<>();       //선택한 이미지들이 담기는 리스트

    private RelativeLayout ButtonsBackgroundLayout;                     ////결과적으로는 안쓸 버튼
    private RelativeLayout LoaderLayout;                                //로딩중을 나타내는 layout 선언
    private EditText Selected_EditText;                                  ////뭔지 모르겠음
    private EditText Title_EditText;                                     //수정하려는 게시물의 제목
    private ImageView FoodMarketbtn;
    private ImageView LifeMarketbtn;
    private ImageView BorrowMarketbtn;
    private ImageView WorkMarketbtn;
    private String Category;
    private ImageView PhotoList0, PhotoList1, PhotoList2, PhotoList3, PhotoList4;
    private TextView camera_Select_Text;

    private FirebaseUser CurrentUser;                                   //파이어베이스 데이터 상의 현재 사용자
    private StorageReference Storagereference;                                //파이어스토리지에 접근하기 위한 선언

    public final static int REQUEST_CODE = 1;                           //REQUEST_CODE 초기화


    private ArrayList<String> ImageList;            //게시물의 이미지 리스트
    private TextView FoodText, LifeText, BorrowText, WorkText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_market);
        setToolbarTitle("");


        LoaderLayout = findViewById(R.id.Loader_Lyaout);
        Title_EditText = findViewById(R.id.Post_Title_EditText);
        Selected_EditText = findViewById(R.id.Market_Content_EditText);

        PhotoList0 = (ImageView)findViewById(R.id.PhotoList0);
        PhotoList1 = (ImageView)findViewById(R.id.PhotoList1);
        PhotoList2 = (ImageView)findViewById(R.id.PhotoList2);
        PhotoList3 = (ImageView)findViewById(R.id.PhotoList3);
        PhotoList4 = (ImageView)findViewById(R.id.PhotoList4);

        findViewById(R.id.back_Button).setOnClickListener(onClickListener);
        findViewById(R.id.camera_Button_Layout).setOnClickListener(onClickListener);
        findViewById(R.id.Write_Register_Button).setOnClickListener(onClickListener);
        camera_Select_Text = (TextView) findViewById(R.id.camera_Select_Text);

        FoodMarketbtn = (ImageView) findViewById(R.id.FoodPostbtn);
        FoodMarketbtn.setOnClickListener(onClickListener);
        LifeMarketbtn = (ImageView) findViewById(R.id.LifePostbtn);
        LifeMarketbtn.setOnClickListener(onClickListener);
        BorrowMarketbtn = (ImageView) findViewById(R.id.BorrowPostbtn);
        BorrowMarketbtn.setOnClickListener(onClickListener);
        WorkMarketbtn = (ImageView) findViewById(R.id.WorkPostbtn);
        WorkMarketbtn.setOnClickListener(onClickListener);

        FoodText = findViewById(R.id.FoodPostText);
        LifeText = findViewById(R.id.LifePostText);
        BorrowText = findViewById(R.id.BorrowPostText);
        WorkText = findViewById(R.id.WorkPostText);
        FirebaseStorage Firebasestorage = FirebaseStorage.getInstance();
        Storagereference = Firebasestorage.getReference();
        Marketmodel = (MarketModel) getIntent().getSerializableExtra("marketInfo");                       // part17 : postInfo의 정체!!!!!!!!!!!!!!!!!!(29')
        MarketInit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                        // part12 : parents 안에 ContentsItemView가 있고 ContentsItemView안에 imageView가 있는 구조 (11')
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.round);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            //기존 이미지 지우기
            PhotoList0.setImageResource(0);
            PhotoList1.setImageResource(0);
            PhotoList2.setImageResource(0);
            PhotoList3.setImageResource(0);
            PhotoList4.setImageResource(0);

            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            if (photos != null) {
                ImageList = null;
                ArrayList_SelectedPhoto.addAll(photos);
                for(int i=0;i<photos.size();i++){
                    switch (i){
                        case 0 :
                            PhotoList0.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList0.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(photos.get(0)).centerInside().override(500).into(PhotoList0);
                            break;
                        case 1 :
                            PhotoList1.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList1.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(photos.get(1)).centerInside().override(500).into(PhotoList1);
                            break;
                        case 2 :
                            PhotoList2.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList2.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(photos.get(2)).centerInside().override(500).into(PhotoList2);
                            break;
                        case 3 :
                            PhotoList3.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList3.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(photos.get(3)).centerInside().override(500).into(PhotoList3);
                            break;
                        case 4 :
                            PhotoList4.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList4.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(photos.get(4)).centerInside().override(500).into(PhotoList4);
                            break;

                    }
                }
                camera_Select_Text.setText(photos.size()+"/5");
            }
        }
    }

    //Activity에서 사용하는 버튼들의 OnClickListener
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.FoodPostbtn:
                    FoodMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                    LifeMarketbtn.setColorFilter(null);
                    BorrowMarketbtn.setColorFilter(null);
                    WorkMarketbtn.setColorFilter(null);
                    FoodText.setTextColor(Color.parseColor("#2fd8df"));
                    LifeText.setTextColor(Color.parseColor("#000000"));
                    BorrowText.setTextColor(Color.parseColor("#000000"));
                    WorkText.setTextColor(Color.parseColor("#000000"));
                    Category = "음식";
                    break;
                case R.id.LifePostbtn:
                    LifeMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                    FoodMarketbtn.setColorFilter(null);
                    BorrowMarketbtn.setColorFilter(null);
                    WorkMarketbtn.setColorFilter(null);
                    FoodText.setTextColor(Color.parseColor("#000000"));
                    LifeText.setTextColor(Color.parseColor("#2fd8df"));
                    BorrowText.setTextColor(Color.parseColor("#000000"));
                    WorkText.setTextColor(Color.parseColor("#000000"));
                    Category = "생필품";
                    break;
                case R.id.BorrowPostbtn:
                    BorrowMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                    FoodMarketbtn.setColorFilter(null);
                    LifeMarketbtn.setColorFilter(null);
                    WorkMarketbtn.setColorFilter(null);
                    FoodText.setTextColor(Color.parseColor("#000000"));
                    LifeText.setTextColor(Color.parseColor("#000000"));
                    BorrowText.setTextColor(Color.parseColor("#2fd8df"));
                    WorkText.setTextColor(Color.parseColor("#000000"));
                    Category = "대여";
                    break;
                case R.id.WorkPostbtn:
                    WorkMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                    FoodMarketbtn.setColorFilter(null);
                    LifeMarketbtn.setColorFilter(null);
                    BorrowMarketbtn.setColorFilter(null);
                    FoodText.setTextColor(Color.parseColor("#000000"));
                    LifeText.setTextColor(Color.parseColor("#000000"));
                    BorrowText.setTextColor(Color.parseColor("#000000"));
                    WorkText.setTextColor(Color.parseColor("#2fd8df"));
                    Category = "용역";
                    break;
                case R.id.back_Button:
                    myStartActivity(MarketActivity.class, Marketmodel);
                    break;
                case R.id.camera_Button_Layout:
                    ArrayList_SelectedPhoto = new ArrayList<>();
                    PhotoUtil intent2 = new PhotoUtil(getApplicationContext());
                    intent2.setMaxSelectCount(5);
                    intent2.setShowCamera(true);
                    intent2.setShowGif(true);
                    intent2.setSelectCheckBox(false);
                    intent2.setMaxGrideItemCount(3);
                    startActivityForResult(intent2, REQUEST_CODE);
                    break;
                case R.id.Write_Register_Button:
                    Modify_Storage_Upload();
                    break;
            }
        }
    };


    //WritePostFragment와 비슷한 형태로 차이점이 있다면, docRef_POSTS_PostUid에 새로운 Uid를 생성 받는 것이 아니라 수정하고자하는 게시물의 Uid를 받아서 쓴다.
    private void Modify_Storage_Upload() {
        final String Title = ((EditText) findViewById(R.id.Post_Title_EditText)).getText().toString();
        final String TextContents = ((EditText) findViewById(R.id.Market_Content_EditText)).getText().toString();
        String Market_Uid = Marketmodel.getMarketModel_Market_Uid();
        final ArrayList<String> LikeList = Marketmodel.getMarketModel_LikeList();
        final String HotMarket = Marketmodel.getMarketModel_HotMarket();
        final String MarketModel_reservation = Marketmodel.getMarketModel_reservation();
        final String MarketModel_deal = Marketmodel.getMarketModel_deal();
        if (Title.length() > 0 && Category != null) {
            LoaderLayout.setVisibility(View.VISIBLE);
            CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage Firebasestorage = FirebaseStorage.getInstance();                                    // part12 :
            Storagereference = Firebasestorage.getReference();
            FirebaseFirestore Firebasefirestore = FirebaseFirestore.getInstance();
            final DocumentReference docRef_MARKETS_MarketUid = Firebasefirestore.collection("MARKETS").document(Market_Uid);     //postInfo가 null이면 그냥 추가 되고 아니면 해당 아게시물 아이디에 해당하는 것으로 추가
            final Date DateOfManufacture = Marketmodel.getMarketModel_DateOfManufacture();          // part17 : null이면 = 새 날짜 / 아니면 = getCreatedAt 날짜 이거 해줘야 수정한게 제일 위로 가지 않음 ((31')
            final ArrayList<String> contentsList = new ArrayList<>();
            final int commentcount = Marketmodel.getMarketModel_CommentCount();
            if (ImageList!=null){
                Marketmodel.setMarketModel_Title(Title);
                Marketmodel.setMarketModel_Text(TextContents);
                Modify_Store_Upload(docRef_MARKETS_MarketUid, Marketmodel);
            }else{
                if (ArrayList_SelectedPhoto!=null){
                    for (int i = 0; i < ArrayList_SelectedPhoto.size(); i++) {                                              // part11 : 안의 자식뷰만큼 반복 (21'15")
                        String path = ArrayList_SelectedPhoto.get(PathCount);
                        contentsList.add(path);
                        String[] pathArray = path.split("\\.");                                         // part14 : 이미지의 확장자를 주어진대로 (2'40")
                        final StorageReference ImagesRef_MARKETS_Uid_PathCount = Storagereference.child("MARKETS/" + docRef_MARKETS_MarketUid.getId() + "/" + PathCount + "." + pathArray[pathArray.length - 1]);
                        try {
                            InputStream Stream = new FileInputStream(new File(ArrayList_SelectedPhoto.get(PathCount)));            // part11 : 경로 설정 (27'20")
                            StorageMetadata Metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                            UploadTask Uploadtask = ImagesRef_MARKETS_Uid_PathCount.putStream(Stream, Metadata);
                            final String Get_MarketUid = Market_Uid;

                            Marketmodel.setMarketModel_ImageList(new ArrayList<String>());
                            final int finalI = i;
                            Uploadtask.addOnFailureListener(new OnFailureListener() {                               // part11 :
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));  // part11 : 메타 데이터를 index에 받아온다.
                                    ImagesRef_MARKETS_Uid_PathCount.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {                                             // part11 : SUCCEESSCOUNT 개의 사진 (37')
                                            contentsList.set(index, uri.toString());                        // part11 : 인덱스를 받아서 URi저장 ( 36'40")
                                            MarketModel marketModel = new MarketModel(Title, TextContents, contentsList,  DateOfManufacture, CurrentUser.getUid(), Get_MarketUid, Category, LikeList, HotMarket,MarketModel_reservation,MarketModel_deal, commentcount);
                                            marketModel.setMarketModel_Market_Uid(Get_MarketUid);
                                            if(finalI == ArrayList_SelectedPhoto.size()-1){
                                                Modify_Store_Upload(docRef_MARKETS_MarketUid, marketModel);
                                            }
                                        }
                                    });
                                }
                            });
                        } catch (FileNotFoundException e) {
                            Log.e("로그", "에러: " + e.toString());
                        }
                        PathCount++;
                    }
                }if (ArrayList_SelectedPhoto.size() == 0) {
                    MarketModel marketModel = new MarketModel(Title,TextContents, DateOfManufacture, CurrentUser.getUid(), Market_Uid, Category, LikeList, HotMarket,MarketModel_reservation,MarketModel_deal, commentcount);
                    marketModel.setMarketModel_Market_Uid(Market_Uid);
                    Modify_Store_Upload(docRef_MARKETS_MarketUid, marketModel);
                }
            }
        } else {
            if(Title.length() == 0){
                Toast.makeText(this, "제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
            }else if(Category == null){
                Toast.makeText(this, "카테고리를 선택해주세요.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 파이어스토어에 바뀐 정보들을 POSTS에 넣는다. WritePostFragment에 있는 것과는 차이가 없다.
    private void Modify_Store_Upload(DocumentReference docRef_MARKETS_MarketUid, final MarketModel marketmodel) {
        docRef_MARKETS_MarketUid.set(marketmodel.getMarketInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LoaderLayout.setVisibility(View.GONE);
                        Intent Resultintent = new Intent();
                        Resultintent.putExtra("marketInfo", marketmodel);                                    // part19 : 수정 후 수정된 정보 즉시 반영 (80')
                        setResult(Activity.RESULT_OK, Resultintent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        LoaderLayout.setVisibility(View.GONE);                                                  // part20 : (37'30") 보면 로딩 뒤에 있는 이미지 클릭시 이ㅏ벤트가 진행되버리는 현상을 방지
                    }
                });
    }

    private void myStartActivity(Class c, MarketModel marketModel) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent Intent_Market_Data = new Intent(this, c);
        Intent_Market_Data.putExtra("marketInfo", marketModel);
        startActivityForResult(Intent_Market_Data, 0);
        finish();
    }
    private void MarketInit() {                                                                               // part17 : (33')
        if (Marketmodel != null) {                                                                             //수정 버튼을 눌러서 들어왔을 때 null이 아니면 == 나 수정 하러 왔음 >> 화면에는 수정하고자하는 게시물의 정보들이 띄워져있음
            Title_EditText.setText(Marketmodel.getMarketModel_Title());
            Selected_EditText.setText(Marketmodel.getMarketModel_Text());
            ImageList = Marketmodel.getMarketModel_ImageList();
            GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.round);
            if (ImageList != null) {
                ArrayList_SelectedPhoto.addAll(ImageList);
                for(int i=0;i<ImageList.size();i++){
                    switch (i){
                        case 0 :
                            PhotoList0.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList0.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(ImageList.get(0)).centerInside().override(500).into(PhotoList0);
                            break;
                        case 1 :
                            PhotoList1.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList1.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(ImageList.get(1)).centerInside().override(500).into(PhotoList1);
                            break;
                        case 2 :
                            PhotoList2.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList2.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(ImageList.get(2)).centerInside().override(500).into(PhotoList2);
                            break;
                        case 3 :
                            PhotoList3.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList3.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(ImageList.get(3)).centerInside().override(500).into(PhotoList3);
                            break;
                        case 4 :
                            PhotoList4.setBackground(drawable);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                PhotoList4.setClipToOutline(true);
                            }
                            Glide.with(getApplicationContext()).load(ImageList.get(4)).centerInside().override(500).into(PhotoList4);
                            break;
                    }
                }
                camera_Select_Text.setText(ImageList.size()+"/5");
            }
            Category = Marketmodel.getMarketModel_Category();
            if(Category.equals("음식")){
                FoodMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                FoodText.setTextColor(Color.parseColor("#2fd8df"));
            }else if(Category.equals("생필품")){
                LifeMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                LifeText.setTextColor(Color.parseColor("#2fd8df"));
            }else if(Category.equals("대여")){
                BorrowMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                BorrowText.setTextColor(Color.parseColor("#2fd8df"));
            }else if(Category.equals("용역")){
                WorkMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                WorkText.setTextColor(Color.parseColor("#2fd8df"));
            }
        }
    }
}