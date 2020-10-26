package com.example.homemade_guardian_beta.Main.bottombar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import com.example.homemade_guardian_beta.Main.activity.MyInfoPostActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.user.UserModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyInfoFragment extends Fragment {
    ImageView Myinfo_profileImage;
    TextView Myinfo_profileNickName, Myinfo_profileUniversity;
    Button Proceeding_Post,Deal_Complete_Post,My_Writen_Post;
    TextView Kind_Count,Complete_Count,Correct_Count,Bad_Count;
    Button My_Reviews_written,To_Reviews_written;

    private String CurrentUid;
    private FirebaseUser CurrentUser;

    UserModel Usermodel;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);
        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("");
        }
        Myinfo_profileImage = (ImageView) view.findViewById(R.id.Myinfo_profileImage);
        Myinfo_profileNickName = (TextView) view.findViewById(R.id.Myinfo_profileNickName);
        Myinfo_profileUniversity = (TextView) view.findViewById(R.id.Myinfo_profileUniversity);

        Proceeding_Post = (Button) view.findViewById(R.id.Proceeding_Post);
        Proceeding_Post.setOnClickListener(onClickListener);
        Deal_Complete_Post = (Button) view.findViewById(R.id.Deal_Complete_Post);
        Deal_Complete_Post.setOnClickListener(onClickListener);
        My_Writen_Post = (Button) view.findViewById(R.id.My_Writen_Post);
        My_Writen_Post.setOnClickListener(onClickListener);

        Kind_Count = (TextView) view.findViewById(R.id.Kind_Count);
        Complete_Count = (TextView) view.findViewById(R.id.Complete_Count);
        Correct_Count = (TextView) view.findViewById(R.id.Correct_Count);
        Bad_Count = (TextView) view.findViewById(R.id.Bad_Count);

        My_Reviews_written = (Button) view.findViewById(R.id.My_Reviews_written);
        My_Reviews_written.setOnClickListener(onClickListener);
        To_Reviews_written = (Button) view.findViewById(R.id.To_Reviews_written);
        To_Reviews_written.setOnClickListener(onClickListener);

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentUid =CurrentUser.getUid();

        Profile_Info();
        Review_Count_Info();




        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Deal_Complete_Post:
                    myStartActivity(MyInfoPostActivity.class,"0");
                    break;
                case R.id.My_Reviews_written:
                    myStartActivity(MyInfoPostActivity.class,"1");
                    break;
                case R.id.My_Writen_Post:
                    myStartActivity(MyInfoPostActivity.class,"2");
                    break;
                case R.id.Proceeding_Post:
                    myStartActivity(MyInfoPostActivity.class,"3");
                    break;
                case R.id.To_Reviews_written:
                    myStartActivity(MyInfoPostActivity.class,"4");
                    break;


            }
        }
    };

    public void Profile_Info(){
        DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUid);
        docRef_MARKETS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Usermodel = documentSnapshot.toObject(UserModel.class);

                Myinfo_profileNickName.setText(Usermodel.getUserModel_NickName());
                //Myinfo_profileUniversity.setText(Usermodel.getUserModel_University());
                if(Usermodel.getUserModel_University() == 0){
                    Myinfo_profileUniversity.setText("홍익대학교");
                }
                else {
                    Myinfo_profileUniversity.setText("고려대학교");
                }
                //post의 이미지 섬네일 띄우기
                if(Usermodel.getUserModel_ProfileImage() != null){
                    Glide.with(getContext()).load(Usermodel.getUserModel_ProfileImage()).centerInside().into(Myinfo_profileImage);
                }
                else{
                    Glide.with(getContext()).load(R.drawable.none_profile_user).centerCrop().into(Myinfo_profileImage);
                }
            }
        });

    }
    public void Review_Count_Info(){


        DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUid);
        docRef_MARKETS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usermodel = documentSnapshot.toObject(UserModel.class);

                Kind_Count.setText("X"+Usermodel.getUserModel_kindReviewList().size());
                Complete_Count.setText("X"+Usermodel.getUserModel_completeReviewList().size());
                Correct_Count.setText("X"+Usermodel.getUserModel_correctReviewList().size());
                Bad_Count.setText("X"+Usermodel.getUserModel_badReviewList().size());

            }
        });

    }

    private void myStartActivity(Class c,String Info) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("Info",Info);
        startActivityForResult(intent, 0);
    }


}