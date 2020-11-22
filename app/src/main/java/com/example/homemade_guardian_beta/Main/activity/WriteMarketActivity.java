package com.example.homemade_guardian_beta.Main.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.Main.bottombar.WriteMarketFragment;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.photo.PhotoUtil;
import com.example.homemade_guardian_beta.photo.activity.PhotoPickerActivity;
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

public class WriteMarketActivity extends BasicActivity {

    private MarketModel marketModel;

    private FirebaseUser currentUser;

    private ImageView PhotoList0, PhotoList1, PhotoList2, PhotoList3, PhotoList4;
    private ImageView FoodMarketbtn,LifeMarketbtn,BorrowMarketbtn,WorkMarketbtn;
    private String Category = null;

    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RelativeLayout loaderLayout;
    private RelativeLayout buttonsBackgroundLayout;

    private TextView camera_Select_Text;

    private int pathCount;

    public final static int REQUEST_CODE = 1;
    private TextView FoodText;
    private TextView LifeText;
    private TextView BorrowText;
    private TextView WorkText;

    Loding_Dialog dialog = new Loding_Dialog(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writemarket);
        setToolbarTitle("");

        findViewById(R.id.back_Button).setOnClickListener(onClickListener);
        findViewById(R.id.camera_Button_Layout).setOnClickListener(onClickListener);
        findViewById(R.id.Write_Register_Button).setOnClickListener(onClickListener);
        camera_Select_Text = (TextView) findViewById(R.id.camera_Select_Text);

        PhotoList0 = (ImageView)findViewById(R.id.PhotoList0);
        PhotoList1 = (ImageView)findViewById(R.id.PhotoList1);
        PhotoList2 = (ImageView)findViewById(R.id.PhotoList2);
        PhotoList3 = (ImageView)findViewById(R.id.PhotoList3);
        PhotoList4 = (ImageView)findViewById(R.id.PhotoList4);
        FoodMarketbtn = (ImageView) findViewById(R.id.FoodPostbtn);
        FoodMarketbtn.setOnClickListener(onClickListener);
        LifeMarketbtn = (ImageView) findViewById(R.id.LifePostbtn);
        LifeMarketbtn.setOnClickListener(onClickListener);
        BorrowMarketbtn = (ImageView) findViewById(R.id.BorrowPostbtn);
        BorrowMarketbtn.setOnClickListener(onClickListener);
        WorkMarketbtn = (ImageView) findViewById(R.id.WorkPostbtn);
        WorkMarketbtn.setOnClickListener(onClickListener);
        //loaderLayout = (RelativeLayout)findViewById(R.id.Loader_Lyaout);

        FoodText = findViewById(R.id.FoodPostText);
        LifeText = findViewById(R.id.LifePostText);
        BorrowText = findViewById(R.id.BorrowPostText);
        WorkText = findViewById(R.id.WorkPostText);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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
            if (photos != null || selectedPhotos!=null) {
                selectedPhotos.addAll(photos);
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

    private void storageUpload() {
        final String Market_Title_EditText = ((EditText) findViewById(R.id.Market_Title_EditText)).getText().toString();
        final String Market_Content_EditText = ((EditText) findViewById(R.id.Market_Content_EditText)).getText().toString();
        final ArrayList<String> LikeList = new ArrayList<>();
        final String HotMarket = "X";
        final String MarketModel_reservation = "X";
        final String MarketModel_deal = "X";
        final int market_commentcount = 0;

        if (Market_Title_EditText.length() > 0 && Category != null) {
            String MarketID = null;
            //Log.d();

            dialog.callDialog();
            final ArrayList<String> contentsList = new ArrayList<>();                                   // part11 : contentsList에는 컨텐츠 내용이
            FirebaseStorage storage = FirebaseStorage.getInstance();                                    // part12 :
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            ///
            MarketID = firebaseFirestore.collection("MARKETS").document().getId();
            final DocumentReference documentReference =firebaseFirestore.collection("MARKETS").document(MarketID);     //postInfo가 null이면 그냥 추가 되고 아니면 해당 아게시물 아이디에 해당하는 것으로 추가
            final Date date = marketModel == null ? new Date() : marketModel.getMarketModel_DateOfManufacture();          // part17 : null이면 = 새 날짜 / 아니면 = getCreatedAt 날짜 이거 해줘야 수정한게 제일 위로 가지 않음 ((31')
            for (int i = 0; i < selectedPhotos.size(); i++) {                                              // part11 : 안의 자식뷰만큼 반복 (21'15")
                String path = selectedPhotos.get(pathCount);
                contentsList.add(path);

                String[] pathArray = path.split("\\.");                                         // part14 : 이미지의 확장자를 주어진대로 (2'40")
                final StorageReference mountainImagesRef = storageRef.child("MARKETS/" + documentReference.getId() + "/" + pathCount + "." + pathArray[pathArray.length - 1]);
                try {
                    InputStream stream = new FileInputStream(new File(selectedPhotos.get(pathCount)));            // part11 : 경로 설정 (27'20")
                    StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                    UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                    final String newMarketID = MarketID;
                    final int finalI = i;
                    uploadTask.addOnFailureListener(new OnFailureListener() {                               // part11 :
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));  // part11 : 메타 데이터를 index에 받아온다.
                            mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    contentsList.set(index, uri.toString());                        // part11 : 인덱스를 받아서 URi저장 ( 36'40")
                                    MarketModel marketModel = new MarketModel(Market_Title_EditText, Market_Content_EditText, contentsList,  date, currentUser.getUid(), newMarketID, Category, LikeList, HotMarket,MarketModel_reservation,MarketModel_deal, market_commentcount);
                                    marketModel.setMarketModel_Market_Uid(newMarketID);
                                    if(finalI == selectedPhotos.size()-1){
                                        storeUpload(documentReference, marketModel);
                                    }
                                }
                            });
                        }
                    });
                } catch (FileNotFoundException e) {
                }
                pathCount++;
            }
            if (selectedPhotos.size() == 0) {
                MarketModel marketModel = new MarketModel(Market_Title_EditText, Market_Content_EditText, date, currentUser.getUid(), MarketID, Category, LikeList, HotMarket,MarketModel_reservation,MarketModel_deal, market_commentcount);
                marketModel.setMarketModel_Market_Uid(MarketID);
                storeUpload(documentReference, marketModel);
            }
        } else {
            if(Market_Title_EditText.length() == 0){
                Toast.makeText(this.getApplicationContext(), "제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
            }else if(Category == null){
                Toast.makeText(this.getApplicationContext(), "카테고리를 선택해주세요.",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void storeUpload(DocumentReference documentReference, final MarketModel marketModel) {
        documentReference.set(marketModel.getMarketInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.calldismiss();
                        //loaderLayout.setVisibility(View.GONE);
                        //Intent resultIntent = new Intent();
                        //resultIntent.putExtra("marketinfo", marketModel);                                    // part19 : 수정 후 수정된 정보 즉시 반영 (80')
                        //getActivity().setResult(RESULT_OK, resultIntent);
                        //setResult(Activity.RESULT_OK, resultIntent);
                        //Intent intentpage = new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(intentpage);
                        finish();
                        dialog.calldismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //dialog.calldismiss();
                        //loaderLayout.setVisibility(View.GONE);                                                  // part20 : (37'30") 보면 로딩 뒤에 있는 이미지 클릭시 이ㅏ벤트가 진행되버리는 현상을 방지
                    }
                });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.FoodPostbtn:
                    //ImageView FoodPostbtn = (ImageView) view.findViewById(R.id.FoodPostbtn);
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
                    finish();
                    break;
                case R.id.camera_Button_Layout:
                    selectedPhotos = new ArrayList<>();
                    PhotoUtil intent2 = new PhotoUtil(getApplicationContext());
                    intent2.setMaxSelectCount(5);
                    intent2.setShowCamera(true);
                    intent2.setShowGif(true);
                    intent2.setSelectCheckBox(false);
                    intent2.setMaxGrideItemCount(3);
                    startActivityForResult(intent2, REQUEST_CODE);
                    break;
                case R.id.Write_Register_Button:
                    storageUpload();
                    break;
            }
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}