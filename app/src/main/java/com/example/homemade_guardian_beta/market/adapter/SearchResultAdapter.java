package com.example.homemade_guardian_beta.market.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.market.activity.MarketActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//SearchResultFragment와 연결된 어댑터이다. onBindViewHolder로 카드뷰에 검색된 게시물의 정보들을 담는 역할을 한다. !검색된 결과의 게시물 나열!
//      Ex) MORE_INDEX를 readContentsView에서 MORE_INDEX 보다 작은 수의 사진만 SearchResultFragment에서 나타낸다.

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MainViewHolder> {
    private ArrayList<MarketModel> ArrayList_MarketModel;   //게시물에 담을 PostModel의 정보들을 담는다.
    private Activity Activity;

    private final int MORE_INDEX = 1;                   //게시물에서 미리 표현할 사진의 개수

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView Cardview;
        MainViewHolder(CardView v) {
            super(v);
            Cardview = v;
        }
    }

    public SearchResultAdapter(Activity activity, ArrayList<MarketModel> arrayList_Marketmodel) {
        this.ArrayList_MarketModel = arrayList_Marketmodel;
        this.Activity = activity;
    }

    //검색한 결과를 POSTS에서 불러온 게시물 내용으로 화면에 나타내는 Holder
    @NonNull
    @Override
    public SearchResultAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {             // part : 게시물을 눌렀을 떄
        CardView Cardview = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        final MainViewHolder Mainviewholder = new MainViewHolder(Cardview);
        Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                                   // part18 : 게시물 클릭시 게시물페이지로 이동 (36'10")
                Intent Intent_MarketActivity = new Intent(Activity, MarketActivity.class);
                //postInfo 안에 uid있음
                Intent_MarketActivity.putExtra("marketInfo", ArrayList_MarketModel.get(Mainviewholder.getAdapterPosition()));
                Activity.startActivity(Intent_MarketActivity);
            }
        });
        return Mainviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {                      // part : 게시물을 나열
        CardView Contents_CardView = holder.Cardview;
        TextView Title_TextView = Contents_CardView.findViewById(R.id.Post_Title_TextView);
        TextView Contents_TextView = Contents_CardView.findViewById(R.id.Post_Contents_TextView);
        TextView Market_Category = Contents_CardView.findViewById(R.id.Post_Category);
        TextView Market_LikeCount = Contents_CardView.findViewById(R.id.Post_LikeCount);
        MarketModel marketModel = ArrayList_MarketModel.get(position);                                                         //HomeFragment에서 PostInfo(mDaset)에 넣은 데이터 get
        Title_TextView.setText(marketModel.getMarketModel_Title());
        Contents_TextView.setText(marketModel.getMarketModel_Text());
        Market_Category.setText(marketModel.getMarketModel_Category());
        Market_LikeCount.setText(String.valueOf(marketModel.getMarketModel_LikeList().size()));
        ImageView Thumbnail_ImageView = Contents_CardView.findViewById(R.id.Post_ImageView);                   //contentsLayout에다가 날짜포함
        LinearLayout Contentslayout = Contents_CardView.findViewById(R.id.contentsLayout);                      /////////////////////이거 대신 텍스트 만든거 보여주기로
        TextView DateOfManufacture_TextView = Contents_CardView.findViewById(R.id.Post_DateOfManufacture);
        DateOfManufacture_TextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(marketModel.getMarketModel_DateOfManufacture()));
        TextView Market_CommentCount = Contents_CardView.findViewById(R.id.comment_count_text);
        Market_CommentCount.setText(String.valueOf(marketModel.getMarketModel_CommentCount()));

        ArrayList<String> ArrayList_ImageList = marketModel.getMarketModel_ImageList();
        if(ArrayList_ImageList != null) {
            String Image = ArrayList_ImageList.get(0);
            Glide.with(Activity).load(Image).centerCrop().override(500).thumbnail(0.1f).into(Thumbnail_ImageView);         // 흐릿하게 로딩하기
        }else {
            Thumbnail_ImageView.setVisibility(View.GONE);
        }
        TextView market_state = Contents_CardView.findViewById(R.id.market_state);
        market_state.setText("");
        GradientDrawable state_none= (GradientDrawable) ContextCompat.getDrawable(this.Activity, R.drawable.state_none);
        market_state.setBackground(state_none);
        market_state.setVisibility(View.INVISIBLE);
        if(!marketModel.getMarketModel_reservation().equals("X")){
            market_state.setVisibility(View.VISIBLE);
            if(!marketModel.getMarketModel_deal().equals("X")){
                market_state.setText("거래완료");
                GradientDrawable state_deal= (GradientDrawable) ContextCompat.getDrawable(this.Activity, R.drawable.state_deal);
                market_state.setBackground(state_deal);
            }else{
                market_state.setText("예약중");
                GradientDrawable state_reserved= (GradientDrawable) ContextCompat.getDrawable(this.Activity, R.drawable.state_reserved);
                market_state.setBackground(state_reserved);
            }
        }
    }

    @Override
    public int getItemViewType(int position){ return position; }

    @Override
    public int getItemCount() {
        return ArrayList_MarketModel.size();
    }
}