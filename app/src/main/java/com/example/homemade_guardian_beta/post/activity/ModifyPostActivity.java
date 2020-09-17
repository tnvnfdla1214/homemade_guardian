package com.example.homemade_guardian_beta.post.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.homemade_guardian_beta.photo.PhotoUtil;
import com.example.homemade_guardian_beta.photo.activity.PhotoPickerActivity;
import com.example.homemade_guardian_beta.model.post.PostModel;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.common.view.ViewPagerAdapter;
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
import static com.example.homemade_guardian_beta.post.PostUtil.GALLERY_IMAGE;
import static com.example.homemade_guardian_beta.post.PostUtil.INTENT_MEDIA;
import static com.example.homemade_guardian_beta.post.PostUtil.isStorageUrl;

//게시물의 수정을 위한 액티비티 이다. 형태는 WritePostFragment와 비슷하지만, 수정은 Activity라는 차이가 있다.
//      Ex) 게시물에서 수정을 눌렀을 때 실행되는 액티비티이다.
// +++수정을 추가해야함, 추가한 사진이 보이게 해주는 기능 추가해야함

public class ModifyPostActivity extends BasicActivity {
    private PostModel Postmodel;                                        //UserModel 참조 선언

    private int PathCount;                                              //이미지 리스트 중 몇번째인지 나타내는 변수
    public  ArrayList<String> ArrayList_SelectedPhoto = new ArrayList<>();       //선택한 이미지들이 담기는 리스트

    private RelativeLayout ButtonsBackgroundLayout;                     ////결과적으로는 안쓸 버튼
    private RelativeLayout LoaderLayout;                                //로딩중을 나타내는 layout 선언
    private EditText Selected_EditText;                                  ////뭔지 모르겠음
    private EditText Title_EditText;                                     //수정하려는 게시물의 제목

    private FirebaseUser CurrentUser;                                   //파이어베이스 데이터 상의 현재 사용자
    private StorageReference Storagereference;                                //파이어스토리지에 접근하기 위한 선언

    public final static int REQUEST_CODE = 1;                           //REQUEST_CODE 초기화



    private ImageView Selected_ImageView;
    private Button Select_Post_Image_Button;
    private ArrayList<String> ImageList;            //게시물의 이미지 리스트
    private ViewPager Viewpager;                    //이미지들을 보여주기 위한 ViewPager 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_post);
        setToolbarTitle("게시글 수정");


        ButtonsBackgroundLayout = findViewById(R.id.ButtonsBackground_Layout);
        LoaderLayout = findViewById(R.id.Loader_Lyaout);
        Title_EditText = findViewById(R.id.Post_Title_EditText);
        Selected_EditText = findViewById(R.id.contentsEditText);
        Select_Post_Image_Button = findViewById(R.id.Select_Post_Image_Button);
        Viewpager = findViewById(R.id.ViewPager);
        findViewById(R.id.Post_Write_Button).setOnClickListener(onClickListener);
        findViewById(R.id.Select_Post_Image_Button).setOnClickListener(onClickListener);
        findViewById(R.id.imageModify).setOnClickListener(onClickListener);
        findViewById(R.id.Comment_Delete_Button).setOnClickListener(onClickListener);
        Spinner Post_Category_Spinner = (Spinner)findViewById(R.id.Post_Category_Spinner);
        ArrayAdapter Post_Category_Adapter = ArrayAdapter.createFromResource(this, R.array.Category, android.R.layout.simple_spinner_item);
        Post_Category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Post_Category_Spinner.setAdapter(Post_Category_Adapter);

        ButtonsBackgroundLayout.setOnClickListener(onClickListener);
        Title_EditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selected_EditText = null;
                }
            }
        });

        FirebaseStorage Firebasestorage = FirebaseStorage.getInstance();
        Storagereference = Firebasestorage.getReference();
        Postmodel = (PostModel) getIntent().getSerializableExtra("postInfo");                       // part17 : postInfo의 정체!!!!!!!!!!!!!!!!!!(29')
        postInit();
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
                ArrayList_SelectedPhoto.addAll(photos);
                ImageList = ArrayList_SelectedPhoto;
                if(ImageList != null) {
                    Viewpager.setAdapter(new ViewPagerAdapter(getApplicationContext(), ImageList));
                }
                Select_Post_Image_Button.setText(Html.fromHtml(ArrayList_SelectedPhoto.size()+"/5"+"<br/>"+"클릭시 이미지 재선택"));
            }
        }
    }

    //Activity에서 사용하는 버튼들의 OnClickListener
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Post_Write_Button:
                    Modify_Storage_Upload();
                    Toast.makeText(getApplicationContext(), "수정 성공", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Select_Post_Image_Button:
                    ArrayList_SelectedPhoto = new ArrayList<>();
                    PhotoUtil intent = new PhotoUtil(ModifyPostActivity.this);
                    intent.setMaxSelectCount(5);
                    intent.setShowCamera(true);
                    intent.setShowGif(true);
                    intent.setSelectCheckBox(false);
                    intent.setMaxGrideItemCount(3);
                    startActivityForResult(intent, REQUEST_CODE);
                break;
            }
        }
    };

    //////아마 안 쓸듯
    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {               // part12 : 설정 (16'10")
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Selected_EditText = (EditText) v;
            }
        }
    };

    //WritePostFragment와 비슷한 형태로 차이점이 있다면, docRef_POSTS_PostUid에 새로운 Uid를 생성 받는 것이 아니라 수정하고자하는 게시물의 Uid를 받아서 쓴다.
    private void Modify_Storage_Upload() {
        final String Title = ((EditText) findViewById(R.id.Post_Title_EditText)).getText().toString();
        final String TextContents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();
        String Post_Uid = Postmodel.getPostModel_Post_Uid();
        final ArrayList<String> LikeList = Postmodel.getPostModel_LikeList();
        final String Category = ((Spinner)findViewById(R.id.Post_Category_Spinner)).getSelectedItem().toString();
        final String HotPost = Postmodel.getPostModel_HotPost();
        Log.e("로그", "Uid 111 : " + Post_Uid);
        Log.e("로그", "카테고리 111 : " + Category);
        if (Title.length() > 0) {
            LoaderLayout.setVisibility(View.VISIBLE);                                                   // part13 : 로딩 화면 (2')
            final ArrayList<String> ImageList = new ArrayList<>();                                   // part11 : contentsList에는 컨텐츠 내용이
            CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage Firebasestorage = FirebaseStorage.getInstance();                                    // part12 :
            Storagereference = Firebasestorage.getReference();
            FirebaseFirestore Firebasefirestore = FirebaseFirestore.getInstance();
            final DocumentReference docRef_POSTS_PostUid = Firebasefirestore.collection("POSTS").document(Post_Uid);     //postInfo가 null이면 그냥 추가 되고 아니면 해당 아게시물 아이디에 해당하는 것으로 추가
            final Date DateOfManufacture = Postmodel.getPostModel_DateOfManufacture();          // part17 : null이면 = 새 날짜 / 아니면 = getCreatedAt 날짜 이거 해줘야 수정한게 제일 위로 가지 않음 ((31')
            for (int i = 0; i < ArrayList_SelectedPhoto.size(); i++) {                                              // part11 : 안의 자식뷰만큼 반복 (21'15")

                // part11 : 자식뷰 저장
                // part11 : EditText가 아닐 때 ---> 이미지 뷰일때는
                String path = ArrayList_SelectedPhoto.get(PathCount);
                ImageList.add(path);
                String[] pathArray = path.split("\\.");                                         // part14 : 이미지의 확장자를 주어진대로 (2'40")
                final StorageReference ImagesRef_POSTS_Uid_PathCount = Storagereference.child("POSTS/" + docRef_POSTS_PostUid.getId() + "/" + PathCount + "." + pathArray[pathArray.length - 1]);
                try {
                    InputStream Stream = new FileInputStream(new File(ArrayList_SelectedPhoto.get(PathCount)));            // part11 : 경로 설정 (27'20")
                    StorageMetadata Metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (ImageList.size() - 1)).build();
                    UploadTask Uploadtask = ImagesRef_POSTS_Uid_PathCount.putStream(Stream, Metadata);
                    final String Get_PostUid = Post_Uid;
                    Uploadtask.addOnFailureListener(new OnFailureListener() {                               // part11 :
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));  // part11 : 메타 데이터를 index에 받아온다.
                            ImagesRef_POSTS_Uid_PathCount.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {                                             // part11 : SUCCEESSCOUNT 개의 사진 (37')
                                    ImageList.set(index, uri.toString());                        // part11 : 인덱스를 받아서 URi저장 ( 36'40")
                                        PostModel Postmodel = new PostModel(Title, TextContents, ImageList,  DateOfManufacture, CurrentUser.getUid(), Get_PostUid, Category, LikeList, HotPost);
                                        Postmodel.setPostModel_Post_Uid(Get_PostUid);
                                        Modify_Store_Upload(docRef_POSTS_PostUid, Postmodel);
                                }
                            });
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그", "에러: " + e.toString());
                }
                PathCount++;
            }
            if (ArrayList_SelectedPhoto.size() == 0) {
                Log.e("로그", "카테고리 222 : " + Category);
                Log.e("로그", "Uid 222 : " + Post_Uid);
                PostModel Postmodel = new PostModel(Title,TextContents, DateOfManufacture, CurrentUser.getUid(), Post_Uid, Category, LikeList, HotPost);
                Postmodel.setPostModel_Post_Uid(Post_Uid);
                Modify_Store_Upload(docRef_POSTS_PostUid,Postmodel);
            }
        } else {
            Toast.makeText(this, "제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    // 파이어스토어에 바뀐 정보들을 POSTS에 넣는다. WritePostFragment에 있는 것과는 차이가 없다.
    private void Modify_Store_Upload(DocumentReference docRef_POSTS_PostUid, final PostModel Postmodel) {
        docRef_POSTS_PostUid.set(Postmodel.getPostInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LoaderLayout.setVisibility(View.GONE);
                        Intent Resultintent = new Intent();
                        Resultintent.putExtra("postInfo", Postmodel);                                    // part19 : 수정 후 수정된 정보 즉시 반영 (80')
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

    private void myStartActivity(Class c, int media, int requestCode) {
        Intent intent = new Intent(this, c);
        intent.putExtra(INTENT_MEDIA, media);
        startActivityForResult(intent, requestCode);
    }
    private void postInit() {                                                                               // part17 : (33')
        if (Postmodel != null) {                                                                             //수정 버튼을 눌러서 들어왔을 때 null이 아니면 == 나 수정 하러 왔음 >> 화면에는 수정하고자하는 게시물의 정보들이 띄워져있음
            Title_EditText.setText(Postmodel.getPostModel_Title());
            Selected_EditText.setText(Postmodel.getPostModel_Text());
            if(Postmodel.getPostModel_ImageList() != null ){
                Viewpager.setAdapter(new ViewPagerAdapter(getApplicationContext(), Postmodel.getPostModel_ImageList()));
                Select_Post_Image_Button.setText(Html.fromHtml(Postmodel.getPostModel_ImageList().size()+"/20"+"<br/>"+"클릭시 이미지 재선택"));
            }
            Log.e("로그", "카 : " + Postmodel.getPostInfo());
            Log.e("로그", "카테고리 1 : " + Postmodel.getPostModel_Category());
            int Selected_Category = 0;
            if(Postmodel.getPostModel_Category().equals("음식")) {
                Log.e("로그", "카테고리 2 : " + Postmodel.getPostModel_Category());
                ((Spinner)findViewById(R.id.Post_Category_Spinner)).setSelection(0);
            }else if (Postmodel.getPostModel_Category().equals("생필품")){
                ((Spinner)findViewById(R.id.Post_Category_Spinner)).setSelection(1);
            }else if (Postmodel.getPostModel_Category().equals("대여")){
                ((Spinner)findViewById(R.id.Post_Category_Spinner)).setSelection(2);
            }else if(Postmodel.getPostModel_Category().equals("용역")){
                ((Spinner)findViewById(R.id.Post_Category_Spinner)).setSelection(3);
            }
             //((Spinner)findViewById(R.id.Post_Category_Spinner)).setSelection(Selected_Category);

            //ArrayList<String> contentsList = Postmodel.getContents();
//            for (int i = 0; i < contentsList.size(); i++) {
//                String contents = contentsList.get(i);
//                if (isStorageUrl(contents)) {
//                    pathList.add(contents);
//                    ContentsItemView contentsItemView = new ContentsItemView(this);
//                    parent.addView(contentsItemView);
//
//                    contentsItemView.setImage(contents);
//                    contentsItemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            buttonsBackgroundLayout.setVisibility(View.VISIBLE);
//                            selectedImageVIew = (ImageView) v;
//                        }
//                    });
//
//                    contentsItemView.setOnFocusChangeListener(onFocusChangeListener);
//                    if (i < contentsList.size() - 1) {
//                        String nextContents = contentsList.get(i + 1);
//                        if (!isStorageUrl(nextContents)) {
//                            contentsItemView.setText(nextContents);
//                        }
//                    }
//                } else if (i == 0) {
//                    contentsEditText.setText(contents);
//                }
//            }
        }
    }
}