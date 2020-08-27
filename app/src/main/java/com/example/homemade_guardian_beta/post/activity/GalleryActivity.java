package com.example.homemade_guardian_beta.post.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.adapter.GalleryAdapter;
import java.util.ArrayList;
import static com.example.homemade_guardian_beta.post.PostUtil.GALLERY_IMAGE;
import static com.example.homemade_guardian_beta.post.PostUtil.GALLERY_VIDEO;
import static com.example.homemade_guardian_beta.post.PostUtil.INTENT_MEDIA;
import static com.example.homemade_guardian_beta.post.PostUtil.showToast;

//이미지 리스트에 넣을 때와는 달리 이미지의 다중 선택을 불허하고, 사용자의 프로필 사진의 선택을 할 때에 실행되는 액티비티이다.
//      Ex) MemberInitActivity에서 사진을 고를 때에 사용된다. <-> GalleryAdapter와 연결된다.

public class GalleryActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                             // part8 : 갤러리를 불러올 때 (16'40")
        setContentView(R.layout.activity_gallery);
        setToolbarTitle("갤러리");

        if (ContextCompat.checkSelfPermission(GalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {   // part8 : (A) 갤러리 권한 받기 (30')
            ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);              // part8 : (B) 권한을 줘서 requestCode가 1이 되면
            if (ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) { }
            else { showToast(GalleryActivity.this, getResources().getString(R.string.please_grant_permission));}   // part18 : strings에 메시지 관리하기
        }
        else { recyclerInit(); }
    }

    @Override                                                                                           // part8 : (C) 그 requestCode를 받아서 onRequestPermissionsResult
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {   //(33')
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { recyclerInit();}
                else {
                    finish();
                    showToast(GalleryActivity.this, getResources().getString(R.string.please_grant_permission));
                }
            }
        }
    }

    // 이미지들을 담을 RecyclerView 설정
    private void recyclerInit(){
        final int numberOfColumns = 3;                                                                  // part9 : (E) recyclerInit()는 사진을 3개씩 나열 (2')
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        RecyclerView.Adapter mAdapter = new GalleryAdapter(this, GetImagePath(this));
        recyclerView.setAdapter(mAdapter);
    }

    // 아이콘 이미지 생성
    public ArrayList<String> GetImagePath(Activity activity) {

        ArrayList<String> ListOfImage = new ArrayList<String>();            //PathOfImage의 값을 넣어서 전달하는 ArrayList<String>
        String PathOfImage = null;                                      //커서로 선택한 이미지의 Uri를 String의 값으로 받으려는 변수
        Cursor CursorEvent;                                             //이미지를 선택했다는 이벤트의 값

        Uri URI;                                                        //선택한 이미지의 Uri
        String[] projection;
        int Column_Index_Data;
        Intent ImageIntent = getIntent();
        final int Media = ImageIntent.getIntExtra(INTENT_MEDIA, GALLERY_IMAGE);                              // part11 : 비디오면 비디오 / 이미지면 이미지 리스트 반환

        if(Media == GALLERY_VIDEO){
            URI = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME };
        }else{
            URI = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        }
        CursorEvent = activity.getContentResolver().query(URI, projection, null, null, null);
        Column_Index_Data = CursorEvent.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (CursorEvent.moveToNext()) {
            PathOfImage = CursorEvent.getString(Column_Index_Data);
            ListOfImage.add(PathOfImage);
        }
        return ListOfImage;
    }
}
