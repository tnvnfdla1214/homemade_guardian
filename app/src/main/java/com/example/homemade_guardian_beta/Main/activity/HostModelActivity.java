package com.example.homemade_guardian_beta.Main.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

// 사용자의 정보를 보여주는 액티비티이다.
//      Ex) 현재는 PostActivity에서 프로필 이미지를 눌렀을 때 사용되고 있지만, 내정보Activity를 구현하는 데에도 사용될 것이다.

public class HostModelActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_info);
        setToolbarTitle("작성자 회원정보");

       // 게시물 작성자의 Uid를 받아옴
        String CurrentUid = getIntent().getStringExtra("toUid");

       // CurrentUid로 현재 사용자의 정보를 가져온다.
        Get_USERS_Info(CurrentUid);
    }

   // CurrentUid로 현재 사용자의 정보를 가져온다.
    public void Get_USERS_Info(String CurrentUid){

        final TextView USERS_University_TextView = (TextView)findViewById(R.id.Users_Address);
        final TextView USERS_BirthDay_TextView = (TextView)findViewById(R.id.Users_BirthDay);
        final TextView USERS_Name_TextView = (TextView)findViewById(R.id.Users_Name);
        final TextView USERS_Usernm_TextView = (TextView)findViewById(R.id.Users_Usernm);
        final ImageView USERS_Profile_ImageView = (ImageView) findViewById(R.id.Users_Profile_ImageView);

        FirebaseFirestore Firebasefirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef_USERS_CurrentUid = Firebasefirestore.collection("USERS").document(CurrentUid);
        docRef_USERS_CurrentUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel Usermodel = documentSnapshot.toObject(UserModel.class);
//                if (Usermodel.getUserModel_University() != 0) {
//                    USERS_University_TextView.setText(Usermodel.getUserModel_University());
//                }
                USERS_BirthDay_TextView.setText(Usermodel.getUserModel_BirthDay());
                USERS_Name_TextView.setText(Usermodel.getUserModel_NickName());
                USERS_Usernm_TextView.setText(Usermodel.getUserModel_NickName());
                if(Usermodel.getUserModel_ProfileImage() != null){ Glide.with(HostModelActivity.this).load(Usermodel.getUserModel_ProfileImage()).centerCrop().override(500).into(USERS_Profile_ImageView); }
                else{ Glide.with(getApplicationContext()).load(R.drawable.none_profile_user).into(USERS_Profile_ImageView); }
            }
        });
    }
}