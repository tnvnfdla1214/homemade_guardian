package com.example.homemade_guardian_beta.chat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.activity.ChatActivity;
import com.example.homemade_guardian_beta.model.post.PostModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Chat_PostInfoFragment extends Fragment {

    String PostModel_Post_Uid;
    String CurrentUserUid;
    PostModel postModel;
    TextView Chat_PostInfo_Title;
    TextView Chat_PostInfo_Text;
    ImageView Chat_PostInfo_Image;

    Button Chat_PostInfo_reservation;//예약 버튼
    LinearLayout Chat_PostInfo_reservation_LinearLayout; //예약 후 예약캔슬과 거래완료 버튼 생성 시키는 LinearLayout
    Button Chat_PostInfo_reservation_cancel; //예약 캔슬 버튼
    Button Chat_PostInfo_deal_cancel; //거래 완료 버튼튼


   ChatActivity chatActivity;
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
        final ViewGroup View = (ViewGroup) inflater.inflate(R.layout.fragment_chat__post_info , container, false);
        Chat_PostInfo_Title = (TextView)View.findViewById(R.id.Chat_PostInfo_Title);
        Chat_PostInfo_Text = (TextView)View.findViewById(R.id.Chat_PostInfo_Text);
        Chat_PostInfo_Image = (ImageView) View.findViewById(R.id.Chat_PostInfo_Image);
        Chat_PostInfo_reservation = (Button) View.findViewById(R.id.Chat_PostInfo_reservation);
        Chat_PostInfo_reservation_LinearLayout = (LinearLayout) View.findViewById(R.id.Chat_PostInfo_reservation_LinearLayout);
        Chat_PostInfo_reservation_cancel = (Button) View.findViewById(R.id.Chat_PostInfo_reservation_cancel);
        Chat_PostInfo_deal_cancel = (Button) View.findViewById(R.id.Chat_PostInfo_deal_cancel);

        CurrentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Bundle Postbundle = getArguments();
        PostModel_Post_Uid = Postbundle.getString("PostModel_Post_Uid");
        DocumentReference docRef_USERS_HostUid = FirebaseFirestore.getInstance().collection("POSTS").document(PostModel_Post_Uid);
        docRef_USERS_HostUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                postModel = documentSnapshot.toObject(PostModel.class);
                Chat_PostInfo_Title.setText(postModel.getPostModel_Title());
                Chat_PostInfo_Text.setText(postModel.getPostModel_Text());
                //post의 작성자가 현재 자신인지 혹은 남인지 확인후 틀리다면 예약 버튼 지우기 그후 예약 텍스트를 버튼으로 바꾸기
                //post의 이미지 섬네일 띄우기
                if(postModel.getPostModel_ImageList() != null){
                    Glide.with(getContext()).load(postModel.getPostModel_ImageList().get(0)).centerCrop().override(500).into(Chat_PostInfo_Image);
                }
                else{
                    Chat_PostInfo_Image.setVisibility(View.GONE);
                }
            }
        });

        Chat_PostInfo_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat_PostInfo_reservation.setVisibility(View.GONE);
                Chat_PostInfo_reservation_LinearLayout.setVisibility(View.VISIBLE);
            }
        });

        return View;
    }
}
