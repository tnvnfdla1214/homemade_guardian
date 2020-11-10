package com.example.homemade_guardian_beta.Main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class InitActivity extends AppCompatActivity {

    ArrayList<String> UnReViewUserList = new ArrayList<>();
    ArrayList<String> UnReViewMarketList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        final FirebaseUser currentUser_Uid = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser_Uid == null) {
            myStartActivity(LoginActivity.class);
        } else {
            final String CurruntUser_Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("USERS").document(CurruntUser_Uid);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {  //데이터의 존재여부
                                UserModel userModel = document.toObject(UserModel.class);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("userModel", userModel);
                                intent.putExtra("Review",0);
                                if(userModel.getUserModel_UnReViewUserList().size()>0){
                                    ReviewActivity reviewActivity = new ReviewActivity(InitActivity.this);
                                    reviewActivity.callFunction(userModel.getUserModel_UnReViewUserList().get(0), userModel.getUserModel_UnReViewPostList().get(0));
                                }
                                startActivity(intent);
                                finish();

                            }else {
                                myStartActivity(MemberInitActivity.class);
                            }
                        }
                    }
                }
            });
        }

    }

    private void myStartActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }
    private void myStartFinishActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
        finish();
    }

}
