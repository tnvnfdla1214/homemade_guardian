package com.example.homemade_guardian_beta.post.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.homemade_guardian_beta.post.PostModel;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.activity.PostActivity;
import com.example.homemade_guardian_beta.post.view.ThumbnailImageView;

import java.util.ArrayList;

//HomeFragment와 연결된 어댑터이다. onBindViewHolder로 카드뷰에 게시물의 정보들을 담는 역할을 한다.
//      Ex) MORE_INDEX를 readContentsView에서 MORE_INDEX 보다 작은 수의 사진만 MainFragment에서 나타낸다.

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MainViewHolder> {
    private ArrayList<PostModel> ArrayList_PostModel;       //게시물에 담을 PostModel의 정보들을 담는다.
    private Activity activity;

    private final int MORE_INDEX = 1;                       //게시물에서 미리 표현할 사진의 개수

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public HomeAdapter(Activity activity, ArrayList<PostModel> ArrayList_PostModel) {
        this.ArrayList_PostModel = ArrayList_PostModel;
        this.activity = activity;
    }

    @NonNull
    @Override
    public HomeAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {             // part : 게시물을 눌렀을 떄
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                                   // part18 : 게시물 클릭시 게시물페이지로 이동 (36'10")
                Intent intent = new Intent(activity, PostActivity.class);
                intent.putExtra("postInfo", ArrayList_PostModel.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {                      // part : 게시물을 나열
        CardView Contents_CardView = holder.cardView;
        TextView Title = Contents_CardView.findViewById(R.id.titleTextView);
        PostModel postModel = ArrayList_PostModel.get(position);                                                         //HomeFragment에서 PostInfo(mDaset)에 넣은 데이터 get
        Title.setText(postModel.getPostModel_Title());
        ThumbnailImageView thumbnailImageView = Contents_CardView.findViewById(R.id.readContentsView);                   //contentsLayout에다가 날짜포함
        LinearLayout contentsLayout = Contents_CardView.findViewById(R.id.contentsLayout);

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(postModel)) {                 // part16 : 게시물 개수에 변화가 있을 때만 실행..? (26'40")
            contentsLayout.setTag(postModel);
            contentsLayout.removeAllViews();                                                                // part14: 다 지웠다가 다시 생성
            thumbnailImageView.setMoreIndex(MORE_INDEX);
            thumbnailImageView.Set_Post_Thumbnail(postModel);
        }
    }

    @Override
    public int getItemViewType(int position){ return position; }

    @Override
    public int getItemCount() {
        return ArrayList_PostModel.size();
    }
}