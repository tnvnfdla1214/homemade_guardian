package com.example.homemade_guardian_beta.Main.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.UserModel;
import com.example.homemade_guardian_beta.post.activity.BasicActivity;
import com.example.homemade_guardian_beta.post.activity.GalleryActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import static com.example.homemade_guardian_beta.post.PostUtil.INTENT_PATH;
import static com.example.homemade_guardian_beta.post.PostUtil.showToast;

//앱이 실행되고나면 로그인 후에 가장 먼저 보게 되는 액티비티로서, 사용자의 정보를 입력 받는다.
//      Ex) 메인프레그먼트에서는 이 MemberInitActivity가 실행되지 않으면, 계속 MemberInitActivity를 실행하게 된다.

public class MemberInitActivity extends BasicActivity {
    private String SelectedImagePath;                       //프로필 이미지로선택한 이미지
    
    private ImageView Profile_ImageView;                     //xml에서 선택한 이미지를 넣은 ImageView
    private RelativeLayout LoaderLayout;                     //로딩중을 나타내는 layout 선언
    private RelativeLayout ButtonBackgroundLayout;          //사진을 넣을 때 앨범으로 가기 위한 버튼을 생성해주는 layout
    
    private FirebaseUser CurrentUser=FirebaseAuth.getInstance().getCurrentUser();;                       //파이어베이스 데이터 상의 현재 사용자
    private String BirthDay;
    private Spinner University;
    private int UserModel_University;

    private EditText Nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_init);
        setToolbarTitle("프로필 입력");

        LoaderLayout = findViewById(R.id.Loader_Lyaout);
        Profile_ImageView = findViewById(R.id.Users_Profile_ImageView);
        ButtonBackgroundLayout = findViewById(R.id.ButtonsBackground_Layout);
        DatePicker BirthDay_Picker = (DatePicker)findViewById(R.id.BirthDay_Picker);
        Nickname = ((EditText) findViewById(R.id.Nickname));
        ButtonBackgroundLayout.setOnClickListener(onClickListener);
        Profile_ImageView.setOnClickListener(onClickListener);

        findViewById(R.id.Users_Info_Send_Button).setOnClickListener(onClickListener);
        findViewById(R.id.picture).setOnClickListener(onClickListener);
        findViewById(R.id.Photo_Directory_Button).setOnClickListener(onClickListener);

        Nickname.setHint(extractIDFromEmail(CurrentUser.getEmail()));

        BirthDay_Picker.init(2020, 1, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                BirthDay = year + "/" + monthOfYear + "/" + dayOfMonth;
            }
        });

        University = findViewById(R.id.University);

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.University, android.R.layout.simple_spinner_dropdown_item);
        //android.R.layout.simple_spinner_dropdown_item은 기본으로 제공해주는 형식입니다.
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        University.setAdapter(monthAdapter); //어댑터에 연결해줍니다.

        University.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserModel_University = position;
            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.
            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // 뒤로가기 이벤트
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    SelectedImagePath = data.getStringExtra(INTENT_PATH);
                    Glide.with(this).load(SelectedImagePath).centerCrop().override(500).into(Profile_ImageView);
                    ButtonBackgroundLayout.setVisibility(View.GONE);
                }
                break;
            }
        }
    }


    //사용하는 버튼들의 OnClickListener
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Users_Info_Send_Button:
                    MemberInit_Storage_Uploader();
                    break;
                case R.id.Users_Profile_ImageView:
                    ButtonBackgroundLayout.setVisibility(View.VISIBLE);                                 // part8 : 처음에는 안보이다가 이미지그림 누르면 나타나게함 (11'30")
                    break;
                case R.id.ButtonsBackground_Layout:                                                      // part20 : 다른데 누르면 buttonBackgroundLayout 사라지게 해줌 (48')
                    ButtonBackgroundLayout.setVisibility(View.GONE);
                    break;
                case R.id.picture:                                                                      // part7 : 프로필 사진 등록시 카메라 기능으로 사진을 찍을 시
                    //myStartActivity(CameraActivity.class);
                    break;
                case R.id.Photo_Directory_Button:                                                                      // part7 : 프로필 사진 등록시 앨범에서 고를 시
                    myStartActivity(GalleryActivity.class);
                    break;
            }
        }
    };

    //스토리지에 사진을 먼저 담는 함수
    private void MemberInit_Storage_Uploader() {                                                                            // part5 : 회원정보 업로드 로직 (3')
        final String UserModel_Nickname = Nickname.getText().toString();


        if (UserModel_Nickname.length() < 20) {

            LoaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage Firebasestorage = FirebaseStorage.getInstance();
            StorageReference Storagereference = Firebasestorage.getReference();
            CurrentUser = FirebaseAuth.getInstance().getCurrentUser();

            //스토리지의 USER/유저의 UID/이미지 들어가는곳  에다가 넣는다.
            final StorageReference ImageRef_USERS_Uid = Storagereference.child("USERS/" + CurrentUser.getUid() + "/USERSImage.jpg");
            final Date DateOfManufacture = new Date();                                                              // + : 사용자 리스트 수정 (현재 날짜 받아오기 [ 사진마다 달라서 그때 그댸 불르기])
            final ArrayList<String> UserModel_UnReViewList = new ArrayList<>();

            if (SelectedImagePath == null) {                                                                      // part5 : 데이터 추가 (9'10")
                UserModel userModel = new UserModel(UserModel_Nickname, BirthDay,DateOfManufacture,UserModel_University,UserModel_UnReViewList);          // + : 사용자 리스트 수정 (가입날짜 추가[사진 없는 버전])
                userModel.setUserModel_Uid(CurrentUser.getUid());
                userModel.setUserModel_ID(CurrentUser.getEmail());
                if(UserModel_Nickname.equals(null)){
                    userModel.setUserModel_NickName(extractIDFromEmail(CurrentUser.getEmail()));
                }
                MemberInit_Store_Uploader(userModel);
            } else {
                try {
                    InputStream Stream = new FileInputStream(new File(SelectedImagePath));                        // part7 : 입력한 회원정보를 스토리지에 저장 (25'20")
                    UploadTask Uploadtask = ImageRef_USERS_Uid.putStream(Stream);
                    Uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ImageRef_USERS_Uid.getDownloadUrl();                                  // part7 : ImageRef_USERS_Uid.getDownloadUrl()를 아래 task.getResult();에서 받아오는 것이 아닐까?
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri DownloadUri = task.getResult();                                         // part7 : 입력한 회원정보를 DB에 저장 (28')
                                UserModel Usermodel = new UserModel(UserModel_Nickname, BirthDay, DateOfManufacture, UserModel_University, DownloadUri.toString(),UserModel_UnReViewList);      // + : 사용자 리스트 수정 (가입날짜 추가)
                                Usermodel.setUserModel_Uid(CurrentUser.getUid());
                                Usermodel.setUserModel_ID(CurrentUser.getEmail());
                                if(UserModel_Nickname.equals(null)){
                                    Usermodel.setUserModel_NickName(extractIDFromEmail(CurrentUser.getEmail()));
                                }
                                MemberInit_Store_Uploader(Usermodel);
                            } else {
                                showToast(MemberInitActivity.this, "회원정보를 보내는데 실패하였습니다.");
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그", "에러: " + e.toString());
                }
            }
        } else {
            showToast(MemberInitActivity.this, "닉네임은 최대 20자까지 가능합나다.");
        }
    }

    //Usermodel에다 담은 회원정보를 USERS/CurrentUser의 Uid  에다가 넣는 함수
    private void MemberInit_Store_Uploader(UserModel Usermodel) {                                                     // part5 : DB에 등록이 됬는지 알려주는 로직
        FirebaseFirestore docSet_USERS_Uid = FirebaseFirestore.getInstance();
        docSet_USERS_Uid.collection("USERS").document(CurrentUser.getUid()).set(Usermodel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(MemberInitActivity.this, "회원정보 등록을 성공하였습니다.");
                        LoaderLayout.setVisibility(View.GONE);
                        finish();                                                                       // part5 : 정보 입력시 창이 나가지게 된다.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(MemberInitActivity.this, "회원정보 등록에 실패하였습니다.");
                        LoaderLayout.setVisibility(View.GONE);
                    }
                });
    }

    //Intent를 하기 위한 함수
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }

    // 이메일에서 @뒤로 잘라서 닉네임으로 이용한다.
    String extractIDFromEmail(String email){
        String[] parts = email.split("@");
        return parts[0];
    }
}