package com.example.homemade_guardian_beta.post.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import java.util.ArrayList;
import static com.example.homemade_guardian_beta.post.PostUtil.INTENT_PATH;

//GalleryActivity와 연결되어 사용되는 어댑터이다.

public class GalleryAdapter  extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private ArrayList<String> ImageList;     //GalleryActivity의 ArrayList<String> getImagesPath에서 return 한 listOfImage를 받는 ArrayList<String>이다.
    private Activity activity;

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        GalleryViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public GalleryAdapter(Activity activity, ArrayList<String> myDataset) {
        ImageList = myDataset;
        this.activity = activity;                                                                       // part9 : 60줄에서 쓸 activity 생성 (11')
    }

    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {   // part9 : 앨범에서 선택한 사진 회원정보 입력화면에 적용 (20'),
        // part10 : 비효율적일 수 있으므로 onBindViewHolder에서 뺐음 (3')
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        final GalleryViewHolder galleryViewHolder = new GalleryViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_PATH, ImageList.get(galleryViewHolder.getAdapterPosition()));
                activity.setResult(Activity.RESULT_OK, resultIntent);
                activity.finish();
            }
        });

        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.imageView);
        Glide.with(activity).load(ImageList.get(position)).centerCrop().override(500).into(imageView);   // part9 :  오픈소스 (10'30") override : 추가적인 resizeing (13') centercrop() : 가운데 정렬 (14')
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }
}