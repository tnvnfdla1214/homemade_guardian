package com.example.homemade_guardian_beta.Photo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import com.example.homemade_guardian_beta.Photo.PhotoPickerActivity;

public class PhotoUtil extends Intent implements Parcelable {

  private PhotoUtil() { }

  public PhotoUtil(Context packageContext) { super(packageContext, PhotoPickerActivity.class); }

  public void setMaxSelectCount(int photoCount) { this.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, photoCount); }

  public void setShowCamera(boolean showCamera) { this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera); }

  public void setShowGif(boolean showGif) { this.putExtra(PhotoPickerActivity.EXTRA_SHOW_GIF, showGif); }

  public void setMaxGrideItemCount(int grideCount){ this.putExtra(PhotoPickerActivity.EXTRA_MAX_GRIDE_ITEM_COUNT, grideCount); }

  public void setSelectCheckBox(boolean isCheckbox){ this.putExtra(PhotoPickerActivity.EXTRA_CHECK_BOX_ONLY, isCheckbox); }

}
