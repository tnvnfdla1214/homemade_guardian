package com.example.homemade_guardian_beta.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.homemade_guardian_beta.MainActivity;
import com.example.homemade_guardian_beta.Photo.PhotoPickerActivity;
import com.example.homemade_guardian_beta.Photo.utils.YPhotoPickerIntent;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.PostModel;
import com.example.homemade_guardian_beta.post.activity.GalleryActivity;
import com.example.homemade_guardian_beta.post.view.ContentsItemView;
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
import static com.example.homemade_guardian_beta.post.PostUtil.INTENT_MEDIA;
import static com.example.homemade_guardian_beta.post.PostUtil.GALLERY_IMAGE;
import static com.example.homemade_guardian_beta.post.PostUtil.isStorageUrl;
import static com.example.homemade_guardian_beta.post.PostUtil.storageUrlToName;

public class WritePostFragment extends Fragment {
    private static final String TAG = "WritePostActivity";
    private FirebaseUser currentUser;
    private StorageReference storageRef;
    private ArrayList<String> pathList = new ArrayList<>();                                             // part11 : 이미지들의 경로를 넣을 리스트 생성 (18'20)
    private LinearLayout parent;
    private RelativeLayout buttonsBackgroundLayout;
    private RelativeLayout loaderLayout;
    private ImageView selectedImageVIew;
    private EditText selectedEditText;
    private EditText contentsEditText;
    private EditText titleEditText;
    private PostModel postModel;
    private int pathCount, successCount;
    public final static int REQUEST_CODE = 1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static ArrayList<String> selectedPhotos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_writepost, container, false);
        ////
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("게시글 작성");
        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("게시글 작성");
        }


        buttonsBackgroundLayout = view.findViewById(R.id.buttonsBackgroundLayout);
        loaderLayout = view.findViewById(R.id.loaderLyaout);
        titleEditText = view.findViewById(R.id.titleEditText);

        view.findViewById(R.id.check).setOnClickListener(onClickListener);
        view.findViewById(R.id.image).setOnClickListener(onClickListener);
        //view.findViewById(R.id.video).setOnClickListener(onClickListener);
        view.findViewById(R.id.imageModify).setOnClickListener(onClickListener);
        //view.findViewById(R.id.videoModify).setOnClickListener(onClickListener);
        view.findViewById(R.id.delete).setOnClickListener(onClickListener);

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

        ////
        postModel = (PostModel) getActivity().getIntent().getSerializableExtra("postInfo");                       // part17 : postInfo의 정체!!!!!!!!!!!!!!!!!!(29')
        //postInit();
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
//        switch (requestCode) {
//            case 0:
//                if (resultCode == RESULT_OK) {
//                    String path = data.getStringExtra(INTENT_PATH);
//                    pathList.add(path);                                                                 // part11 : 이미지들의 경로를 저장 (18'40")
//
//                    ContentsItemView contentsItemView = new ContentsItemView(getActivity());             // part11 : edittext랑 imageview를 계속 넣어줄 거임
//
//                    if (selectedEditText == null) {
//                        parent.addView(contentsItemView);
//                    } else {
//                        for (int i = 0; i < parent.getChildCount(); i++) {                               // part12 : Focus를 등록 Focus를 기준으로 contentsItemView생성 (17'10")
//                            if (parent.getChildAt(i) == selectedEditText.getParent()) {
//                                parent.addView(contentsItemView, i + 1);
//                                break;
//                            }
//                        }
//                    }
//
//                    contentsItemView.setImage(path);
//                    contentsItemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {                                           // part12 : parents 안에 ContentsItemView가 있고 ContentsItemView안에 imageView가 있는 구조 (11')
//                            buttonsBackgroundLayout.setVisibility(View.VISIBLE);
//                            selectedImageVIew = (ImageView) v;
//                        }
//                    });
//
//                    contentsItemView.setOnFocusChangeListener(onFocusChangeListener);                   // part12 : Focus를 등록 Focus를 기준으로 contentsItemView생성 (16'10")
//                }
//                break;
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    String path = data.getStringExtra(INTENT_PATH);
//                    pathList.set(parent.indexOfChild((View) selectedImageVIew.getParent()) - 1, path);
//                    Glide.with(this).load(path).override(1000).into(selectedImageVIew);
//                }
//                break;
//        }


        Log.d("태그", "11");
        List<String> photos = null;
        Log.d("태그", "111");
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Log.d("태그", "1111");
            if (data != null) {
                Log.d("태그", "11111");
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            if (photos != null) {
                Log.d("태그", "----111111");
                selectedPhotos.addAll(photos);
                Log.d("태그", "----111111"+selectedPhotos);
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    storageUpload();
                    break;
                case R.id.image:
                    //myStartActivity(GalleryActivity.class, GALLERY_IMAGE, 0);               // part12 : 실행중인 Activity의 request 값 다르게 설정 (13'41")
                    Log.d("태구", "1");
                    YPhotoPickerIntent intent = new YPhotoPickerIntent(getActivity());
                    Log.d("태구", "2");
                    intent.setMaxSelectCount(20);
                    Log.d("태구", "3");
                    intent.setShowCamera(true);
                    Log.d("태구", "4");
                    intent.setShowGif(true);
                    Log.d("태구", "5");
                    intent.setSelectCheckBox(false);
                    Log.d("태구", "6");
                    intent.setMaxGrideItemCount(3);
                    Log.d("태구", "7");
                    startActivityForResult(intent, REQUEST_CODE);
                    Log.d("태구", "8");
                    break;
                    /*
                case R.id.video:
                    ////myStartActivity(GalleryActivity.class, GALLERY_VIDEO, 0);               // part12 : 실행중인 Activity의 request 값 다르게 설정 (13'41")
                    break;
                     */
                case R.id.buttonsBackgroundLayout:
                    if (buttonsBackgroundLayout.getVisibility() == View.VISIBLE) {
                        buttonsBackgroundLayout.setVisibility(View.GONE);                               // part12 : 실행되고나면 사라지게 설정 (15'19")
                    }
                    break;
                case R.id.imageModify:
                    myStartActivity(GalleryActivity.class, GALLERY_IMAGE, 1);               // part12 : 실행중인 Activity의 request 값 다르게 설정 (13'41")
                    buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
                    /*
                case R.id.videoModify:
                    ////myStartActivity(GalleryActivity.class, GALLERY_VIDEO, 1);               // part12 : 실행중인 Activity의 request 값 다르게 설정 (13'41")
                    buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
                     */
                case R.id.delete:                                                                       // part12 : 작성중인 게시물에서 사진 빼기 (12'30")

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
        final String title = ((EditText) getView().findViewById(R.id.titleEditText)).getText().toString();
        if (title.length() > 0) {
            String postID = null;
            loaderLayout.setVisibility(View.VISIBLE);                                                   // part13 : 로딩 화면 (2')
            final ArrayList<String> contentsList = new ArrayList<>();                                   // part11 : contentsList에는 컨텐츠 내용이
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();                                    // part12 :
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            ///
            postID = firebaseFirestore.collection("posts").document().getId();
            final DocumentReference documentReference =  postModel == null ? firebaseFirestore.collection("posts").document(postID) : firebaseFirestore.collection("posts").document(postModel.getPostModel_Post_Uid());     //postInfo가 null이면 그냥 추가 되고 아니면 해당 아게시물 아이디에 해당하는 것으로 추가
            final Date date = postModel == null ? new Date() : postModel.getPostModel_DateOfManufacture();          // part17 : null이면 = 새 날짜 / 아니면 = getCreatedAt 날짜 이거 해줘야 수정한게 제일 위로 가지 않음 ((31')
            Log.d("로그","111");
            for (int i = 0; i < selectedPhotos.size(); i++) {                                              // part11 : 안의 자식뷰만큼 반복 (21'15")

                                                            // part11 : 자식뷰 저장
                                                    // part11 : EditText가 아닐 때 ---> 이미지 뷰일때는
                        String path = selectedPhotos.get(pathCount);
                Log.d("태그3","size = "+selectedPhotos.size());
                Log.d("태그3","path = "+path);
                        contentsList.add(path);

                        String[] pathArray = path.split("\\.");                                         // part14 : 이미지의 확장자를 주어진대로 (2'40")
                        final StorageReference mountainImagesRef = storageRef.child("posts/" + documentReference.getId() + "/" + pathCount + "." + pathArray[pathArray.length - 1]);
                        try {
                            Log.d("태그3","pathCount = "+pathCount);
                            InputStream stream = new FileInputStream(new File(selectedPhotos.get(pathCount)));            // part11 : 경로 설정 (27'20")
                            Log.d("태그3","selectedPhotos.get(pathCount) = "+selectedPhotos.get(pathCount));
                            StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                            Log.d("태그3","metadata = "+metadata);
                            UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                            Log.d("태그3","uploadTask = "+uploadTask);
                            ///
                            final String newPostID = postID;
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
                                                                                         // part11 : SUCCEESSCOUNT 개의 사진 (37')
                                            contentsList.set(index, uri.toString());                        // part11 : 인덱스를 받아서 URi저장 ( 36'40")

                                                Log.d("태그3","1");
                                                PostModel postModel = new PostModel(title, contentsList,  date, currentUser.getUid(), newPostID);
                                                ///
                                                postModel.setPostModel_Post_Uid(newPostID);
                                                Log.d("태그3","ㄴ");

                                                storeUpload(documentReference, postModel);

                                        }
                                    });
                                }
                            });
                        } catch (FileNotFoundException e) {
                            Log.e("로그", "에러: " + e.toString());
                        }
                        pathCount++;


            }
        } else {
            Toast.makeText(getActivity(), "제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    private void storeUpload(DocumentReference documentReference, final PostModel postModel) {
        Log.d("로그","1234");

        documentReference.set(postModel.getPostInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        loaderLayout.setVisibility(View.GONE);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("postinfo", postModel);                                    // part19 : 수정 후 수정된 정보 즉시 반영 (80')
                        Log.d("로그","11");
                        getActivity().setResult(RESULT_OK, resultIntent);
                        Log.d("로그","12");
                        //**
                        Intent intentpage = new Intent(getActivity(), MainActivity.class);
                        startActivity(intentpage);
                        //getActivity().finish();
                        Log.d("로그","13");
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

//    private void postInit() {                                                                               // part17 : (33')
//        if (postModel != null) {                                                                             //수정 버튼을 눌러서 들어왔을 때 null이 아니면 == 나 수정 하러 왔음 >> 화면에는 수정하고자하는 게시물의 정보들이 띄워져있음
//            titleEditText.setText(postModel.getPostModel_Title());
//            ArrayList<String> contentsList = postModel.getPostModel_ImageList();
//            for (int i = 0; i < contentsList.size(); i++) {
//                String contents = contentsList.get(i);
//                if (isStorageUrl(contents)) {
//                    pathList.add(contents);
//                    ////
//                    ContentsItemView contentsItemView = new ContentsItemView(getActivity());
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
//        }
//    }

    private void myStartActivity(Class c, int media, int requestCode) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra(INTENT_MEDIA, media);
        startActivityForResult(intent, requestCode);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.d("태그", "11");
//        List<String> photos = null;
//        Log.d("태그", "111");
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//            Log.d("태그", "1111");
//            if (data != null) {
//                Log.d("태그", "11111");
//                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
//            }
//            if (photos != null) {
//                Log.d("태그", "111111");
//                selectedPhotos.addAll(photos);
//            }
//
//            Log.d("태그", "1111111");
//            // start image viewr
//            Intent startActivity = new Intent(getActivity() , PhotoPagerActivity.class);
//            Log.d("태그", "11111111");
//            startActivity.putStringArrayListExtra("photos" , selectedPhotos);
//            Log.d("태그", "111111111");
//            startActivity(startActivity);
//            Log.d("태그", "1111111111");
//        }
//    }

}
