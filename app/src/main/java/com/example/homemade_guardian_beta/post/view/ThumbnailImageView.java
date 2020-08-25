package com.example.homemade_guardian_beta.post.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.post.PostModel;
import com.example.homemade_guardian_beta.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//MainFragment에서 이미지를 썸네일로써 한장만 보여주고 2장 이상일경우 "더보기"로 구현

public class ThumbnailImageView extends LinearLayout {
    private LayoutInflater layoutInflater;
    private Context context;

    private int moreIndex = -1;             //썸네일에 사진 1장 만을 만들기 위한 인덱스

    public ThumbnailImageView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ThumbnailImageView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView();
    }

    private void initView(){
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_post, this, true);                                                 // part19 : view_post는 게시물 클릭시 페이지, item_post는 메인에서 나열된 게시물
    }

    public void setMoreIndex(int moreIndex){ this.moreIndex = moreIndex;}                                                                          // part19 : 어댑터에서도 쓸 수 있게 (46'50") }

    public void Set_Post_Thumbnail(PostModel postModel){
        TextView DateOfManufacture = findViewById(R.id.createAtTextView);
        DateOfManufacture.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(postModel.getPostModel_DateOfManufacture()));
        LinearLayout Thumbnail_Layout = findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> Post_ImageList = postModel.getPostModel_ImageList();
        for (int i = 0; i < Post_ImageList.size(); i++) {                                                 // part17 : 더보기기능 추가
            if (i == moreIndex) {
                TextView textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText("더보기...");
                Thumbnail_Layout.addView(textView);
                break;
            }
            String Image = Post_ImageList.get(i);                                                                    // part20 : 컨텐츠의 정보에 따라 다르게 만듦 (12')
            ImageView imageView = (ImageView)layoutInflater.inflate(R.layout.view_contents_image, this, false);
            Thumbnail_Layout.addView(imageView);
            Glide.with(this).load(Image).override(1000).thumbnail(0.1f).into(imageView);         // 흐릿하게 로딩하기
        }
    } 
}
