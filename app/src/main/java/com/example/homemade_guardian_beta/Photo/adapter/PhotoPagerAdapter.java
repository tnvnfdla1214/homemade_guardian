package com.example.homemade_guardian_beta.Photo.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.Photo.PhotoPickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// ImagePagerFragment와 연결되어 있고, 이미지를 크게 보기 위한 데에 필요한 Adapter이다.
//    (PhotoPickerActivity) -> (PhotoPickerFragment) -> (PhotoGridAdapter)
//                        ↘ (ImagePagerFragment)  -> PhotoPagerAdapter

public class PhotoPagerAdapter extends PagerAdapter {

  private List<String> paths = new ArrayList<>();
  private Context mContext;
  private LayoutInflater mLayoutInflater;


  public PhotoPagerAdapter(Context mContext, List<String> paths) {
    this.mContext = mContext;
    this.paths = paths;
    mLayoutInflater = LayoutInflater.from(mContext);
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {

    View itemView = mLayoutInflater.inflate(R.layout.util_item_pager, container, false);

    ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);

    final String path = paths.get(position);
    final Uri uri;
    if (path.startsWith("http")) {
      uri = Uri.parse(path);
    } else {
      uri = FileProvider.getUriForFile(mContext, "com.example.homemade_guardian_beta.provider", new File(path));
    }

    Glide.with(mContext)
            .load(uri)
            .thumbnail(0.4f)
            .apply(new RequestOptions()
                    .placeholder(R.color.img_loding_placeholder)
                    .error(R.drawable.ic_place_holder))
            .into(imageView);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (mContext instanceof PhotoPickerActivity) {
          if (!((Activity) mContext).isFinishing()) {
            ((Activity) mContext).onBackPressed();
          }
        }
      }
    });

    container.addView(itemView);

    return itemView;
  }


  @Override public int getCount() {
    return paths.size();
  }


  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override
  public int getItemPosition (Object object) { return POSITION_NONE; }

}
