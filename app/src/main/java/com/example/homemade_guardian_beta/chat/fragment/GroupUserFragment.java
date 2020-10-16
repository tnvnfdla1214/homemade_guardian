package com.example.homemade_guardian_beta.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.activity.SelectUserActivity;
import com.example.homemade_guardian_beta.model.user.UserModel;

//그룹채팅 프래그먼트
//우리 그룹채팅 안할거니까 빼야할 프래그먼트
public class GroupUserFragment extends Fragment {
    private String ChatRoomListModel_RoomUid;  //room의 Uid
    private List<UserModel> Userlist; //유저모델 리스트
    private RecyclerView recyclerView; //리사클러뷰

    public GroupUserFragment() {
    }

    public static final GroupUserFragment getInstance(String roomID, Map<String, UserModel> userModels) {
        List<UserModel> UserModellist = new ArrayList();
        for( Map.Entry<String, UserModel> elem : userModels.entrySet() ){
            UserModellist.add(elem.getValue());
        }

        GroupUserFragment groupUserFragment = new GroupUserFragment();
        groupUserFragment.setUserList(UserModellist);
        Bundle bdl = new Bundle();
        bdl.putString("roomID", roomID);
        groupUserFragment.setArguments(bdl);

        return groupUserFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userlistinroom, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager( new LinearLayoutManager((inflater.getContext())));
        recyclerView.setAdapter(new UserFragmentRecyclerViewAdapter());

        view.findViewById(R.id.Group_Chat_Add_Button).setOnClickListener(Group_Chat_Add_Button_ClickListener);


        if (getArguments() != null) {
            ChatRoomListModel_RoomUid = getArguments().getString("ChatRoomListModel_RoomUid");
        }
        return view;
    }

    //유저 추가 버튼 함수
    Button.OnClickListener Group_Chat_Add_Button_ClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), SelectUserActivity.class);
            intent.putExtra("ChatRoomListModel_RoomUid", ChatRoomListModel_RoomUid);
            startActivity(intent);
        }
    };



    public void setUserList(List<UserModel> users) {
        Userlist = users;
    }

    class UserFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private StorageReference storageReference;
        final private RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));

        public UserFragmentRecyclerViewAdapter() {
            storageReference  = FirebaseStorage.getInstance().getReference();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final UserModel user = Userlist.get(position);
            CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            customViewHolder.user_name.setText(user.getUserModel_NickName());
            if (user.getUserModel_ProfileImage()!=null) {
                Glide.with(getActivity()).load(user.getUserModel_ProfileImage()).centerCrop().override(500).into(customViewHolder.user_photo);
            } else{
                Glide.with(getActivity()).load(R.drawable.user).into(customViewHolder.user_photo);
            }


        }

        @Override
        public int getItemCount() {
            return Userlist.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView user_photo;
        public TextView user_name;
        public TextView user_msg;

        public CustomViewHolder(View view) {
            super(view);
            user_photo = view.findViewById(R.id.User_Profile_Imalge);
            user_name = view.findViewById(R.id.user_name);
            user_msg = view.findViewById(R.id.user_msg);
            user_msg.setVisibility(View.GONE);
        }
    }
}
