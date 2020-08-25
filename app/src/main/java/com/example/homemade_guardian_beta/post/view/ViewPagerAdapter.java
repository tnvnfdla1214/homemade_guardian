package com.example.homemade_guardian_beta.post.view;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import java.util.ArrayList;

//뷰페이져에서 이미지리스트의 string으로 저장된 이미지들을 imageList에 넣어서 PostActivity에서 슬라이드하여 이미지들을 볼 수 있게 해준다.
//      Ex) 22번째 줄 ViewPagerAdapter가 PostActivity와 연결된다.
public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<String> imageList = new ArrayList<String>();      //받아온 이미지리스트
    private Context mContext;

    public ViewPagerAdapter(Context context, ArrayList<String> imageList)
    {
        this.mContext = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.post_silder, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(view).load(imageList.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) { container.removeView((View)object); }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) { return (view == (View)o); }
}