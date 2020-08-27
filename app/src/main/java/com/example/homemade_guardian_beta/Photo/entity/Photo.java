package com.example.homemade_guardian_beta.Photo.entity;

// 이미지의 모델이다.

public class Photo {

  private int Photo_Id;
  private String Photo_Path;

  public Photo(int Photo_Id, String Photo_Path) {
    this.Photo_Id = Photo_Id;
    this.Photo_Path = Photo_Path;
  }

  public Photo() { }

  //선택된 이미지와 이벤트를 실행하려는 이미지의 일치 여부를 확인하는 함수
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Photo)) return false;
    Photo photo = (Photo) o;
    return Photo_Id == photo.Photo_Id;
  }

  @Override public int hashCode() {
    return Photo_Id;
  }

  public String getPhoto_Path() {
    return Photo_Path;
  }
  public void setPhoto_Path(String photo_Path) {
    this.Photo_Path = photo_Path;
  }
  public int getPhoto_Id() {
    return Photo_Id;
  }
  public void setPhoto_Id(int photo_Id) {
    this.Photo_Id = photo_Id;
  }
}
