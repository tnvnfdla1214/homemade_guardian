package com.example.homemade_guardian_beta.Photo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.Photo.entity.PhotoDirectory;
import java.util.ArrayList;
import java.util.List;

// PhotoPickerFragment에서 나타낸 하단부에 이미지들의 디렉토리를 고를 수 있다. 다른 경로의 이미지들을 선택할 수 있게 해준다.

public class PopupDirectoryListAdapter extends BaseAdapter {

  private Context MContext;
  private List<PhotoDirectory> Photodirectory_List = new ArrayList<>();
  private LayoutInflater LayoutInflater;

  public PopupDirectoryListAdapter(Context MContext, List<PhotoDirectory> Photodirectory_List) {
    this.MContext = MContext;
    this.Photodirectory_List = Photodirectory_List;
    LayoutInflater = LayoutInflater.from(MContext);
  }

  @Override
  public int getCount() {
    return Photodirectory_List.size();
  }

  @Override
  public PhotoDirectory getItem(int position) {
    return Photodirectory_List.get(position);
  }

  @Override
  public long getItemId(int position) {
    return Photodirectory_List.get(position).hashCode();
  }

  @Override
  public View getView(int position, View Convertview, ViewGroup Parent) {
    ViewHolder Holder;
    if (Convertview == null) {
      Convertview = LayoutInflater.inflate(R.layout.util_item_directory, Parent, false);
      Holder = new ViewHolder(Convertview);
      Convertview.setTag(Holder);
    } else {
      Holder = (ViewHolder) Convertview.getTag();
    }
    Holder.bindData(Photodirectory_List.get(position));

    return Convertview;
  }

  private class ViewHolder {
    public ImageView IvCover;
    public TextView TvName;
    public TextView TvCount;

    public ViewHolder(View Rootview) {
      IvCover = (ImageView) Rootview.findViewById(R.id.iv_dir_cover);
      TvName = (TextView)  Rootview.findViewById(R.id.tv_dir_name);
      TvCount = (TextView)  Rootview.findViewById(R.id.tv_dir_count);
    }

    //디렉토리의 정보 (커버사진,이름,이미지개수 등)를 set
    public void bindData(PhotoDirectory Directory) {
      if (MContext instanceof Activity && ((Activity) MContext).isFinishing()) { return; }
      Glide.with(MContext)
          .load(Directory.getPhotoDirectory_CoverPath())
          .thumbnail(0.1f)
          .into(IvCover);
      TvName.setText(Directory.getPhotoDirectory_Name());
      TvCount.setText(MContext.getString(R.string.y_photopicker_image_count, Directory.getPhotoList().size()));
    }
  }
}