package com.example.homemade_guardian_beta.Main.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.Main.bottombar.MyInfoFragment;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.market.activity.GalleryActivity;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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

import static com.example.homemade_guardian_beta.Main.common.Util.INTENT_PATH;
import static com.example.homemade_guardian_beta.Main.common.Util.showMarketToast;
import static com.example.homemade_guardian_beta.Main.common.Util.showToast;
import static com.example.homemade_guardian_beta.Main.common.Util.storageUrlToName;

public class UpdateInfoActivity extends BasicActivity {
    ImageView Myinfo_profileImage;
    UserModel userModel = new UserModel();
    Spinner Myinfo_profileUniversity;
    EditText Myinfo_profileNickName;
    MyInfoFragment myInfoFragment;
    private int UserModel_University;
    DatePicker BirthDay_Picker;
    private String BirthDay;
    ConstraintLayout Myinfo_profileImage_layout;
    private String SelectedImagePath;
    private RelativeLayout LoaderLayout;
    private ImageView back_Button;
    private Button UpdateInfo_Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateinfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        Myinfo_profileNickName = findViewById(R.id.Myinfo_profileNickName);
        Myinfo_profileUniversity = findViewById(R.id.Myinfo_profileUniversity);
        Myinfo_profileImage = findViewById(R.id.Myinfo_profileImage);
        BirthDay_Picker = (DatePicker)findViewById(R.id.BirthDay_Picker);
        Myinfo_profileImage_layout = findViewById(R.id.Myinfo_profileImage_layout);
        LoaderLayout = findViewById(R.id.Loader_Lyaout);
        back_Button = findViewById(R.id.back_Button);
        UpdateInfo_Button = findViewById(R.id.UpdateInfo_Button);
        UpdateInfo_Button.setOnClickListener(onClickListener);
        back_Button.setOnClickListener(onClickListener);
        Myinfo_profileImage_layout.setOnClickListener(onClickListener);

        userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        ShowInfo();


    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.UpdateInfo_Button:
                    MemberInit_Storage_Uploader();
                    break;
                case R.id.Myinfo_profileImage_layout:
                    myStartActivity(GalleryActivity.class);                                // part8 : 처음에는 안보이다가 이미지그림 누르면 나타나게함 (11'30")
                    break;
                case R.id.back_Button:
                    finish();
                    break;
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    SelectedImagePath = data.getStringExtra(INTENT_PATH);
                    if(SelectedImagePath != null){
                        Glide.with(this).load(SelectedImagePath).centerCrop().override(500).into(Myinfo_profileImage);
                    } else{
                        Glide.with(this).load(R.drawable.none_profile_user).centerCrop().into(Myinfo_profileImage);
                    }
                }
                if(resultCode == Activity.RESULT_CANCELED){
                    SelectedImagePath = null;
                    Glide.with(this).load(R.drawable.none_profile_user).centerCrop().into(Myinfo_profileImage);
                }
                break;
            }
        }
    }

    public void ShowInfo(){
        Myinfo_profileNickName.setText(userModel.getUserModel_NickName());

        BirthDay = userModel.getUserModel_BirthDay();
        if(BirthDay != null){
            String[] array = BirthDay.split("/");
            BirthDay_Picker.init( Integer.parseInt(array[0]),  Integer.parseInt(array[1])-1, Integer.parseInt(array[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    BirthDay = year + "/" + (monthOfYear+1) + "/" + dayOfMonth;
                }
            });
        }else {
            BirthDay_Picker.init(2020, 0, 1, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    BirthDay = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                }
            });
        }

        Myinfo_profileUniversity = findViewById(R.id.Myinfo_profileUniversity);

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.University, android.R.layout.simple_spinner_dropdown_item);
        //android.R.layout.simple_spinner_dropdown_item은 기본으로 제공해주는 형식입니다.
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Myinfo_profileUniversity.setAdapter(monthAdapter); //어댑터에 연결해줍니다.
        Myinfo_profileUniversity.setSelection(userModel.getUserModel_University());
        Myinfo_profileUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserModel_University = position;
            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.
            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        if(userModel.getUserModel_ProfileImage() != null){
            Glide.with(this).load(userModel.getUserModel_ProfileImage()).centerCrop().into(Myinfo_profileImage);
        }
        else{
            Glide.with(this).load(R.drawable.none_profile_user).centerCrop().into(Myinfo_profileImage);
        }
    }


    private void MemberInit_Storage_Uploader() {                                                                            // part5 : 회원정보 업로드 로직 (3')
        final String UserModel_Nickname = Myinfo_profileNickName.getText().toString();


        if (UserModel_Nickname.length() < 20) {

            LoaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage Firebasestorage = FirebaseStorage.getInstance();
            StorageReference Storagereference = Firebasestorage.getReference();

            //스토리지의 USER/유저의 UID/이미지 들어가는곳  에다가 넣는다.
            final StorageReference ImageRef_USERS_Uid = Storagereference.child("USERS/" + userModel.getUserModel_Uid() + "/USERSImage.jpg");


            if (SelectedImagePath == null) {                                                                      // part5 : 데이터 추가 (9'10")
                if(UserModel_Nickname.equals("")){
                    userModel.setUserModel_NickName(extractIDFromEmail(userModel.getUserModel_ID()));
                }else{
                    userModel.setUserModel_NickName(Myinfo_profileNickName.getText().toString());
                }
                userModel.setUserModel_BirthDay(BirthDay);
                userModel.setUserModel_University(UserModel_University);
                userModel.setUserModel_ProfileImage(null);
                StorageReference desertRef_USERS_Profile = Storagereference.child("USERS/" +  userModel.getUserModel_Uid()  + "/USERSImage.jpg");    // part17: (((파이어베이스에서 삭제))) 파이에베이스 스토리지는 폴더가 없다, 하나하나가 객체로서 저장 (13'30")
                desertRef_USERS_Profile.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) { }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) { }
                });
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
                                if(UserModel_Nickname.equals("")){
                                    userModel.setUserModel_NickName(extractIDFromEmail(userModel.getUserModel_ID()));
                                }else{
                                    userModel.setUserModel_NickName(Myinfo_profileNickName.getText().toString());
                                }
                                userModel.setUserModel_BirthDay(BirthDay);
                                userModel.setUserModel_University(UserModel_University);
                                userModel.setUserModel_ProfileImage(DownloadUri.toString());
                                MemberInit_Store_Uploader(userModel);
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그", "에러: " + e.toString());
                }
            }
        }else {
            showToast(UpdateInfoActivity.this, "닉네임은 최대 20자까지 가능합나다.");
        }
    }
    private void MemberInit_Store_Uploader(final UserModel Usermodel) {                                                     // part5 : DB에 등록이 됬는지 알려주는 로직
        FirebaseFirestore docSet_USERS_Uid = FirebaseFirestore.getInstance();
        docSet_USERS_Uid.collection("USERS").document(userModel.getUserModel_Uid()).set(Usermodel.getUserInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(UpdateInfoActivity.this, "회원정보 수정을 성공하였습니다.");
                        LoaderLayout.setVisibility(View.GONE);
                        Intent Resultintent = new Intent();
                        Resultintent.putExtra("userModel", userModel);                                    // part19 : 수정 후 수정된 정보 즉시 반영 (80')
                        setResult(Activity.RESULT_OK, Resultintent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(UpdateInfoActivity.this, "회원정보 수정에 실패하였습니다.");
                        LoaderLayout.setVisibility(View.GONE);
                    }
                });
    }
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
