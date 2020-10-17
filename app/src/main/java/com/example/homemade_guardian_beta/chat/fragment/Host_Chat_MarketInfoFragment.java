package com.example.homemade_guardian_beta.chat.fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

//호스트용 포스트 정보 프레그먼트
public class Host_Chat_MarketInfoFragment extends Fragment {

    String MarketModel_Market_Uid;
    String To_User_Uid;
    String currentUser_Uid;
    MarketModel marketModel;
    UserModel userModel;
    TextView Chat_MarketInfo_Title;
    TextView Chat_MarketInfo_Text;
    ImageView Chat_MarketInfo_Image;

    Switch Chat_MarketInfo_reservation;                       //예약 버튼
    Switch Chat_MarketInfo_deal;                              //거래완료 버튼

   ChatActivity chatActivity;

    ArrayList<String> UnReViewUserList = new ArrayList<>();
    ArrayList<String> UnReViewMarketList = new ArrayList<>();


    TextView main_label;
    TextView selected_review;

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
        Chat_MarketInfo_Title = (TextView)View.findViewById(R.id.Chat_PostInfo_Title);
        Chat_MarketInfo_Text = (TextView)View.findViewById(R.id.Chat_PostInfo_Text);
        Chat_MarketInfo_Image = (ImageView) View.findViewById(R.id.Chat_PostInfo_Image);
        Chat_MarketInfo_reservation = (Switch) View.findViewById(R.id.Chat_PostInfo_reservation);
        Chat_MarketInfo_deal = (Switch) View.findViewById(R.id.Chat_PostInfo_deal);

        Chat_MarketInfo_deal.setEnabled(false);
        Bundle Marketbundle = getArguments();
        MarketModel_Market_Uid = Marketbundle.getString("PostModel_Post_Uid");
        To_User_Uid = Marketbundle.getString("To_User_Uid");
        currentUser_Uid = Marketbundle.getString("currentUser_Uid");

        main_label = (TextView) View.findViewById(R.id.main_label);
        selected_review = (TextView) View.findViewById(R.id.selected_review);


        DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(MarketModel_Market_Uid);
        docRef_MARKETS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                marketModel = documentSnapshot.toObject(MarketModel.class);
                Chat_MarketInfo_Title.setText(marketModel.getMarketModel_Title());
                Chat_MarketInfo_Text.setText(marketModel.getMarketModel_Text());
                //post의 이미지 섬네일 띄우기
                if(marketModel.getMarketModel_ImageList() != null){
                    Glide.with(getContext()).load(marketModel.getMarketModel_ImageList().get(0)).centerCrop().override(500).into(Chat_MarketInfo_Image);
                }
                else{
                    Chat_MarketInfo_Image.setVisibility(View.GONE);
                }
                if(marketModel.getMarketModel_reservation().equals("O")){
                    Chat_MarketInfo_reservation.setChecked(true);
                }

                if(marketModel.getMarketModel_deal().equals("O")){
                    Chat_MarketInfo_deal.setChecked(true);
                }

            }
        });

        Chat_MarketInfo_reservation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(MarketModel_Market_Uid);
                    docRef_MARKETS_HostUid
                            .update("PostModel_reservation", "O")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Chat_MarketInfo_deal.setEnabled(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                }else{
                    DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(MarketModel_Market_Uid);
                    docRef_MARKETS_HostUid
                            .update("PostModel_reservation", "X")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Chat_MarketInfo_deal.setEnabled(false);
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

        Chat_MarketInfo_deal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(MarketModel_Market_Uid);
                    docRef_MARKETS_HostUid
                            .update("PostModel_deal", "O")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    final DocumentReference documentReferencegetToUser = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
                                    documentReferencegetToUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            userModel = documentSnapshot.toObject(UserModel.class);
                                            UnReViewUserList = userModel.getUserModel_UnReViewUserList();
                                            UnReViewUserList.add(currentUser_Uid);
                                            userModel.setUserModel_UnReViewUserList(UnReViewUserList);
                                            UnReViewMarketList = userModel.getUserModel_UnReViewPostList();
                                            UnReViewMarketList.add(MarketModel_Market_Uid);
                                            userModel.setUserModel_UnReViewPostList(UnReViewMarketList);

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
                    reviewActivity.callFunction(To_User_Uid, MarketModel_Market_Uid);

                }else{
                    DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(MarketModel_Market_Uid);
                    docRef_MARKETS_HostUid
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
