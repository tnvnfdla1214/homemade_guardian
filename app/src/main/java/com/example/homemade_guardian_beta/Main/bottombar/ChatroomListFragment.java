package com.example.homemade_guardian_beta.Main.bottombar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;


import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.Main.common.FirebaseHelper;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.activity.ChatActivity;
import com.example.homemade_guardian_beta.chat.common.ItemTouchHelperCallback;
import com.example.homemade_guardian_beta.chat.common.ItemTouchHelperListener;
import com.example.homemade_guardian_beta.model.chat.ChatRoomListModel;
import com.example.homemade_guardian_beta.model.chat.MessageModel;
import com.example.homemade_guardian_beta.model.chat.RoomModel;
import com.example.homemade_guardian_beta.model.user.UserModel;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

//참고 : adapter란 데이터 테이블을 목록 상태로 보여주기 위해 사용되는것
//데이터를 다양한 형식의 리스트 형식으로 보여주기 위해서 데이터와 리스트 뷰 사이에 존재하는 객체
//users - (token,uid:고유번호,user id : "tnvnfdla1214@naver.com", usermsg : "....",userm : "tnvnfdla1214",userphoto : null)
//지금까지 채팅했던거 보여주는 프레그먼트 액티비티 -(채팅앱 : ChatroomFragment)
public class ChatroomListFragment extends Fragment {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerViewAdapter mAdapter;
    ItemTouchHelper helper;
    private FirebaseHelper Firebasehelper;          //FirebaseHelper 참조 선언
    String RoomUid;                                 //해당 포지션에 따른 룸 uid
    String ToUserUid;                               // 해당 포지션에 따른 상대방 uid

    public ChatroomListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroomlist, container, false);

        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("채팅 목록");
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        mAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);

        //메모 프로젝트에서는 리사이클뷰 이고 madapter는 리사이클뷰어뎁터임
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        helper.attachToRecyclerView(recyclerView);                                // RecyclerView에 ItemTouchHelper 붙이기
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Firebasehelper = new FirebaseHelper(getActivity());
        return view;
    }

    // 프래그먼트 객체 자체는 사라지지 않고 메모리에 남아있는 함수
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
    // =============================================================================================
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperListener {
        final private RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        private List<ChatRoomListModel> RoomList = new ArrayList<>();
        private Map<String, UserModel> UserList = new HashMap<>();
        private String My_User_Uid;
        private StorageReference StorageReference;
        private FirebaseFirestore Firestore;
        private ListenerRegistration listenerRegistration;
        private ListenerRegistration listenerUsers;
        Integer unreadTotal = 0;

        RecyclerViewAdapter() {
            Firestore = FirebaseFirestore.getInstance();
            StorageReference = FirebaseStorage.getInstance().getReference();
            My_User_Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // all users information
            listenerUsers = Firestore.collection("USERS")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {return;}
                            for (QueryDocumentSnapshot doc : value) {
                                UserList.put(doc.getId(), doc.toObject(UserModel.class));
                            }
                            getRoomInfo();
                        }
                    });
        }

        public void getRoomInfo() {
            // my chatting room information
            listenerRegistration = Firestore.collection("ROOMS").whereGreaterThanOrEqualTo("RoomModel_USER_OUT."+My_User_Uid,1)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {return;}
                            TreeMap<Date, ChatRoomListModel> orderedRooms = new TreeMap<Date, ChatRoomListModel>(Collections.reverseOrder());
                            for (final QueryDocumentSnapshot document : value) {
                                RoomModel roomModel = document.toObject(RoomModel.class);
                                Log.d("alsrbb",""+roomModel.getRoomModel_PostUid());
                                if (roomModel.getRoomModel_Message() !=null & roomModel.getRoomModel_DateOfManufacture() == null) {continue;} // FieldValue.serverTimestamp is so late
                                ChatRoomListModel chatRoomListModel = new ChatRoomListModel();
                                chatRoomListModel.setChatRoomListModel_RoomUid(document.getId());
                                //추가
                                chatRoomListModel.setChatRoomListModel_PostUid(document.getData().get("RoomModel_PostUid").toString());

                                if (roomModel.getRoomModel_Message() !=null) { // there are no last message
                                    chatRoomListModel.setChatRoomListModel_MessageLastDateTime(simpleDateFormat.format(roomModel.getRoomModel_DateOfManufacture()));
                                    switch(roomModel.getRoomModel_MessageType()){
                                        case "1": chatRoomListModel.setChatRoomListModel_LastMessage("Image"); break;
                                        case "2": chatRoomListModel.setChatRoomListModel_LastMessage("File"); break;
                                        default:  chatRoomListModel.setChatRoomListModel_LastMessage(roomModel.getRoomModel_Message());
                                    }
                                }
                                Map<String, Long> users = (Map<String, Long>) document.get("RoomModel_USERS");
                                chatRoomListModel.setChatRoomListModel_NumberOfUser(users.size());
                                for( String key : users.keySet() ){
                                    if (My_User_Uid.equals(key)) {
                                        Integer  unread = (int) (long) users.get(key);
                                        unreadTotal += unread;
                                        chatRoomListModel.setChatRoomListModel_UnreadCheck(unreadTotal);
                                        break;
                                    }
                                }
                                if (users.size()==2) {
                                    for( String key : users.keySet() ){
                                        if (My_User_Uid.equals(key)) continue;
                                        UserModel userModel = UserList.get(key);
                                        chatRoomListModel.setChatRoomListModel_Title(userModel.getUserModel_NickName());
                                        //ChatRoomListModel_ToUserUid 받아오기
                                        chatRoomListModel.setChatRoomListModel_ToUserUid(userModel.getUserModel_Uid());
                                        chatRoomListModel.setChatRoomListModel_ProfileImage(userModel.getUserModel_ProfileImage());
                                    }
                                }

                                if (roomModel.getRoomModel_DateOfManufacture()==null) roomModel.setRoomModel_DateOfManufacture(new Date());
                                orderedRooms.put(roomModel.getRoomModel_DateOfManufacture(), chatRoomListModel);
                            }
                            RoomList.clear();
                            for(Map.Entry<Date, ChatRoomListModel> entry : orderedRooms.entrySet()) {
                                RoomList.add(entry.getValue());
                            }
                            notifyDataSetChanged();
                            setBadge(getContext(), unreadTotal);
                        }
                    });
        }

        public void stopListening() {
            if (listenerRegistration != null) {
                listenerRegistration.remove();
                listenerRegistration = null;
            }
            if (listenerUsers != null) {
                listenerUsers.remove();
                listenerUsers = null;
            }

            RoomList.clear();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatroom, parent, false);
            return new RoomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final RoomViewHolder roomViewHolder = (RoomViewHolder) holder;

            final ChatRoomListModel chatRoomListModel = RoomList.get(position);

            roomViewHolder.room_title.setText(chatRoomListModel.getChatRoomListModel_Title());
            roomViewHolder.last_msg.setText(chatRoomListModel.getChatRoomListModel_LastMessage());
            roomViewHolder.last_time.setText(chatRoomListModel.getChatRoomListModel_MessageLastDateTime());


            if(chatRoomListModel.getChatRoomListModel_ProfileImage() !=null){
                Glide.with(getActivity()).load(chatRoomListModel.getChatRoomListModel_ProfileImage()).centerCrop().override(500).into(roomViewHolder.room_image);
            }
            else{
                Glide.with(getActivity()).load(R.drawable.none_profile_user).into(roomViewHolder.room_image);
            }

            if (chatRoomListModel.getChatRoomListModel_NumberOfUser() > 2) {
                roomViewHolder.room_count.setText(chatRoomListModel.getChatRoomListModel_NumberOfUser().toString());
                roomViewHolder.room_count.setVisibility(View.VISIBLE);
            } else {
                roomViewHolder.room_count.setVisibility(View.INVISIBLE);
            }
            if (chatRoomListModel.getChatRoomListModel_UnreadCheck() > 0) {
                roomViewHolder.unread_count.setText(chatRoomListModel.getChatRoomListModel_UnreadCheck().toString());
                roomViewHolder.unread_count.setVisibility(View.VISIBLE);
            } else {
                roomViewHolder.unread_count.setVisibility(View.INVISIBLE);
            }

            roomViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                    intent.putExtra("RoomUid", chatRoomListModel.getChatRoomListModel_RoomUid());
                    intent.putExtra("To_User_Uid", chatRoomListModel.getChatRoomListModel_ToUserUid());
                    intent.putExtra("ChatRoomListModel_Title", chatRoomListModel.getChatRoomListModel_Title());
                    intent.putExtra("MarketModel_Market_Uid", chatRoomListModel.getChatRoomListModel_PostUid());
                    chatRoomListModel.setChatRoomListModel_UnreadCheck(0);
                    roomViewHolder.unread_count.setVisibility(View.GONE);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return RoomList.size();
        }

        @Override
        public void onItemSwipe(int position) {
            RoomList.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
            RoomUid = RoomList.get(position).getChatRoomListModel_RoomUid();           // position으로 ID를 get
            ToUserUid = RoomList.get(position).getChatRoomListModel_ToUserUid();
            Firebasehelper.ROOMS_USERS_OUT_CHECK(RoomUid,My_User_Uid,ToUserUid);
            RoomList.remove(position);                         // 해당 position의 리스트의 데이터도 삭제한다.
            notifyItemRemoved(position);
        }

        private class RoomViewHolder extends RecyclerView.ViewHolder {
            public ImageView room_image;
            public TextView room_title;
            public TextView last_msg;
            public TextView last_time;
            public TextView room_count;
            public TextView unread_count;

            RoomViewHolder(View view) {
                super(view);
                room_image = view.findViewById(R.id.room_image);
                room_title = view.findViewById(R.id.room_title);
                last_msg = view.findViewById(R.id.last_msg);
                last_time = view.findViewById(R.id.last_time);
                room_count = view.findViewById(R.id.room_count);
                unread_count = view.findViewById(R.id.unread_count);
            }
        }
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                return resolveInfo.activityInfo.name;
            }
        }
        return null;
    }
}