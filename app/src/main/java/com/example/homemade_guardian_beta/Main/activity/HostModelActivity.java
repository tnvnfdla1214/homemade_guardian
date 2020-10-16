package com.example.homemade_guardian_beta.Main.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.example.homemade_guardian_beta.post.activity.BasicActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

// 사용자의 정보를 보여주는 액티비티이다.
//      Ex) 현재는 PostActivity에서 프로필 이미지를 눌렀을 때 사용되고 있지만, 내정보Activity를 구현하는 데에도 사용될 것이다.

public class HostModelActivity extends BasicActivity {
    private UserModel Usermodel;                //UserModel 참조 선언

    private TextView Users_Address_TextView;                    //사용자의 주소
    private TextView Users_BirthDay_TextView;                  //사용자의 생일
    private TextView Users_Name_TextView;                      //사용자의 이름
    private TextView Users_PhoneNumber_TextView;               //사용자의 전화번호
    private TextView Users_Usernm_TextView;                    //사용지의 닉네임
    private ImageView Users_Profile_ImageView;         //사용자의 이미지

    private FirebaseFirestore Firebasefirestore =null;   //파이어스토어에 접근하기 위한 선언
    private StorageReference Storagereference;  //파이어스토리지에 접근하기 위한 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_info);
        setToolbarTitle("호스트용 회원정보");

        Users_Address_TextView = (TextView)findViewById(R.id.Users_Address);
        Users_BirthDay_TextView = (TextView)findViewById(R.id.Users_BirthDay);
        Users_Name_TextView = (TextView)findViewById(R.id.Users_Name);
        Users_PhoneNumber_TextView = (TextView)findViewById(R.id.Users_PhoneNumber);
        Users_Usernm_TextView = (TextView)findViewById(R.id.Users_Usernm);
        Users_Profile_ImageView = (ImageView) findViewById(R.id.Users_Profile_ImageView);

        String CurrentUid = getIntent().getStringExtra("toUid");    //postActivity에서 정보를 받아옴

        Firebasefirestore = FirebaseFirestore.getInstance();
        Storagereference = FirebaseStorage.getInstance().getReference();

        DocRef_USERS_CurrentUid(CurrentUid);
    }

    //CurrentUid로 현재 사용자의 정보를 USERS 컬렉션에서 가져온다.
    public void DocRef_USERS_CurrentUid(String CurrentUid){
        DocumentReference docRef_USERS_CurrentUid = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUid);
        docRef_USERS_CurrentUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usermodel = documentSnapshot.toObject(UserModel.class);
                Users_Address_TextView.setText(Usermodel.getUserModel_University());
                Users_BirthDay_TextView.setText(Usermodel.getUserModel_BirthDay());
                Users_Name_TextView.setText(Usermodel.getUserModel_NickName());
                Users_Usernm_TextView.setText(Usermodel.getUserModel_NickName());
                if(Usermodel.getUserModel_ProfileImage() != null){ Glide.with(HostModelActivity.this).load(Usermodel.getUserModel_ProfileImage()).centerCrop().override(500).into(Users_Profile_ImageView); }
                else{ Glide.with(getApplicationContext()).load(R.drawable.user).into(Users_Profile_ImageView); }
            }
        });
    }
}