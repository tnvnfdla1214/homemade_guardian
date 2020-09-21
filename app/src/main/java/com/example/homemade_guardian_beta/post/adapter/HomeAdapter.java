package com.example.homemade_guardian_beta.post.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.homemade_guardian_beta.model.post.PostModel;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.activity.PostActivity;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//HomeFragment와 연결된 어댑터이다. onBindViewHolder로 카드뷰에 게시물의 정보들을 담는 역할을 한다.
//      Ex) MORE_INDEX를 readContentsView에서 MORE_INDEX 보다 작은 수의 사진만 MainFragment에서 나타낸다.

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MainViewHolder> {
    private ArrayList<PostModel> ArrayList_PostModel;       //게시물에 담을 PostModel의 정보들을 담는다.
    private Activity Activity;

    private final int MORE_INDEX = 1;                       //게시물에서 미리 표현할 사진의 개수

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView Cardview;
        MainViewHolder(CardView v) {
            super(v);
            Cardview = v;
        }
    }

    public HomeAdapter(Activity activity, ArrayList<PostModel> ArrayList_PostModel) {
        this.ArrayList_PostModel = ArrayList_PostModel;
        this.Activity = activity;
    }

    //POSTS에서 불러온 게시물 내용을 화면에 나타내는 Holder
    @NonNull
    @Override
    public HomeAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {             // part : 게시물을 눌렀을 떄
        CardView Cardview = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final MainViewHolder Mainviewholder = new MainViewHolder(Cardview);
        Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                                   // part18 : 게시물 클릭시 게시물페이지로 이동 (36'10")
                Intent Intent_PostActivity = new Intent(Activity, PostActivity.class);
                Intent_PostActivity.putExtra("postInfo", ArrayList_PostModel.get(Mainviewholder.getAdapterPosition()));
                Activity.startActivity(Intent_PostActivity);
            }
        });
        return Mainviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {                      // part : 게시물을 나열
        //final RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(this.Activity, R.drawable.image_round);
        CardView Contents_CardView = holder.Cardview;
        TextView Title_TextView = Contents_CardView.findViewById(R.id.Post_Title_TextView);
        TextView Contents_TextView = Contents_CardView.findViewById(R.id.Post_Contents_TextView);
        TextView Post_Category = Contents_CardView.findViewById(R.id.Post_Category);
        TextView Post_LikeCount = Contents_CardView.findViewById(R.id.Post_LikeCount);
        PostModel Postmodel = ArrayList_PostModel.get(position);                                                         //HomeFragment에서 PostInfo(mDaset)에 넣은 데이터 get
        Title_TextView.setText(Postmodel.getPostModel_Title());
        Contents_TextView.setText(Postmodel.getPostModel_Text());
        Post_Category.setText(Postmodel.getPostModel_Category());
        Post_LikeCount.setText(String.valueOf(Postmodel.getPostModel_LikeList().size()));
                         //contentsLayout에다가 날짜포함
        LinearLayout Contentslayout = Contents_CardView.findViewById(R.id.contentsLayout);                      /////////////////////이거 대신 텍스트 만든거 보여주기로
        TextView DateOfManufacture_TextView = Contents_CardView.findViewById(R.id.Post_DateOfManufacture);
        DateOfManufacture_TextView.setText(new SimpleDateFormat("MM/dd hh:mm", Locale.getDefault()).format(Postmodel.getPostModel_DateOfManufacture()));

        ArrayList<String> ArrayList_ImageList = Postmodel.getPostModel_ImageList();
        if(ArrayList_ImageList != null) {
            Log.d("로그","썸네일 있");
            ImageView Thumbnail_ImageView = Contents_CardView.findViewById(R.id.Post_ImageView);
            Thumbnail_ImageView.setVisibility(View.VISIBLE);
            Thumbnail_ImageView.setBackground(drawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Thumbnail_ImageView.setClipToOutline(true);
            }
            String Image = ArrayList_ImageList.get(0);
            Glide.with(Activity).load(Image).centerCrop().override(500).thumbnail(0.1f).into(Thumbnail_ImageView);
        }else {
            Log.d("로그","썸네일 없");
            ImageView Thumbnail_ImageView = Contents_CardView.findViewById(R.id.Post_ImageView);
            Log.d("로그","썸네일 없");
            Thumbnail_ImageView.setVisibility(View.GONE);
            Log.d("로그","썸네일 없");
        }



//        if (Contentslayout.getTag() == null || !Contentslayout.getTag().equals(Postmodel)) {                 // part16 : 게시물 개수에 변화가 있을 때만 실행..? (26'40")
//            Contentslayout.setTag(Postmodel);
//            Contentslayout.removeAllViews();                                                                // part14: 다 지웠다가 다시 생성
//            Thumbnail_ImageView.setMoreIndex(MORE_INDEX);
//            Thumbnail_ImageView.Set_Post_Thumbnail(Postmodel);
//        }

    }

    @Override
    public int getItemViewType(int position){ return position; }

    @Override
    public int getItemCount() {
        return ArrayList_PostModel.size();
    }
}