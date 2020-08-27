package com.example.homemade_guardian_beta.Photo.entity;

import java.util.ArrayList;
import java.util.List;

// 디렉토리의 모델이다.

public class PhotoDirectory {

  private String PhotoDirectory_Id;
  private String PhotoDirectory_CoverPath;
  private String PhotoDirectory_Name;
  private long PhotoDirectory_Dateadded;
  private List<Photo> PhotoList = new ArrayList<>();

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PhotoDirectory)) return false;

    PhotoDirectory directory = (PhotoDirectory) o;

    if (!PhotoDirectory_Id.equals(directory.PhotoDirectory_Id)) return false;
    return PhotoDirectory_Name.equals(directory.PhotoDirectory_Name);
  }

  @Override public int hashCode() {
    int result = PhotoDirectory_Id.hashCode();
    result = 31 * result + PhotoDirectory_Name.hashCode();
    return result;
  }

  public String getPhotoDirectory_Id() {
    return PhotoDirectory_Id;
  }
  public void setPhotoDirectory_Id(String photoDirectory_Id) { this.PhotoDirectory_Id = photoDirectory_Id; }
  public String getPhotoDirectory_CoverPath() {
    return PhotoDirectory_CoverPath;
  }
  public void setPhotoDirectory_CoverPath(String photoDirectory_CoverPath) { this.PhotoDirectory_CoverPath = photoDirectory_CoverPath; }
  public String getPhotoDirectory_Name() {
    return PhotoDirectory_Name;
  }
  public void setPhotoDirectory_Name(String photoDirectory_Name) { this.PhotoDirectory_Name = photoDirectory_Name; }
  public long getPhotoDirectory_Dateadded() {
    return PhotoDirectory_Dateadded;
  }
  public void setPhotoDirectory_Dateadded(long photoDirectory_Dateadded) { this.PhotoDirectory_Dateadded = photoDirectory_Dateadded; }
  public List<Photo> getPhotoList() {
    return PhotoList;
  }
  public void setPhotoList(List<Photo> photoList) {
    this.PhotoList = photoList;
  }
  public List<String> getPhotoPaths() {
    List<String> paths = new ArrayList<>(PhotoList.size());
    for (Photo photo : PhotoList) {
      paths.add(photo.getPhoto_Path());
    }
    return paths;
  }
  public void addPhoto(int id, String path) {
    PhotoList.add(new Photo(id, path));
  }
}
