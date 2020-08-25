package com.example.homemade_guardian_beta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.ChatActivity;
import com.example.homemade_guardian_beta.chat.common.FirestoreAdapter;
import com.example.homemade_guardian_beta.chat.model.UserModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//지금은 임시로 우리앱에 들어간 유저들의 정보들이 나옴 -> 해당 유저를 누르면 그사람과 채팅 액티비티가 켜짐
//임시 : (채팅앱 : UserListFragment)
public class MyInfoFragment extends Fragment {
    private FirestoreAdapter firestoreAdapter;

    public MyInfoFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firestoreAdapter != null) {
            firestoreAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firestoreAdapter != null) {
            firestoreAdapter.stopListening();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);
        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("유저 목록 (내 정보)");
        }
        firestoreAdapter = new RecyclerViewAdapter(FirebaseFirestore.getInstance().collection("USERS").orderBy("usernm"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager( new LinearLayoutManager((inflater.getContext())));
        recyclerView.setAdapter(firestoreAdapter);

        return view;
    }

    class RecyclerViewAdapter extends FirestoreAdapter<CustomViewHolder> {
        final private RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        private StorageReference storageReference;
        //현재 들어와 있는 유저 uid 가져오기
        private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerViewAdapter(Query query) {
            super(query);
            storageReference  = FirebaseStorage.getInstance().getReference();
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user, parent, false));
        }

        @Override
        public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
            DocumentSnapshot documentSnapshot = getSnapshot(position);
            final UserModel user = documentSnapshot.toObject(UserModel.class);

            if (myUid.equals(user.getUid())) {
                viewHolder.itemView.setVisibility(View.INVISIBLE);
                viewHolder.itemView.getLayoutParams().height = 0;
                return;
            }
            Log.d("태그1","viewHolder.user_name"+user.getUsernm());
            viewHolder.user_name.setText(user.getUsernm());
            viewHolder.user_msg.setText(user.getUsermsg());

            /*
            if (user.getUserphoto()==null) {
                Glide.with(getActivity()).load(R.drawable.user)
                        .apply(requestOptions)
                        .into(viewHolder.user_photo);
            } else{
                Glide.with(getActivity())
                        .load(storageReference.child("userPhoto/"+user.getUserphoto()))
                        .apply(requestOptions)
                        .into(viewHolder.user_photo);
            }
             */

            if (user.getphotoUrl()!=null) {
                Glide.with(getActivity()).load(user.getphotoUrl()).centerCrop().override(500).into(viewHolder.user_photo);
            } else{
                Glide.with(getActivity()).load(R.drawable.user).into(viewHolder.user_photo);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //chatactivity는 해당 고유uid를 가진 사람을 찾아 그사람과의 채팅방을 들어감
                    Intent intent = new Intent(getView().getContext(), ChatActivity.class);
                    intent.putExtra("toUid", user.getUid());
                    startActivity(intent);
                }
            });

        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView user_photo;
        public TextView user_name;
        public TextView user_msg;

        CustomViewHolder(View view) {
            super(view);
            user_photo = view.findViewById(R.id.user_photo);
            user_name = view.findViewById(R.id.user_name);
            user_msg = view.findViewById(R.id.user_msg);
        }
    }

}
