package com.example.homemade_guardian_beta.Photo.event;


import com.example.homemade_guardian_beta.Photo.entity.Photo;

public interface OnItemCheckListener {

  /***
   *
   * @param position Select Image postion
   * @param path     Select image path
   *@param isCheck   Image status
   * @param selectedItemCount  Select image count
   * @return enable check
   */
  boolean OnItemCheck(int position, Photo path, boolean isCheck, int selectedItemCount);

}
