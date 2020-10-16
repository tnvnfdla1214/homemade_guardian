package com.example.homemade_guardian_beta.chat.fragment;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.activity.ChatActivity;
import com.example.homemade_guardian_beta.model.chat.ChatRoomListModel;
import com.example.homemade_guardian_beta.model.chat.MessageModel;
import com.example.homemade_guardian_beta.model.user.UserModel;


//채팅 목록 프래그먼트
//이거 왕따 아무대도 안받아줌 이거는  ChatRoomFragment임
public class ChatRoomFragment extends Fragment {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerViewAdapter mAdapter;

    public ChatRoomFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        mAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
    // =============================================================================================
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        final private RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        private List<ChatRoomListModel> roomList = new ArrayList<>();
        private Map<String, UserModel> userList = new HashMap<>();
        private String myUid;
        private StorageReference storageReference;
        private FirebaseFirestore firestore;
        private ListenerRegistration listenerRegistration;
        private ListenerRegistration listenerUsers;

        RecyclerViewAdapter() {
            firestore = FirebaseFirestore.getInstance();
            storageReference  = FirebaseStorage.getInstance().getReference();
            myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // all users information
            listenerUsers = firestore.collection("USERS")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {return;}

                            for (QueryDocumentSnapshot doc : value) {
                                userList.put(doc.getId(), doc.toObject(UserModel.class));
                            }
                            getRoomInfo();
                        }
                    });
        }

        Integer unreadTotal = 0;
        public void getRoomInfo() {
            // my chatting room information
            listenerRegistration = firestore.collection("ROOMS").whereGreaterThanOrEqualTo("USERS."+myUid, 0)
//                    a.orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {return;}

                            TreeMap<Date, ChatRoomListModel> orderedRooms = new TreeMap<Date, ChatRoomListModel>(Collections.reverseOrder());

                            for (final QueryDocumentSnapshot document : value) {
                                MessageModel messageModel = document.toObject(MessageModel.class);
                                if (messageModel.getMessageModel_Message() !=null & messageModel.getMessageModel_DateOfManufacture() == null) {continue;} // FieldValue.serverTimestamp is so late

                                ChatRoomListModel chatRoomListModel = new ChatRoomListModel();
                                chatRoomListModel.setChatRoomListModel_RoomUid(document.getId());

                                if (messageModel.getMessageModel_Message() !=null) { // there are no last message
                                    chatRoomListModel.setChatRoomListModel_MessageLastDateTime(simpleDateFormat.format(messageModel.getMessageModel_DateOfManufacture()));
                                    switch(messageModel.getMessage_MessageType()){
                                        case "1": chatRoomListModel.setChatRoomListModel_LastMessage("Image"); break;
                                        case "2": chatRoomListModel.setChatRoomListModel_LastMessage("File"); break;
                                        default:  chatRoomListModel.setChatRoomListModel_LastMessage(messageModel.getMessageModel_Message());
                                    }
                                }
                                Map<String, Long> users = (Map<String, Long>) document.get("USERS");
                                chatRoomListModel.setChatRoomListModel_NumberOfUser(users.size());
                                for( String key : users.keySet() ){
                                    if (myUid.equals(key)) {
                                        Integer  unread = (int) (long) users.get(key);
                                        unreadTotal += unread;
                                        chatRoomListModel.setChatRoomListModel_UnreadCheck(unread);
                                        break;
                                    }
                                }
                                if (users.size()==2) {
                                    for( String key : users.keySet() ){
                                        if (myUid.equals(key)) continue;
                                        UserModel userModel = userList.get(key);
                                        chatRoomListModel.setChatRoomListModel_Title(userModel.getUserModel_NickName());
                                        //chatRoomModel.setPhoto(userModel.getUserphoto());
                                        chatRoomListModel.setChatRoomListModel_ProfileImage(userModel.getUserModel_ProfileImage());
                                    }
                                } else {                // group chat room
                                    chatRoomListModel.setChatRoomListModel_Title(document.getString("ChatRoomListModel_Title"));
                                }
                                if (messageModel.getMessageModel_DateOfManufacture()==null) messageModel.setMessageModel_DateOfManufacture(new Date());
                                orderedRooms.put(messageModel.getMessageModel_DateOfManufacture(), chatRoomListModel);
                            }
                            roomList.clear();
                            for(Map.Entry<Date, ChatRoomListModel> entry : orderedRooms.entrySet()) {
                                roomList.add(entry.getValue());
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

            roomList.clear();
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
            RoomViewHolder roomViewHolder = (RoomViewHolder) holder;

            final ChatRoomListModel chatRoomListModel = roomList.get(position);

            roomViewHolder.room_title.setText(chatRoomListModel.getChatRoomListModel_Title());
            roomViewHolder.last_msg.setText(chatRoomListModel.getChatRoomListModel_LastMessage());
            roomViewHolder.last_time.setText(chatRoomListModel.getChatRoomListModel_MessageLastDateTime());


            //이거 왜 아무이상 없음?
            if(chatRoomListModel.getChatRoomListModel_ProfileImage() !=null){
                Glide.with(getActivity()).load(chatRoomListModel.getChatRoomListModel_ProfileImage()).centerCrop().override(500).into(roomViewHolder.room_image);
                Log.d("태그1","민규11");
            }
            else{
                Glide.with(getActivity()).load(R.drawable.user).into(roomViewHolder.room_image);
                Log.d("태그1","민규22");
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
                    intent.putExtra("ChatRoomListModel_Title", chatRoomListModel.getChatRoomListModel_Title());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return roomList.size();
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
