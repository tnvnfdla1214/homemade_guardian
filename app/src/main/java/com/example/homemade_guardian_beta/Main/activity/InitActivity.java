package com.example.homemade_guardian_beta.Main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class InitActivity extends AppCompatActivity {

    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        mContext =this;

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
                                Log.d("test1","InitActivity : "+userModel);
                                Log.d("test1","InitActivity : "+userModel.getUserModel_NickName());
                                if(userModel.getUserModel_UnReViewUserList().size()>0){
                                    //ReviewActivity reviewActivity = new ReviewActivity(InitActivity.this);
                                    //reviewActivity.callFunction(userModel.getUserModel_UnReViewUserList().get(0), userModel.getUserModel_UnReViewPostList().get(0),userModel);
                                }
                                else{
                                    Log.d("test1","InitActivity 아래 : "+userModel);
                                    Log.d("test1","InitActivity 아래 : "+userModel.getUserModel_NickName());
                                    setResult(Activity.RESULT_OK, intent);
                                    startActivityForResult(intent, 0);
                                    finish();
                                }

                            }else {
                                myStartActivity(MemberInitActivity.class);
                            }
                        }
                    }
                }
            });
        }

    }

    public void myStartActivity(Class c) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
        finish();
    }
    public void myStartFinishActivity(Class c, UserModel userModel) {                                                             // part22 : c에다가 이동하려는 클래스를 받고 requestcode는 둘다 1로 준다.
        Intent intent = new Intent(this, c);
        intent.putExtra("userModel", userModel);
        startActivityForResult(intent, 1);
        finish();

    }

}
