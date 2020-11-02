package com.example.homemade_guardian_beta.Main.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.user.ReviewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//SearchResultFragment와 연결된 어댑터이다. onBindViewHolder로 카드뷰에 검색된 게시물의 정보들을 담는 역할을 한다. !검색된 결과의 게시물 나열!
//      Ex) MORE_INDEX를 readContentsView에서 MORE_INDEX 보다 작은 수의 사진만 SearchResultFragment에서 나타낸다.

public class ReviewResultAdapter extends RecyclerView.Adapter<ReviewResultAdapter.MainViewHolder> {
    private ArrayList<ReviewModel> ArrayList_ReviewModel;   //게시물에 담을 PostModel의 정보들을 담는다.
    private Activity Activity;

    private final int MORE_INDEX = 1;                   //게시물에서 미리 표현할 사진의 개수

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView Cardview;
        MainViewHolder(CardView v) {
            super(v);
            Cardview = v;
        }
    }

    public ReviewResultAdapter(Activity activity, ArrayList<ReviewModel> ArrayList_ReviewModel) {
        this.ArrayList_ReviewModel = ArrayList_ReviewModel;
        this.Activity = activity;
    }

    //검색한 결과를 POSTS에서 불러온 게시물 내용으로 화면에 나타내는 Holder
    @NonNull
    @Override
    public ReviewResultAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {             // part : 게시물을 눌렀을 떄
        CardView Cardview = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        final MainViewHolder Mainviewholder = new MainViewHolder(Cardview);
        return Mainviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {                      // part : 게시물을 나열
        CardView Contents_CardView = holder.Cardview;
        ImageView Review_profileImage = Contents_CardView.findViewById(R.id.Review_profileImage);
        TextView Review_Nickname = Contents_CardView.findViewById(R.id.Review_Nickname);
        TextView Review_DateOfManufacture = Contents_CardView.findViewById(R.id.Review_DateOfManufacture);
        TextView Selected_Review = Contents_CardView.findViewById(R.id.Selected_Review);
        TextView Review = Contents_CardView.findViewById(R.id.Review);

        ReviewModel reviewModel = ArrayList_ReviewModel.get(position);
        String profileImage = reviewModel.getReviewModel_To_User_ProfileImage();
        if(profileImage != null) {
            Glide.with(Activity).load(profileImage).centerCrop().override(500).into(Review_profileImage);         // 흐릿하게 로딩하기
        }else {
            Glide.with(Activity).load(R.drawable.none_profile_user).centerCrop().override(500).into(Review_profileImage);
        }
        Review_Nickname.setText(reviewModel.getReviewModel_To_User_NickName());
        Review_DateOfManufacture.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(reviewModel.getReviewModel_DateOfManufacture()));
        switch (reviewModel.getReviewModel_Selected_Review()) {
            case 0 : Selected_Review.setText("친절함"); break;
            case 1 : Selected_Review.setText("정확함"); break;
            case 2 : Selected_Review.setText("완벽함"); break;
            case 3 : Selected_Review.setText("불쾌함"); break;
        }
        Review.setText(reviewModel.getReviewModel_Review());
    }

    @Override
    public int getItemViewType(int position){ return position; }

    @Override
    public int getItemCount() {
        return ArrayList_ReviewModel.size();
    }
}