package com.example.homemade_guardian_beta.Main.bottombar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.photo.activity.PhotoPickerActivity;
import com.example.homemade_guardian_beta.photo.PhotoUtil;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.Main.common.ViewPagerAdapter;
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
import static android.app.Activity.RESULT_OK;
import static com.example.homemade_guardian_beta.Main.common.Util.INTENT_MEDIA;

public class WriteMarketFragment extends Fragment {
    private FirebaseUser currentUser;
    private StorageReference storageRef;
    private RelativeLayout buttonsBackgroundLayout;
    private RelativeLayout loaderLayout;
    private EditText selectedEditText;
    private EditText titleEditText;
    private MarketModel marketModel;
    private int pathCount;
    public final static int REQUEST_CODE = 1;
    public  ArrayList<String> selectedPhotos = new ArrayList<>();
////////////////////////
    private ImageView Selected_ImageView;
    private Button Select_Market_Image_Button;
    private ArrayList<String> ImageList;            //게시물의 이미지 리스트
    private ViewPager Viewpager;                    //이미지들을 보여주기 위한 ViewPager 선언
    private ImageView FoodMarketbtn;
    private ImageView LifeMarketbtn;
    private ImageView BorrowMarketbtn;
    private ImageView WorkMarketbtn;
    private String Category = null;

    private String title;
    private String textcontents;

    /////////////////////////////////////////////////////
    private ImageView imagelist0, imagelist1, imagelist2, imagelist3, imagelist4;
    //////////////////////////////////////////////////////



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_writemarket, container, false);
        Toolbar myToolbar = view.findViewById(R.id.toolbar_title_write);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.image_round);


        buttonsBackgroundLayout = view.findViewById(R.id.ButtonsBackground_Layout);
        loaderLayout = view.findViewById(R.id.Loader_Lyaout);
        titleEditText = view.findViewById(R.id.Post_Title_EditText);
        selectedEditText = view.findViewById(R.id.contentsEditText);
        //////////////////
        //Selected_ImageView = view.findViewById(R.id.Selected_ImageView);
        view.findViewById(R.id.Post_Write_Button).setOnClickListener(onClickListener);
        Select_Market_Image_Button = view.findViewById(R.id.Select_Post_Image_Button);
        Select_Market_Image_Button.setOnClickListener(onClickListener);
//        Select_Post_Image_Button.setBackground(drawable);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Select_Post_Image_Button.setClipToOutline(true);
//        }
        FoodMarketbtn = (ImageView) view.findViewById(R.id.FoodPostbtn);
        FoodMarketbtn.setOnClickListener(onClickListener);
        LifeMarketbtn = (ImageView) view.findViewById(R.id.LifePostbtn);
        LifeMarketbtn.setOnClickListener(onClickListener);
        BorrowMarketbtn = (ImageView) view.findViewById(R.id.BorrowPostbtn);
        BorrowMarketbtn.setOnClickListener(onClickListener);
        WorkMarketbtn = (ImageView) view.findViewById(R.id.WorkPostbtn);
        WorkMarketbtn.setOnClickListener(onClickListener);
        ////////
        Viewpager = view.findViewById(R.id.ViewPager);
        Viewpager.setBackground(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Viewpager.setClipToOutline(true);
        }

        buttonsBackgroundLayout.setOnClickListener(onClickListener);
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    selectedEditText = null;
                }
            }
        });


        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        marketModel = (MarketModel) getActivity().getIntent().getSerializableExtra("marketInfo");                       // part17 : postInfo의 정체!!!!!!!!!!!!!!!!!!(29')


        /////////////////////////////////////////////////
        imagelist0 = (ImageView)view.findViewById(R.id.imagelist0);
        imagelist1 = (ImageView)view.findViewById(R.id.imagelist1);
        imagelist2 = (ImageView)view.findViewById(R.id.imagelist2);
        imagelist3 = (ImageView)view.findViewById(R.id.imagelist3);
        imagelist4 = (ImageView)view.findViewById(R.id.imagelist4);
        //////////////////////////////////////////////////

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                        // part12 : parents 안에 ContentsItemView가 있고 ContentsItemView안에 imageView가 있는 구조 (11')
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            if (photos != null) {
                selectedPhotos.addAll(photos);
                ////////////////////
                //Selected_ImageView.setImageURI(Uri.parse(selectedPhotos.get(0)));
                ImageList = selectedPhotos;
                if(ImageList != null) {
                    GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.image_round);
                    Viewpager.setBackground(drawable);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Viewpager.setClipToOutline(true);
                    }
                    String ViewpagerState = "Disable";
                    Viewpager.setAdapter(new ViewPagerAdapter(getContext(), ImageList, ViewpagerState));
                }
                Select_Market_Image_Button.setText(Html.fromHtml(selectedPhotos.size()+"/5"+"<br/>"+"클릭시 이미지 재선택"));

                for(int i=0;i<photos.size();i++){
                    switch (i){
                        case 0 :
                            Glide.with(getActivity()).load(photos.get(0)).centerInside().override(500).into(imagelist0);
                            break;
                        case 1 :
                            Glide.with(getActivity()).load(photos.get(1)).centerInside().override(500).into(imagelist1);
                            break;
                        case 2 :
                            Glide.with(getActivity()).load(photos.get(2)).centerInside().override(500).into(imagelist2);
                            break;
                        case 3 :
                            Glide.with(getActivity()).load(photos.get(3)).centerInside().override(500).into(imagelist3);
                            break;
                        case 4 :
                            Glide.with(getActivity()).load(photos.get(4)).centerInside().override(500).into(imagelist4);
                            break;

                    }
                }

            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Post_Write_Button:
                    storageUpload();
                    break;
                case R.id.Select_Post_Image_Button:
                    //////////////
                    selectedPhotos = new ArrayList<>();

                    PhotoUtil intent = new PhotoUtil(getActivity());
                    intent.setMaxSelectCount(5);
                    intent.setShowCamera(true);
                    intent.setShowGif(true);
                    intent.setSelectCheckBox(false);
                    intent.setMaxGrideItemCount(3);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.FoodPostbtn:
                    //ImageView FoodPostbtn = (ImageView) view.findViewById(R.id.FoodPostbtn);
                    FoodMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                    LifeMarketbtn.setColorFilter(null);
                    BorrowMarketbtn.setColorFilter(null);
                    WorkMarketbtn.setColorFilter(null);
                    Category = "음식";
                    break;
                case R.id.LifePostbtn:
                    LifeMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                    FoodMarketbtn.setColorFilter(null);
                    BorrowMarketbtn.setColorFilter(null);
                    WorkMarketbtn.setColorFilter(null);
                    Category = "생필품";
                    break;
                case R.id.BorrowPostbtn:
                    BorrowMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                    FoodMarketbtn.setColorFilter(null);
                    LifeMarketbtn.setColorFilter(null);
                    WorkMarketbtn.setColorFilter(null);
                    Category = "대여";
                    break;
                case R.id.WorkPostbtn:
                    WorkMarketbtn.setColorFilter(Color.parseColor("#2fd8df"), PorterDuff.Mode.SRC_IN);
                    FoodMarketbtn.setColorFilter(null);
                    LifeMarketbtn.setColorFilter(null);
                    BorrowMarketbtn.setColorFilter(null);
                    Category = "용역";
                    break;
                case R.id.ButtonsBackground_Layout:
                    if (buttonsBackgroundLayout.getVisibility() == View.VISIBLE) {
                        buttonsBackgroundLayout.setVisibility(View.GONE);                               // part12 : 실행되고나면 사라지게 설정 (15'19")
                    }
                    break;
            }
        }
    };

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {               // part12 : 설정 (16'10")
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                selectedEditText = (EditText) v;
            }
        }
    };

    private void storageUpload() {
        title = ((EditText) getView().findViewById(R.id.Post_Title_EditText)).getText().toString();
        textcontents = ((EditText) getView().findViewById(R.id.contentsEditText)).getText().toString();
        final ArrayList<String> LikeList = new ArrayList<>();
        final String HotMarket = "X";
        final String MarketModel_reservation = "X";
        final String MarketModel_deal = "X";

        Log.e("로그", "카테고리 : " + Category);
        if (title.length() > 0 && Category != null) {
            String MarketID = null;
            loaderLayout.setVisibility(View.VISIBLE);                                                   // part13 : 로딩 화면 (2')
            final ArrayList<String> contentsList = new ArrayList<>();                                   // part11 : contentsList에는 컨텐츠 내용이
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();                                    // part12 :
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            ///
            MarketID = firebaseFirestore.collection("MARKETS").document().getId();
            final DocumentReference documentReference =firebaseFirestore.collection("MARKETS").document(MarketID);     //postInfo가 null이면 그냥 추가 되고 아니면 해당 아게시물 아이디에 해당하는 것으로 추가
            final Date date = marketModel == null ? new Date() : marketModel.getMarketModel_DateOfManufacture();          // part17 : null이면 = 새 날짜 / 아니면 = getCreatedAt 날짜 이거 해줘야 수정한게 제일 위로 가지 않음 ((31')
            Log.d("로그","111");
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
                                            Log.e("로그", "카테고리 11111111111: " + Category);
                                            MarketModel marketModel = new MarketModel(title, textcontents, contentsList,  date, currentUser.getUid(), newMarketID, Category, LikeList, HotMarket,MarketModel_reservation,MarketModel_deal);
                                            marketModel.setMarketModel_Market_Uid(newMarketID);
                                            storeUpload(documentReference, marketModel);
                                        }
                                    });
                                }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그", "에러: " + e.toString());
                }
                pathCount++;
            }
            if (selectedPhotos.size() == 0) {
                Log.e("로그", "카테고리 22222222222222222222 : " + Category);
                MarketModel marketModel = new MarketModel(title, textcontents, date, currentUser.getUid(), MarketID, Category, LikeList, HotMarket,MarketModel_reservation,MarketModel_deal);
                marketModel.setMarketModel_Market_Uid(MarketID);
                storeUpload(documentReference, marketModel);
            }
        } else {
            if(title.length() == 0){
                Toast.makeText(getActivity(), "제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
            }else if(Category == null){
                Toast.makeText(getActivity(), "카테고리를 선택해주세요.",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void storeUpload(DocumentReference documentReference, final MarketModel marketModel) {
        documentReference.set(marketModel.getMarketInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loaderLayout.setVisibility(View.GONE);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("marketinfo", marketModel);                                    // part19 : 수정 후 수정된 정보 즉시 반영 (80')
                        getActivity().setResult(RESULT_OK, resultIntent);
                        Intent intentpage = new Intent(getActivity(), MainActivity.class);
                        startActivity(intentpage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("로그", "Error writing document", e);
                        loaderLayout.setVisibility(View.GONE);                                                  // part20 : (37'30") 보면 로딩 뒤에 있는 이미지 클릭시 이ㅏ벤트가 진행되버리는 현상을 방지
                    }
                });
    }

    private void myStartActivity(Class c, int media, int requestCode) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra(INTENT_MEDIA, media);
        startActivityForResult(intent, requestCode);
    }

    public Boolean WritePostFragmentDataCheck(){
        title = ((EditText) getView().findViewById(R.id.Post_Title_EditText)).getText().toString();
        textcontents = ((EditText) getView().findViewById(R.id.contentsEditText)).getText().toString();
        if(title.length()!=0||title.length()!=0){
            return true;
        }
        return false;
    }
}