package com.example.homemade_guardian_beta.post.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

// 사용자의 정보를 보여주는 액티비티이다.
//      Ex) 현재는 PostActivity에서 프로필 이미지를 눌렀을 때 사용되고 있지만, 내정보Activity를 구현하는 데에도 사용될 것이다.

public class HostModelActivity extends BasicActivity {
    private UserModel userModel;                //UserModel 참조 선언

    private TextView Adress;                    //사용자의 주소
    private TextView BirthDay;                  //사용자의 생일
    private TextView Name;                      //사용자의 이름
    private TextView PhoneNumber;               //사용자의 전화번호
    private TextView Usernm;                    //사용지의 닉네임
    private ImageView ProfileImageView;         //사용자의 이미지

    private FirebaseFirestore Firebasefirestore =null;   //파이어스토어에 접근하기 위한 선언
    private StorageReference Storagereference;  //파이어스토리지에 접근하기 위한 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_info);
        setToolbarTitle("호스트용 회원정보");

        Adress = (TextView)findViewById(R.id.adress);
        BirthDay = (TextView)findViewById(R.id.birthDay);
        Name = (TextView)findViewById(R.id.name);
        PhoneNumber = (TextView)findViewById(R.id.phoneNumber);
        Usernm = (TextView)findViewById(R.id.usernm);
        ProfileImageView = (ImageView) findViewById(R.id.profileImageView);

        String CurrentUid = getIntent().getStringExtra("toUid");    //postActivity에서 정보를 받아옴

        Firebasefirestore = FirebaseFirestore.getInstance();
        Storagereference = FirebaseStorage.getInstance().getReference();

        DocumentReference docRef_USERS_CurrentUid = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUid);
        docRef_USERS_CurrentUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                Adress.setText(userModel.getAddress());
                BirthDay.setText(userModel.getBirthDay());
                Name.setText(userModel.getName());
                PhoneNumber.setText(userModel.getPhoneNumber());
                Usernm.setText(userModel.getUsernm());
                if(userModel.getphotoUrl() != null){
                    Glide.with(HostModelActivity.this).load(userModel.getphotoUrl()).centerCrop().override(500).into(ProfileImageView);
                }
                else{
                    Glide.with(getApplicationContext()).load(R.drawable.user).into(ProfileImageView);
                }

            }
        });

    }

}