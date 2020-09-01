package com.example.homemade_guardian_beta.Photo.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.Photo.entity.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static com.example.homemade_guardian_beta.Photo.PhotoPickerActivity.EXTRA_SHOW_GIF;

//?

public class MediaStoreHelper {
    public final static int INDEX_ALL_PHOTOS = 0;

    public static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context Context;
        private PhotosResultCallback ResultCallback;

        public PhotoDirLoaderCallbacks(Context Context, PhotosResultCallback ResultCallback) {
            this.Context = Context;
            this.ResultCallback = ResultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int Id, Bundle Args) { return new PhotoDirectoryLoader(Context, Args.getBoolean(EXTRA_SHOW_GIF, false)); }

        @Override
        public void onLoadFinished(Loader<Cursor> Loader, Cursor Data) {

            if (Data == null) return;
            List<PhotoDirectory> Directory_List = new ArrayList<>();
            PhotoDirectory PhotoDirectoryAll = new PhotoDirectory();
            PhotoDirectoryAll.setPhotoDirectory_Name(Context.getString(R.string.y_photopicker_all_image));
            PhotoDirectoryAll.setPhotoDirectory_Id("ALL");

            while (Data.moveToNext()) {

                int ImageId = Data.getInt(Data.getColumnIndexOrThrow(_ID));
                String BucketId = Data.getString(Data.getColumnIndexOrThrow(BUCKET_ID));
                String Name = Data.getString(Data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String Path = Data.getString(Data.getColumnIndexOrThrow(DATA));

                PhotoDirectory Photodirectory = new PhotoDirectory();
                Photodirectory.setPhotoDirectory_Id(BucketId);
                Photodirectory.setPhotoDirectory_Name(Name);

                if (!Directory_List.contains(Photodirectory)) {
                    Photodirectory.setPhotoDirectory_CoverPath(Path);
                    Photodirectory.addPhoto(ImageId, Path);
                    Photodirectory.setPhotoDirectory_Dateadded(Data.getLong(Data.getColumnIndexOrThrow(DATE_ADDED)));
                    Directory_List.add(Photodirectory);
                } else {
                    Directory_List.get(Directory_List.indexOf(Photodirectory)).addPhoto(ImageId, Path);
                }
                PhotoDirectoryAll.addPhoto(ImageId, Path);
            }
            if (PhotoDirectoryAll.getPhotoPaths().size() > 0) {
                PhotoDirectoryAll.setPhotoDirectory_CoverPath(PhotoDirectoryAll.getPhotoPaths().get(0));
            }
            Directory_List.add(INDEX_ALL_PHOTOS, PhotoDirectoryAll);
            if (ResultCallback != null) {
                ResultCallback.onResultCallback(Directory_List);
            }
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) { }
    }

    public interface PhotosResultCallback { void onResultCallback(List<PhotoDirectory> directories);}
}
