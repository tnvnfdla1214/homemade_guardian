/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.homemade_guardian_beta.chat.photoview;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.common.ChatUtil;
import com.example.homemade_guardian_beta.chat.model.MessageModel;



//chatfragment에서 roomID와 realname이거 줌
public class ViewPagerActivity extends AppCompatActivity {

	private static String ChatRoomListModel_RoomUid; //룸 uid
	private static String realname; //뭔지 모르겟음
	private static ViewPager viewPager; //뷰페이저
	private static ArrayList<MessageModel> Message_Image_List = new ArrayList<>();

    private String rootPath = ChatUtil.getRootPath()+"/homemade_guardian_beta/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ChatRoomListModel_RoomUid = getIntent().getStringExtra("ChatRoomListModel_RoomUid");
		realname = getIntent().getStringExtra("realname");

		viewPager = findViewById(R.id.view_pager);
		viewPager.setAdapter(new SamplePagerAdapter());

        findViewById(R.id.downloadBtn).setOnClickListener(downloadBtnClickListener);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PhotoView");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	//다운로드 버튼 함수
	Button.OnClickListener downloadBtnClickListener = new View.OnClickListener() {
		public void onClick(final View view) {
            if (!ChatUtil.isPermissionGranted((Activity) view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return ;
            }
			MessageModel messageModel = Message_Image_List.get(viewPager.getCurrentItem());
            /// showProgressDialog("Downloading File.");

			final File localFile = new File(rootPath, messageModel.getMessageModel_FileName());

			// realname == message.msg
			FirebaseStorage.getInstance().getReference().child("files/"+ messageModel.getMessageModel_Message()).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
				@Override
				public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
					// hideProgressDialog();
					ChatUtil.showMessage(view.getContext(), "Downloaded file");
					Log.e("DirectTalk9 ","local file created " +localFile.toString());
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception exception) {
					Log.e("DirectTalk9 ","local file not created  " +exception.toString());
				}
			});
		}
	};

	static class SamplePagerAdapter extends PagerAdapter {
		private StorageReference storageReference;
		private int inx = -1;

		public SamplePagerAdapter() {
			storageReference  = FirebaseStorage.getInstance().getReference();

			FirebaseFirestore.getInstance().collection("ROOMS").document(ChatRoomListModel_RoomUid).collection("MESSAGE").whereEqualTo("Message_MessageType", "1")
					.get()
					.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
						@Override
						public void onComplete(@NonNull Task<QuerySnapshot> task) {
							if (!task.isSuccessful()) { return;}

							for (QueryDocumentSnapshot document : task.getResult()) {
								MessageModel messageModel = document.toObject(MessageModel.class);
								Message_Image_List.add(messageModel);
								if (realname.equals(messageModel.getMessageModel_Message())) {inx = Message_Image_List.size()-1; }
							}
							notifyDataSetChanged();
							if (inx>-1) {
								viewPager.setCurrentItem(inx);
							}
						}
					});
		}

		@Override
		public int getCount() {
			return Message_Image_List.size();
		}

		@Override
		public View instantiateItem(final ViewGroup container, final int position) {
			final PhotoView photoView = new PhotoView(container.getContext());
            photoView.setId(R.id.photoView);

			Glide.with(container.getContext())
					.load(storageReference.child("filesmall/"+ Message_Image_List.get(position).getMessageModel_Message()))
					.into(photoView);

			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
