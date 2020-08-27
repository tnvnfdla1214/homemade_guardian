package com.example.homemade_guardian_beta.Photo.adapter;

import androidx.recyclerview.widget.RecyclerView;
import com.example.homemade_guardian_beta.Photo.entity.Photo;
import com.example.homemade_guardian_beta.Photo.entity.PhotoDirectory;
import com.example.homemade_guardian_beta.Photo.event.Selectable;
import java.util.ArrayList;
import java.util.List;

//RecyclerView에 Utill처럼 Photo package에서만 사용 할 기능을 추가하여 쓰기 위해 만든 추상 클래스이다.

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> implements Selectable {

  private static final String TAG = SelectableAdapter.class.getSimpleName();

  protected List<PhotoDirectory> PhotoDirectory_List;
  protected List<Photo> SelectedPhoto_List;
  public int CurrentDirectoryIndex = 0;

  public SelectableAdapter() {
    PhotoDirectory_List = new ArrayList<>();
    SelectedPhoto_List = new ArrayList<>();
  }

  /**
   * Indicates if the item at position position is selected
   * @param photo Photo of the item to check
   * @return true if the item is selected, false otherwise
   */
  @Override public boolean isSelected(Photo photo) { return getSelectedPhoto_List().contains(photo); }

  /**
   * Toggle the selection status of the item at a given position
   *
   * @param photo Photo of the item to toggle the selection status for
   */
  @Override public void toggleSelection(Photo photo) {
    if (SelectedPhoto_List.contains(photo)) {
      SelectedPhoto_List.remove(photo);
    } else {
      SelectedPhoto_List.add(photo);
    }
  }

  /**
   * Clear the selection status for all items
   */
  @Override public void clearSelection() {
    SelectedPhoto_List.clear();
  }


  /**
   * Count the selected items
   *
   * @return Selected items count
   */
  @Override public int getSelectedItemCount() {
    return SelectedPhoto_List.size();
  }

  public void setCurrentDirectoryIndex(int currentDirectoryIndex) { this.CurrentDirectoryIndex = currentDirectoryIndex; }

  public List<Photo> getCurrentPhotos() { return PhotoDirectory_List.get(CurrentDirectoryIndex).getPhotoList(); }

  public List<String> getCurrentPhotoPaths() {
    List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
    for (Photo photo : getCurrentPhotos()) {
      currentPhotoPaths.add(photo.getPhoto_Path());
    }
    return currentPhotoPaths;
  }

  @Override public List<Photo> getSelectedPhoto_List() {
    return SelectedPhoto_List;
  }
}