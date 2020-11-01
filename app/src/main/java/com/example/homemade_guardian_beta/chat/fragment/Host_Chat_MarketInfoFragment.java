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
import androidx.cardview.widget.CardView;
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
    CardView Chat_PostInfo_Card;

    Switch Chat_MarketInfo_reservation;                       //예약 버튼
    Switch Chat_MarketInfo_deal;                              //거래완료 버튼

    ChatActivity chatActivity;

    ArrayList<String> UnReViewUserList = new ArrayList<>();
    ArrayList<String> UnReViewMarketList = new ArrayList<>();

    ArrayList<String> Market_reservationList = new ArrayList<>();
    ArrayList<String> Market_dealList = new ArrayList<>();

    int reservationsetting = 0;
    int dealsetting  = 0;

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
        final ViewGroup View = (ViewGroup) inflater.inflate(R.layout.fragment_host_chat_marketinfo, container, false);
        Chat_MarketInfo_Title = (TextView)View.findViewById(R.id.Chat_PostInfo_Title);
        Chat_MarketInfo_Text = (TextView)View.findViewById(R.id.Chat_PostInfo_Text);
        Chat_MarketInfo_Image = (ImageView) View.findViewById(R.id.Chat_PostInfo_Image);
        Chat_MarketInfo_reservation = (Switch) View.findViewById(R.id.Chat_PostInfo_reservation);
        Chat_MarketInfo_deal = (Switch) View.findViewById(R.id.Chat_PostInfo_deal);
        Chat_PostInfo_Card = View.findViewById(R.id.Chat_PostInfo_Card);
        Chat_MarketInfo_deal.setEnabled(false);
        Bundle Marketbundle = getArguments();
        MarketModel_Market_Uid = Marketbundle.getString("MarketModel_Market_Uid");
        To_User_Uid = Marketbundle.getString("To_User_Uid");
        currentUser_Uid = Marketbundle.getString("currentUser_Uid");


        Setting_Post_Info();
        Switch_reservatrion();
        Switch_deal();

        return View;
    }

    //기본 세팅
    void Setting_Post_Info(){
        reservationsetting=0;
        dealsetting=0;
        DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("MARKETS").document(MarketModel_Market_Uid);
        docRef_MARKETS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                marketModel = documentSnapshot.toObject(MarketModel.class);
                Chat_MarketInfo_Title.setText(marketModel.getMarketModel_Title());
                Chat_MarketInfo_Text.setText(marketModel.getMarketModel_Text());
                //post의 이미지 섬네일 띄우기
                if(marketModel.getMarketModel_ImageList() != null){
                    Chat_PostInfo_Card.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(marketModel.getMarketModel_ImageList().get(0)).centerCrop().override(500).into(Chat_MarketInfo_Image);
                }
                if(!marketModel.getMarketModel_reservation().equals("X")){
                    Chat_MarketInfo_reservation.setChecked(true);
                    reservationsetting =1;
                }

                if(!marketModel.getMarketModel_deal().equals("X")){
                    Chat_MarketInfo_reservation.setChecked(true);
                    Chat_MarketInfo_deal.setChecked(true);
                    dealsetting = 1;
                    Chat_MarketInfo_reservation.setEnabled(false);
                    Chat_MarketInfo_deal.setEnabled(false);
                }
                Switch_reservatrion();
                Switch_deal();
            }
        });
    }

    //예약 버튼
    //마켓의 reservation X -> 상대방의 Uid로 바꾸기
    void Switch_reservatrion(){
        Chat_MarketInfo_reservation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && reservationsetting !=1){
                    DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("MARKETS").document(MarketModel_Market_Uid);
                    docRef_MARKETS_HostUid
                            .update("MarketModel_reservation", To_User_Uid)
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


                    //currentuseruid에 예약한 포스트의 uid 삽입
                    final DocumentReference documentReferenceMyUser = FirebaseFirestore.getInstance().collection("USERS").document(currentUser_Uid);
                    documentReferenceMyUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userModel = documentSnapshot.toObject(UserModel.class);
                            Market_reservationList = userModel.getUserModel_Market_reservationList();
                            Market_reservationList.add(MarketModel_Market_Uid);
                            userModel.setUserModel_Market_reservationList(Market_reservationList);
                            final DocumentReference documentReferencesetToUser = FirebaseFirestore.getInstance().collection("USERS").document(currentUser_Uid);
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

                    //To_User_Uid에 예약한 포스트의 uid 삽입
                    final DocumentReference documentReferenceToUser = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
                    documentReferenceToUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userModel = documentSnapshot.toObject(UserModel.class);
                            Market_reservationList = userModel.getUserModel_Market_reservationList();
                            Market_reservationList.add(MarketModel_Market_Uid);
                            userModel.setUserModel_Market_reservationList(Market_reservationList);
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

                }else{
                    DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("MARKETS").document(MarketModel_Market_Uid);
                    docRef_MARKETS_HostUid
                            .update("MarketModel_reservation", "X")
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

                    //currentuseruid에 예약한 포스트의 uid 빼기
                    final DocumentReference documentReferenceMyUser = FirebaseFirestore.getInstance().collection("USERS").document(currentUser_Uid);
                    documentReferenceMyUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userModel = documentSnapshot.toObject(UserModel.class);
                            Market_reservationList = userModel.getUserModel_Market_reservationList();
                            Market_reservationList.remove(MarketModel_Market_Uid);
                            userModel.setUserModel_Market_reservationList(Market_reservationList);
                            final DocumentReference documentReferencesetToUser = FirebaseFirestore.getInstance().collection("USERS").document(currentUser_Uid);
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

                    //To_User_Uid에 예약한 포스트의 uid 빼기
                    final DocumentReference documentReferenceToUser = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
                    documentReferenceToUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userModel = documentSnapshot.toObject(UserModel.class);
                            Market_reservationList = userModel.getUserModel_Market_reservationList();
                            Market_reservationList.remove(MarketModel_Market_Uid);
                            userModel.setUserModel_Market_reservationList(Market_reservationList);

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
            }
        });
    }

    //딜 버튼
    void Switch_deal(){
        Chat_MarketInfo_deal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked&&dealsetting !=1){
                    Log.d("민규규","2");
                    //마켓에 있는 deal X -> 상대방의 Uid로 바꾸기
                    DocumentReference docRef_MARKETS_HostUid = FirebaseFirestore.getInstance().collection("MARKETS").document(MarketModel_Market_Uid);
                    docRef_MARKETS_HostUid
                            .update("MarketModel_deal", To_User_Uid)
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

                    //currentuseruid에 거래 완료된 포스틔의 uid 삽입
                    final DocumentReference documentReferenceMyUser = FirebaseFirestore.getInstance().collection("USERS").document(currentUser_Uid);
                    documentReferenceMyUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userModel = documentSnapshot.toObject(UserModel.class);
                            Market_dealList = userModel.getUserModel_Market_dealList();
                            Market_reservationList = userModel.getUserModel_Market_reservationList();
                            Market_dealList.add(MarketModel_Market_Uid);
                            Market_reservationList.remove(MarketModel_Market_Uid);
                            userModel.setUserModel_Market_dealList(Market_dealList);
                            userModel.setUserModel_Market_reservationList(Market_reservationList);
                            final DocumentReference documentReferencesetToUser = FirebaseFirestore.getInstance().collection("USERS").document(currentUser_Uid);
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

                    //To_User_Uid에 거래 완료된 포스틔의 uid 삽입
                    final DocumentReference documentReferenceToUser = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
                    documentReferenceToUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("민규","ㅁㄴㅇ1");
                            userModel = documentSnapshot.toObject(UserModel.class);
                            Market_dealList = userModel.getUserModel_Market_dealList();
                            Market_reservationList = userModel.getUserModel_Market_reservationList();
                            Market_dealList.add(MarketModel_Market_Uid);
                            Market_reservationList.remove(MarketModel_Market_Uid);
                            userModel.setUserModel_Market_dealList(Market_dealList);
                            userModel.setUserModel_Market_reservationList(Market_reservationList);
                            final DocumentReference documentReferencesetToUser = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
                            documentReferencesetToUser.set(userModel.getUserInfo())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Chat_MarketInfo_reservation.setEnabled(false);
                                            Chat_MarketInfo_deal.setEnabled(false);
                                            Log.d("민규","ㅁㄴㅇ2");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                        }
                    });

                    //리뷰 창 띄우기
                    //ReviewActivity reviewActivity = new ReviewActivity(getContext());
                    //reviewActivity.callFunction(To_User_Uid, MarketModel_Market_Uid);

                }else{

                }
            }
        });
    }

}