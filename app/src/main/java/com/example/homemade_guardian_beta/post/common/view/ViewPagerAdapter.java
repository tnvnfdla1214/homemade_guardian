package com.example.homemade_guardian_beta.post.common.view;

import android.content.Context;
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
    private ArrayList<String> ArrayList_ImageList = new ArrayList<String>();      //받아온 이미지리스트
    private Context Context;

    public ViewPagerAdapter(Context Context, ArrayList<String> ArrayList_ImageList)
    {
        this.Context = Context;
        this.ArrayList_ImageList = ArrayList_ImageList;
    }

    //로그를 찍어본다면 이미 이미지의 정보를 ArrayList_ImageList에 갖고 왔으므로 Glide로 생성만 해준다.
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater Inflater = (LayoutInflater) Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Inflater.inflate(R.layout.post_silder, null);
        ImageView Post_ImageView = view.findViewById(R.id.PostActivity_Post_ImageView);
        Glide.with(view).load(ArrayList_ImageList.get(position)).into(Post_ImageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return ArrayList_ImageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) { container.removeView((View)object); }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) { return (view == (View)o); }

}