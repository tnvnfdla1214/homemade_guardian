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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_info);
        setToolbarTitle("호스트용 회원정보");

        //TextView adress = (TextView)findViewById(R.id.adress);
        TextView birthDay = (TextView)findViewById(R.id.birthDay);
        TextView name = (TextView)findViewById(R.id.name);
        TextView phoneNumber = (TextView)findViewById(R.id.phoneNumber);
        TextView usernm = (TextView)findViewById(R.id.usernm);

        String toUid = getIntent().getStringExtra("toUid");

        firestore = FirebaseFirestore.getInstance();
        storageReference  = FirebaseStorage.getInstance().getReference();

        firestore.collection("users").document(toUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //바깥에서 선언된것들은 여기서는 안먹힘 이유는 모르겠음
                        //이거 만드는게 뭔가 어려웠던게 구글링한것들이 다 코틀린 형식이라 읽을 수 가 없음

                        //Log.d("태그", "DocumentSnapshot data: " + document.getData());
                        //Log.d("태그", "d : " + document.getData().get("address").toString());
                        TextView adress = (TextView)findViewById(R.id.adress);
                        adress.setText(document.getData().get("address").toString());

                        TextView birthDay = (TextView)findViewById(R.id.birthDay);
                        birthDay.setText(document.getData().get("birthDay").toString());

                        TextView name = (TextView)findViewById(R.id.name);
                        name.setText(document.getData().get("name").toString());

                        TextView phoneNumber = (TextView)findViewById(R.id.phoneNumber);
                        phoneNumber.setText(document.getData().get("phoneNumber").toString());

                        TextView usernm = (TextView)findViewById(R.id.usernm);
                        usernm.setText(document.getData().get("usernm").toString());

                    } else {
                        //Log.d("태그", "No such document");
                    }
                } else {
                    //Log.d("태그", "get failed with ", task.getException());
                }
            }
        });

    }

}