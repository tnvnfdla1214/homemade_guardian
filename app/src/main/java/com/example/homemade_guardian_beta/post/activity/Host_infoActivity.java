package com.example.homemade_guardian_beta.post.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.fragment.ChatFragment;
import com.example.homemade_guardian_beta.chat.model.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static com.example.homemade_guardian_beta.post.PostUtil.INTENT_PATH;
import static com.example.homemade_guardian_beta.post.PostUtil.showToast;

public class Host_infoActivity extends BasicActivity {
    private static final String TAG = "Host_infoActivity";
    private FirebaseFirestore firestore=null;
    private StorageReference storageReference;
    private UserModel userModel;
    private TextView adress;
    private TextView birthDay;
    private TextView name;
    private TextView phoneNumber;
    private TextView usernm;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_info);
        setToolbarTitle("호스트용 회원정보");

        adress = (TextView)findViewById(R.id.adress);
        birthDay = (TextView)findViewById(R.id.birthDay);
        name = (TextView)findViewById(R.id.name);
        phoneNumber = (TextView)findViewById(R.id.phoneNumber);
        usernm = (TextView)findViewById(R.id.usernm);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);

        //postActivity에서 받아옴
        String toUid = getIntent().getStringExtra("toUid");

        firestore = FirebaseFirestore.getInstance();
        storageReference  = FirebaseStorage.getInstance().getReference();


        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(toUid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);

                adress.setText(userModel.getAddress());
                birthDay.setText(userModel.getBirthDay());
                name.setText(userModel.getName());
                phoneNumber.setText(userModel.getPhoneNumber());
                usernm.setText(userModel.getUsernm());
                if(userModel.getphotoUrl() != null){
                    Glide.with(Host_infoActivity.this).load(userModel.getphotoUrl()).centerCrop().override(500).into(profileImageView);
                }
                else{
                    Glide.with(getApplicationContext()).load(R.drawable.user).into(profileImageView);
                }

            }
        });

    }

}