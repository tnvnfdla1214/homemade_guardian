package com.example.homemade_guardian_beta.Main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.homemade_guardian_beta.Main.bottombar.MyInfoFragment;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateInfoActivity extends BasicActivity {
    ImageView Myinfo_profileImage;
    UserModel userModel = new UserModel();
    TextView  Myinfo_profileUniversity;
    EditText Myinfo_profileNickName;
    Button Update_Info_Buutton;
    MyInfoFragment myInfoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateinfo);

        Update_Info_Buutton = findViewById(R.id.Update_Info_Buutton);
        Myinfo_profileNickName = findViewById(R.id.Myinfo_profileNickName);
        Myinfo_profileUniversity = findViewById(R.id.Myinfo_profileUniversity);
        Myinfo_profileImage = findViewById(R.id.Myinfo_profileImage);

        userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Myinfo_profileNickName.setText(userModel.getUserModel_NickName());
        if(userModel.getUserModel_University() == 0){
            Myinfo_profileUniversity.setText("홍익대학교");
        }
        else {
            Myinfo_profileUniversity.setText("고려대학교");
        }
        if(userModel.getUserModel_ProfileImage() != null){
            //Glide.with(getContext()).load(Usermodel.getUserModel_ProfileImage()).centerCrop().into(Myinfo_profileImage);
        }
        else{
            //Glide.with(getContext()).load(R.drawable.none_profile_user).centerCrop().into(Myinfo_profileImage);
        }

        Update_Info_Buutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore Firebasefirestore = FirebaseFirestore.getInstance();
                final DocumentReference docRef_USERS_UsersUid = Firebasefirestore.collection("USERS").document(userModel.getUserModel_Uid());
                userModel.setUserModel_NickName(Myinfo_profileNickName.getText().toString());
                docRef_USERS_UsersUid.set(userModel.getUserInfo())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Intent Resultintent = new Intent();
                                Resultintent.putExtra("userModel", userModel);                                    // part19 : 수정 후 수정된 정보 즉시 반영 (80')
                                setResult(Activity.RESULT_OK, Resultintent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {// part20 : (37'30") 보면 로딩 뒤에 있는 이미지 클릭시 이ㅏ벤트가 진행되버리는 현상을 방지
                            }
                        });
            }
        });
    }


}
