package com.example.homemade_guardian_beta.post.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.PostModel;
import com.example.homemade_guardian_beta.post.activity.PostActivity;
import com.example.homemade_guardian_beta.post.view.ThumbnailImageView;
import java.util.ArrayList;

//SearchResultFragment와 연결된 어댑터이다. onBindViewHolder로 카드뷰에 검색된 게시물의 정보들을 담는 역할을 한다. !검색된 결과의 게시물 나열!
//      Ex) MORE_INDEX를 readContentsView에서 MORE_INDEX 보다 작은 수의 사진만 SearchResultFragment에서 나타낸다.

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MainViewHolder> {
    private ArrayList<PostModel> ArrayList_PostModel;   //게시물에 담을 PostModel의 정보들을 담는다.
    private Activity activity;

    private final int MORE_INDEX = 1;                   //게시물에서 미리 표현할 사진의 개수

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView Cardview;
        MainViewHolder(CardView v) {
            super(v);
            Cardview = v;
        }
    }

    public SearchResultAdapter(Activity activity, ArrayList<PostModel> ArrayList_Postmodel) {
        this.ArrayList_PostModel = ArrayList_Postmodel;
        this.activity = activity;
    }

    //검색한 결과를 POSTS에서 불러온 게시물 내용으로 화면에 나타내는 Holder
    @NonNull
    @Override
    public SearchResultAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {             // part : 게시물을 눌렀을 떄
        CardView Cardview = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final MainViewHolder Mainviewholder = new MainViewHolder(Cardview);
        Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                                   // part18 : 게시물 클릭시 게시물페이지로 이동 (36'10")
                Intent Intent_PostActivity = new Intent(activity, PostActivity.class);
                //postInfo 안에 uid있음
                Intent_PostActivity.putExtra("postInfo", ArrayList_PostModel.get(Mainviewholder.getAdapterPosition()));
                activity.startActivity(Intent_PostActivity);
            }
        });
        return Mainviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {                      // part : 게시물을 나열
        CardView Cardview = holder.Cardview;
        TextView Title_TextView = Cardview.findViewById(R.id.titleTextView);
        PostModel Postmodel = ArrayList_PostModel.get(position);                                                         //HomeFragment에서 PostInfo(mDaset)에 넣은 데이터 get
        Title_TextView.setText(Postmodel.getPostModel_Title());
        ThumbnailImageView Thumbnail_ImageView = Cardview.findViewById(R.id.readContentsView);                   //contentsLayout에다가 날짜포함
        LinearLayout Contentslayout = Cardview.findViewById(R.id.contentsLayout);

        if (Contentslayout.getTag() == null || !Contentslayout.getTag().equals(Postmodel)) {                 // part16 : 게시물 개수에 변화가 있을 때만 실행..? (26'40")
            Contentslayout.setTag(Postmodel);
            Contentslayout.removeAllViews();                                                                // part14: 다 지웠다가 다시 생성
            Thumbnail_ImageView.setMoreIndex(MORE_INDEX);                                                      // part19 : 위에서 두개 까지만 표시
            Thumbnail_ImageView.Set_Post_Thumbnail(Postmodel);
        }
    }

    @Override
    public int getItemViewType(int position){ return position; }

    @Override
    public int getItemCount() {
        return ArrayList_PostModel.size();
    }
}