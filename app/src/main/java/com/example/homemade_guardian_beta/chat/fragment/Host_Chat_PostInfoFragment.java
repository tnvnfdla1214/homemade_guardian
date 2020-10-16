package com.example.homemade_guardian_beta.chat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.Main.activity.ReviewActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.activity.ChatActivity;
import com.example.homemade_guardian_beta.model.UserModel;
import com.example.homemade_guardian_beta.model.post.PostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//호스트용 포스트 정보 프레그먼트
public class Host_Chat_PostInfoFragment extends Fragment {

    String PostModel_Post_Uid;
    String To_User_Uid;
    String currentUser_Uid;
    PostModel postModel;
    UserModel userModel;
    TextView Chat_PostInfo_Title;
    TextView Chat_PostInfo_Text;
    ImageView Chat_PostInfo_Image;

    Switch Chat_PostInfo_reservation;                       //예약 버튼
    Switch Chat_PostInfo_deal;                              //거래완료 버튼

   ChatActivity chatActivity;

    ArrayList<String> UnReViewList = new ArrayList<>();

    TextView main_label;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        chatActivity = (ChatActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //이제 더이상 엑티비티 참초가안됨
        chatActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //프래그먼트 메인을 인플레이트해주고 컨테이너에 붙여달라는 뜻임
        final ViewGroup View = (ViewGroup) inflater.inflate(R.layout.fragment_host_chat_postinfo, container, false);
        Chat_PostInfo_Title = (TextView)View.findViewById(R.id.Chat_PostInfo_Title);
        Chat_PostInfo_Text = (TextView)View.findViewById(R.id.Chat_PostInfo_Text);
        Chat_PostInfo_Image = (ImageView) View.findViewById(R.id.Chat_PostInfo_Image);
        Chat_PostInfo_reservation = (Switch) View.findViewById(R.id.Chat_PostInfo_reservation);
        Chat_PostInfo_deal = (Switch) View.findViewById(R.id.Chat_PostInfo_deal);

        Chat_PostInfo_deal.setEnabled(false);
        Bundle Postbundle = getArguments();
        PostModel_Post_Uid = Postbundle.getString("PostModel_Post_Uid");
        To_User_Uid = Postbundle.getString("To_User_Uid");
        currentUser_Uid = Postbundle.getString("currentUser_Uid");

        main_label = (TextView) View.findViewById(R.id.main_label);



        DocumentReference docRef_POSTS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(PostModel_Post_Uid);
        docRef_POSTS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                postModel = documentSnapshot.toObject(PostModel.class);
                Chat_PostInfo_Title.setText(postModel.getPostModel_Title());
                Chat_PostInfo_Text.setText(postModel.getPostModel_Text());
                //post의 이미지 섬네일 띄우기
                if(postModel.getPostModel_ImageList() != null){
                    Glide.with(getContext()).load(postModel.getPostModel_ImageList().get(0)).centerCrop().override(500).into(Chat_PostInfo_Image);
                }
                else{
                    Chat_PostInfo_Image.setVisibility(View.GONE);
                }
                if(postModel.getPostModel_reservation().equals("O")){
                    Chat_PostInfo_reservation.setChecked(true);
                }

                if(postModel.getPostModel_deal().equals("O")){
                    Chat_PostInfo_deal.setChecked(true);
                }

            }
        });

        Chat_PostInfo_reservation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    DocumentReference docRef_POSTS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(PostModel_Post_Uid);
                    docRef_POSTS_HostUid
                            .update("PostModel_reservation", "O")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Chat_PostInfo_deal.setEnabled(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                }else{
                    DocumentReference docRef_POSTS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(PostModel_Post_Uid);
                    docRef_POSTS_HostUid
                            .update("PostModel_reservation", "X")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Chat_PostInfo_deal.setEnabled(false);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });

        Chat_PostInfo_deal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    DocumentReference docRef_POSTS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(PostModel_Post_Uid);
                    docRef_POSTS_HostUid
                            .update("PostModel_deal", "O")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    final DocumentReference documentReferencegetToUser = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
                                    documentReferencegetToUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            userModel = documentSnapshot.toObject(UserModel.class);
                                            UnReViewList = userModel.getUserModel_UnReViewList();
                                            UnReViewList.add(currentUser_Uid);
                                            userModel.setUserModel_UnReViewList(UnReViewList);

                                            final DocumentReference documentReferencesetToUser = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
                                            documentReferencesetToUser.set(userModel.getUserInfo())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                    ReviewActivity reviewActivity = new ReviewActivity(getContext());
                    reviewActivity.callFunction(main_label);

                }else{
                    DocumentReference docRef_POSTS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(PostModel_Post_Uid);
                    docRef_POSTS_HostUid
                            .update("PostModel_deal", "X")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });
        return View;
    }
}
