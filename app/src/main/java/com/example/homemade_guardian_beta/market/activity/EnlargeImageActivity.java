package com.example.homemade_guardian_beta.market.activity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

import com.example.homemade_guardian_beta.Main.activity.BasicActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.market.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import me.relex.circleindicator.CircleIndicator;

//마켓 게시물에서 이미지가 있다면 클릭하여 뷰페이저 형식으로 볼 수 있음
public class EnlargeImageActivity extends BasicActivity {
    private ArrayList<String> ImageList = new ArrayList<>();            // 게시물의 이미지 리스트

    private ViewPager Viewpager;                                        // 이미지들을 보여주기 위한 ViewPager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_imagelist);

        ImageList = (ArrayList<String>) getIntent().getSerializableExtra("marketImage");                // marketActivity에서 받아온 Image들을 ImageList로 저장

        Viewpager = findViewById(R.id.ViewPager);
        Viewpager.setAdapter(new ViewPagerAdapter(this, ImageList, "Disable","Enlarge"));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);                             // 이미지의 개수 대로 이미지 하단에 CircleIndicator를 생성
        indicator.setViewPager(Viewpager);
    }
}