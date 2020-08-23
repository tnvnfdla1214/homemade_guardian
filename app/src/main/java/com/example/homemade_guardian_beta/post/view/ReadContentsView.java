package com.example.homemade_guardian_beta.post.view;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.post.PostInfo;
import com.example.homemade_guardian_beta.R;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.source.ProgressiveMediaSource;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
//import com.google.android.exoplayer2.video.VideoListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//게시물 보여주는 함수
public class ReadContentsView extends LinearLayout {
    private Context context;
    private LayoutInflater layoutInflater;
    //private ArrayList<SimpleExoPlayer> playerArrayList = new ArrayList<>();
    private int moreIndex = -1;

    public ReadContentsView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ReadContentsView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView();
    }

    private void initView(){
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_post, this, true);                                                 // part19 : view_post는 게시물 클릭시 페이지, item_post는 메인에서 나열된 게시물
        //addView(layoutInflater.inflate(R.layout.view_post, this, true));                                          // part19 : merge를 사용하면 직접 실행 (45')
    }

    public void setMoreIndex(int moreIndex){
        this.moreIndex = moreIndex;                                                                                 // part19 : 어댑터에서도 쓸 수 있게 (46'50")
    }

    public void setPostInfo(PostInfo postInfo){
                // part19 : setPostInfo 작성 (34'20")
        TextView createdAtTextView = findViewById(R.id.createAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(postInfo.getCreatedAt()));
        LinearLayout contentsLayout = findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = postInfo.getContents();
        for (int i = 0; i < contentsList.size(); i++) {                                                 // part17 : 더보기기능 추가
            if (i == moreIndex) {
                TextView textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText("더보기...");
                contentsLayout.addView(textView);
                break;
            }
            String contents = contentsList.get(i);
//            if(formats.equals("image")){                                                                            // part20 : 컨텐츠의 정보에 따라 다르게 만듦 (12')
            ImageView imageView = (ImageView)layoutInflater.inflate(R.layout.view_contents_image, this, false);
            contentsLayout.addView(imageView);
            Glide.with(this).load(contents).override(1000).thumbnail(0.1f).into(imageView);         // 흐릿하게 로딩하기
//            }
//
//        else{
//                TextView textView = (TextView) layoutInflater.inflate(R.layout.view_contents_text, this, false);
//                textView.setText(contents);
//                contentsLayout.addView(textView);
//            }
        }

    } public void setPostPageInfo(PostInfo postInfo){                                                                                     // part19 : setPostInfo 작성 (34'20")


        LinearLayout contentsLayout = findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = postInfo.getContents();

        for (int i = 0; i < contentsList.size(); i++) {                                                 // part17 : 더보기기능 추가
            if (i == moreIndex) {
                TextView textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText("더보기...");
                contentsLayout.addView(textView);
                break;
            }

            String contents = contentsList.get(i);

//            if(formats.equals("image")){                                                                            // part20 : 컨텐츠의 정보에 따라 다르게 만듦 (12')
                ImageView imageView = (ImageView)layoutInflater.inflate(R.layout.view_contents_image, this, false);
                contentsLayout.addView(imageView);
                Glide.with(this).load(contents).override(1000).thumbnail(0.1f).into(imageView);         // 흐릿하게 로딩하기
//            }

//            else{
//                TextView textView = (TextView) layoutInflater.inflate(R.layout.view_contents_text, this, false);
//                textView.setText(contents);
//                contentsLayout.addView(textView);
//            }
        }

    }


//    public ArrayList<SimpleExoPlayer> getPlayerArrayList() {
//        return playerArrayList;
//    }
}
