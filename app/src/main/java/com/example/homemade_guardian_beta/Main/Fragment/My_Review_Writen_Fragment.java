package com.example.homemade_guardian_beta.Main.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemade_guardian_beta.Main.common.ReviewResultAdapter;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.market.adapter.SearchResultAdapter;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.model.user.ReviewModel;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.homemade_guardian_beta.Main.common.ReviewResultAdapter;

import java.util.ArrayList;
import java.util.Date;

//내가 작성한 리뷰
public class My_Review_Writen_Fragment extends Fragment {
    private String CurrentUid;
    private ArrayList<ReviewModel> ReviewList;
    private ArrayList<UserModel> WritenUserList;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "SearchResultFragment";
    private ReviewResultAdapter ReviewResultAdapter;
    private ArrayList<String> WritenReviewList;
    UserModel userModel;
    UserModel WritenUserModel;
    int i;

    public My_Review_Writen_Fragment() {

    }
    public static final My_Review_Writen_Fragment getInstance(String CurrentUid) {
        My_Review_Writen_Fragment f = new My_Review_Writen_Fragment();
        Bundle bdl = new Bundle();
        bdl.putString("CurrentUid", CurrentUid);
        f.setArguments(bdl);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchresult, container, false);
        CurrentUid =  getArguments().getString("CurrentUid");

        firebaseFirestore = FirebaseFirestore.getInstance();
        ReviewList = new ArrayList<>();
        WritenUserList = new ArrayList<>();
        WritenReviewList = new ArrayList<>();
        ReviewResultAdapter = new ReviewResultAdapter(getActivity(), ReviewList, WritenUserList);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(ReviewResultAdapter);
        MarketUpdate(true);
        return  view;
    }
    private void MarketUpdate(final boolean clear) {
        Log.d("test","CurrentUid : "+CurrentUid);
        final DocumentReference documentReferenceMyUser = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUid);
        documentReferenceMyUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                Log.d("test","userModel : "+userModel);
                WritenReviewList = userModel.getUserModel_WritenReviewList();
                Log.d("test","WritenReviewList : "+WritenReviewList);
                for(i =0;i<WritenReviewList.size();i++){
                    final DocumentReference documentReferenceMyUser = FirebaseFirestore.getInstance().collection("USERS").document(WritenReviewList.get(i));
                    documentReferenceMyUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserModel WritenUserModel = documentSnapshot.toObject(UserModel.class);
                            WritenUserList.add(WritenUserModel);
                            Log.d("test","WritenUserList WritenUserList : "+WritenUserList);
                            ReviewResultAdapter.notifyDataSetChanged();
                        }
                    });
                    Date date = new Date();
                    CollectionReference collectionReference = firebaseFirestore.collection("USERS").document(WritenReviewList.get(i)).collection("REVIEW");
                    collectionReference.whereEqualTo("ReviewModel_To_User_Uid",CurrentUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
//                                if(clear){                      //part22 : clear를 boolean으로 써서 업데이트 도중에 게시물 클릭시 발생하는 오류 해결 (3'30")   // part15 : MainAdapter에서 setOnClickListener에서 시작 (35'30")
//                                    ReviewList.clear();                                                           // part16 : List 안의 데이터 초기화
//                                }                                                                               // part16 : postsUpdate로 이동 (15'50")
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ReviewModel reviewModel = document.toObject(ReviewModel.class);
                                    Log.d("test","ReviewList : "+ReviewList);
                                    ReviewList.add(reviewModel);
                                    Log.d("test","ReviewList ReviewList : "+ReviewList);
                                }
                                ReviewResultAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("민규다다","없음");
                            }
                        }
                    });
                }
            }
        });

    }
}