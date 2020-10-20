package com.example.homemade_guardian_beta.community.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;

import java.util.ArrayList;

import static com.example.homemade_guardian_beta.market.MarketUtil.INTENT_PATH;

//GalleryActivity와 연결되어 사용되는 어댑터이다.

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private ArrayList<String> ArrayList_ImageList;     //GalleryActivity의 ArrayList<String> getImagesPath에서 return 한 listOfImage를 받는 ArrayList<String>이다.
    private Activity Activity;

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        CardView Cardview;
        GalleryViewHolder(CardView v) {
            super(v);
            Cardview = v;
        }
    }

    public GalleryAdapter(Activity activity, ArrayList<String> myDataset) {
        ArrayList_ImageList = myDataset;
        this.Activity = activity;                                                                       // part9 : 60줄에서 쓸 activity 생성 (11')
    }

    //앨범에서 불러온 사진을 화면에 나타내는 Holder
    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {   // part9 : 앨범에서 선택한 사진 회원정보 입력화면에 적용 (20'),
        // part10 : 비효율적일 수 있으므로 onBindViewHolder에서 뺐음 (3')
        CardView Cardview = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        final GalleryViewHolder Galleryviewholder = new GalleryViewHolder(Cardview);
        Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Resultintent = new Intent();
                Resultintent.putExtra(INTENT_PATH, ArrayList_ImageList.get(Galleryviewholder.getAdapterPosition()));
                Activity.setResult(Activity.RESULT_OK, Resultintent);
                Activity.finish();
            }
        });

        return Galleryviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {
        CardView Cardview = holder.Cardview;
        ImageView Imageview = Cardview.findViewById(R.id.Directory_Image);
        Glide.with(Activity).load(ArrayList_ImageList.get(position)).centerCrop().override(500).into(Imageview);   // part9 :  오픈소스 (10'30") override : 추가적인 resizeing (13') centercrop() : 가운데 정렬 (14')
    }

    @Override
    public int getItemCount() {
        return ArrayList_ImageList.size();
    }
}