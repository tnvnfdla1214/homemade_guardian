package com.example.homemade_guardian_beta.community.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.community.activity.CommunityActivity;
import com.example.homemade_guardian_beta.model.community.CommunityModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//SearchResultFragment와 연결된 어댑터이다. onBindViewHolder로 카드뷰에 검색된 게시물의 정보들을 담는 역할을 한다. !검색된 결과의 게시물 나열!
//      Ex) MORE_INDEX를 readContentsView에서 MORE_INDEX 보다 작은 수의 사진만 SearchResultFragment에서 나타낸다.

public class SearchCommunityResultAdapter extends RecyclerView.Adapter<SearchCommunityResultAdapter.MainViewHolder> {
    private ArrayList<CommunityModel> ArrayList_CommunityModel;   //게시물에 담을 PostModel의 정보들을 담는다.
    private Activity Activity;

    private final int MORE_INDEX = 1;                   //게시물에서 미리 표현할 사진의 개수

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView Cardview;
        MainViewHolder(CardView v) {
            super(v);
            Cardview = v;
        }
    }

    public SearchCommunityResultAdapter(Activity activity, ArrayList<CommunityModel> arrayList_Communitymodel) {
        this.ArrayList_CommunityModel = arrayList_Communitymodel;
        this.Activity = activity;
    }

    //검색한 결과를 POSTS에서 불러온 게시물 내용으로 화면에 나타내는 Holder
    @NonNull
    @Override
    public SearchCommunityResultAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {             // part : 게시물을 눌렀을 떄
        CardView Cardview = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent, false);
        final MainViewHolder Mainviewholder = new MainViewHolder(Cardview);
        Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                                   // part18 : 게시물 클릭시 게시물페이지로 이동 (36'10")
                Intent Intent_CommunityActivity = new Intent(Activity, CommunityActivity.class);
                //postInfo 안에 uid있음
                Intent_CommunityActivity.putExtra("communityInfo", ArrayList_CommunityModel.get(Mainviewholder.getAdapterPosition()));
                Activity.startActivity(Intent_CommunityActivity);
            }
        });
        return Mainviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {                      // part : 게시물을 나열
        CardView Contents_CardView = holder.Cardview;
        TextView Title_TextView = Contents_CardView.findViewById(R.id.Post_Title_TextView);
        TextView Contents_TextView = Contents_CardView.findViewById(R.id.Post_Contents_TextView);
        TextView Community_LikeCount = Contents_CardView.findViewById(R.id.Post_LikeCount);
        CommunityModel communityModel = ArrayList_CommunityModel.get(position);                                                         //HomeFragment에서 PostInfo(mDaset)에 넣은 데이터 get
        Title_TextView.setText(communityModel.getCommunityModel_Title());
        Contents_TextView.setText(communityModel.getCommunityModel_Text());
        Community_LikeCount.setText(String.valueOf(communityModel.getCommunityModel_LikeList().size()));
        ImageView Thumbnail_ImageView = Contents_CardView.findViewById(R.id.Post_ImageView);                   //contentsLayout에다가 날짜포함
        LinearLayout Contentslayout = Contents_CardView.findViewById(R.id.contentsLayout);                      /////////////////////이거 대신 텍스트 만든거 보여주기로
        TextView DateOfManufacture_TextView = Contents_CardView.findViewById(R.id.Post_DateOfManufacture);
        DateOfManufacture_TextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(communityModel.getCommunityModel_DateOfManufacture()));

        TextView Community_CommentCount = Contents_CardView.findViewById(R.id.comment_count_text);
        Community_CommentCount.setText(String.valueOf(communityModel.getCommunityModel_CommentCount()));
        ArrayList<String> ArrayList_ImageList = communityModel.getCommunityModel_ImageList();
        if(ArrayList_ImageList != null) {
            String Image = ArrayList_ImageList.get(0);
            Glide.with(Activity).load(Image).centerCrop().override(500).thumbnail(0.1f).into(Thumbnail_ImageView);         // 흐릿하게 로딩하기
        }else {
            Thumbnail_ImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position){ return position; }

    @Override
    public int getItemCount() {
        return ArrayList_CommunityModel.size();
    }
}