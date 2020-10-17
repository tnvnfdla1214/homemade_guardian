package com.example.homemade_guardian_beta.chat.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.activity.ChatActivity;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//호스트용 포스트 정보 프레그먼트
public class Guest_Chat_PostInfoFragment extends Fragment {

    String PostModel_Post_Uid;
    String CurrentUserUid;
    MarketModel marketModel;
    TextView Chat_PostInfo_Title;
    TextView Chat_PostInfo_Text;
    ImageView Chat_PostInfo_Image;

    Button Chat_PostInfo_reservation;//예약 버튼

   ChatActivity chatActivity;

    TextView Chat_PostInfo_dealText;


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
        final ViewGroup View = (ViewGroup) inflater.inflate(R.layout.fragment_guest_chat_postinfo, container, false);
        Chat_PostInfo_Title = (TextView)View.findViewById(R.id.Chat_PostInfo_Title);
        Chat_PostInfo_Text = (TextView)View.findViewById(R.id.Chat_PostInfo_Text);
        Chat_PostInfo_Image = (ImageView) View.findViewById(R.id.Chat_PostInfo_Image);
        Chat_PostInfo_reservation = (Button) View.findViewById(R.id.Chat_PostInfo_reservation);
        Chat_PostInfo_dealText = (TextView)View.findViewById(R.id.Chat_PostInfo_dealText);

        CurrentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Bundle Postbundle = getArguments();
        PostModel_Post_Uid = Postbundle.getString("PostModel_Post_Uid");
        DocumentReference docRef_USERS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(PostModel_Post_Uid);
        docRef_USERS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                marketModel = documentSnapshot.toObject(MarketModel.class);
                Chat_PostInfo_Title.setText(marketModel.getMarketModel_Title());
                Chat_PostInfo_Text.setText(marketModel.getMarketModel_Text());
                //post의 이미지 섬네일 띄우기
                if(marketModel.getMarketModel_ImageList() != null){
                    Glide.with(getContext()).load(marketModel.getMarketModel_ImageList().get(0)).centerCrop().override(500).into(Chat_PostInfo_Image);
                }
                else{
                    Chat_PostInfo_Image.setVisibility(View.GONE);
                }
                if(marketModel.getMarketModel_reservation().equals("O")){
                    if(marketModel.getMarketModel_deal().equals("O")) {
                        Chat_PostInfo_dealText.setText("[거래완료]");
                        Chat_PostInfo_dealText.setTextColor(Color.parseColor("#e65d5d"));
                    }
                    else{
                        Chat_PostInfo_dealText.setText("[예약중]");
                        Chat_PostInfo_dealText.setTextColor(Color.parseColor("#e65d5d"));
                    }
                }
                else{
                    Chat_PostInfo_dealText.setText("");
                }

            }
        });

        return View;
    }
}
