package com.example.homemade_guardian_beta.Main.bottombar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.photo.activity.PhotoPickerActivity;
import com.example.homemade_guardian_beta.photo.PhotoUtil;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.post.PostModel;
import com.example.homemade_guardian_beta.post.activity.GalleryActivity;
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
import com.google.protobuf.StringValue;

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

public class WritePostFragment extends Fragment {
    private FirebaseUser currentUser;
    private StorageReference storageRef;
    private RelativeLayout buttonsBackgroundLayout;
    private RelativeLayout loaderLayout;
    private EditText selectedEditText;
    private EditText titleEditText;
    private PostModel postModel;
    private int pathCount;
    public final static int REQUEST_CODE = 1;
    public  ArrayList<String> selectedPhotos = new ArrayList<>();
////////////////////////
    private ImageView Selected_ImageView;
    private Button Select_Post_Image_Button;
    private ArrayList<String> ImageList;            //게시물의 이미지 리스트
    private ViewPager Viewpager;                    //이미지들을 보여주기 위한 ViewPager 선언


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_writepost, container, false);
        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("게시글 작성");
        }

        buttonsBackgroundLayout = view.findViewById(R.id.ButtonsBackground_Layout);
        loaderLayout = view.findViewById(R.id.Loader_Lyaout);
        titleEditText = view.findViewById(R.id.Post_Title_EditText);
        selectedEditText = view.findViewById(R.id.contentsEditText);
        //////////////////
        //Selected_ImageView = view.findViewById(R.id.Selected_ImageView);
        Select_Post_Image_Button = view.findViewById(R.id.Select_Post_Image_Button);

        view.findViewById(R.id.Post_Write_Button).setOnClickListener(onClickListener);
        view.findViewById(R.id.Select_Post_Image_Button).setOnClickListener(onClickListener);
        view.findViewById(R.id.imageModify).setOnClickListener(onClickListener);
        view.findViewById(R.id.Comment_Delete_Button).setOnClickListener(onClickListener);
        ////////
        Viewpager = view.findViewById(R.id.ViewPager);

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
        postModel = (PostModel) getActivity().getIntent().getSerializableExtra("postInfo");                       // part17 : postInfo의 정체!!!!!!!!!!!!!!!!!!(29')
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
                    Viewpager.setAdapter(new ViewPagerAdapter(getContext(), ImageList));
                }
                Select_Post_Image_Button.setText(Html.fromHtml(selectedPhotos.size()+"/20"+"<br/>"+"클릭시 이미지 재선택"));

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
                    intent.setMaxSelectCount(20);
                    intent.setShowCamera(true);
                    intent.setShowGif(true);
                    intent.setSelectCheckBox(false);
                    intent.setMaxGrideItemCount(3);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.ButtonsBackground_Layout:
                    if (buttonsBackgroundLayout.getVisibility() == View.VISIBLE) {
                        buttonsBackgroundLayout.setVisibility(View.GONE);                               // part12 : 실행되고나면 사라지게 설정 (15'19")
                    }
                    break;
                case R.id.imageModify:
                    myStartActivity(GalleryActivity.class, GALLERY_IMAGE, 1);               // part12 : 실행중인 Activity의 request 값 다르게 설정 (13'41")
                    buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
                case R.id.Comment_Delete_Button:                                                                       // part12 : 작성중인 게시물에서 사진 빼기 (12'30")
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
        final String title = ((EditText) getView().findViewById(R.id.Post_Title_EditText)).getText().toString();
        final String textcontents = ((EditText) getView().findViewById(R.id.contentsEditText)).getText().toString();
        if (title.length() > 0) {
            String postID = null;
            loaderLayout.setVisibility(View.VISIBLE);                                                   // part13 : 로딩 화면 (2')
            final ArrayList<String> contentsList = new ArrayList<>();                                   // part11 : contentsList에는 컨텐츠 내용이
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();                                    // part12 :
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            ///
            postID = firebaseFirestore.collection("POSTS").document().getId();
            final DocumentReference documentReference =  postModel == null ? firebaseFirestore.collection("POSTS").document(postID) : firebaseFirestore.collection("POSTS").document(postModel.getPostModel_Post_Uid());     //postInfo가 null이면 그냥 추가 되고 아니면 해당 아게시물 아이디에 해당하는 것으로 추가
            final Date date = postModel == null ? new Date() : postModel.getPostModel_DateOfManufacture();          // part17 : null이면 = 새 날짜 / 아니면 = getCreatedAt 날짜 이거 해줘야 수정한게 제일 위로 가지 않음 ((31')
            Log.d("로그","111");
            for (int i = 0; i < selectedPhotos.size(); i++) {                                              // part11 : 안의 자식뷰만큼 반복 (21'15")
                String path = selectedPhotos.get(pathCount);
                contentsList.add(path);

                String[] pathArray = path.split("\\.");                                         // part14 : 이미지의 확장자를 주어진대로 (2'40")
                final StorageReference mountainImagesRef = storageRef.child("POSTS/" + documentReference.getId() + "/" + pathCount + "." + pathArray[pathArray.length - 1]);
                try {
                    InputStream stream = new FileInputStream(new File(selectedPhotos.get(pathCount)));            // part11 : 경로 설정 (27'20")
                    StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                    UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
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
                                            contentsList.set(index, uri.toString());                        // part11 : 인덱스를 받아서 URi저장 ( 36'40")
                                            PostModel postModel = new PostModel(title, textcontents, contentsList,  date, currentUser.getUid(), newPostID);
                                            postModel.setPostModel_Post_Uid(newPostID);
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
            if (selectedPhotos.size() == 0) {
                PostModel postModel = new PostModel(title, textcontents, date, currentUser.getUid(), postID);
                postModel.setPostModel_Post_Uid(postID);
                storeUpload(documentReference,postModel);
            }
        } else {
            Toast.makeText(getActivity(), "제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    private void storeUpload(DocumentReference documentReference, final PostModel postModel) {
        documentReference.set(postModel.getPostInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loaderLayout.setVisibility(View.GONE);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("postinfo", postModel);                                    // part19 : 수정 후 수정된 정보 즉시 반영 (80')
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
}