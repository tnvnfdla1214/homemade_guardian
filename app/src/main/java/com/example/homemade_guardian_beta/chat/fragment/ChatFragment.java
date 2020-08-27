package com.example.homemade_guardian_beta.chat.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.homemade_guardian_beta.chat.photoview.ViewPagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.common.ChatUtil;
import com.example.homemade_guardian_beta.chat.model.ChatModel;
import com.example.homemade_guardian_beta.chat.model.MessageModel;
import com.example.homemade_guardian_beta.chat.model.NotificationModel;
import com.example.homemade_guardian_beta.chat.model.UserModel;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

//채팅룸 안의 프레그먼트
//채팅에서 어떻게 배열되는지 설정되는 기능(Chat_RecyclerView_Adapter 함수)
//처음 채팅 시작할때 한번 튕기는거 고쳐야 함
public class ChatFragment extends Fragment {

    private ListenerRegistration listenerRegistration;                                            //생성일자인터페이스(찾고 싶을때 사용하는듯)
    private LinearLayoutManager linearLayoutManager;                                              //리니얼레이아웃 매니져
    private RecyclerViewAdapter Chat_RecyclerView_Adapter;                                        //채팅 리사이클러뷰 어텝터 함수

    private Button Chat_Send_Button;                                                              //채팅 보내기 버튼
    private EditText Chat_Message_Input_EditText;                                                 //채팅 edittext
    private RecyclerView Chat_RecyclerView;                                                       //채팅 리사이클러뷰

    private String roomID;                                                                        //룸의 uid (확실 x) -> 맞다면 roommodel에 하나 만들어줘서 저장하기
    private String myUid;                                                                         //나의 uid
    private String toUid;                                                                         //상대방의 uid
    private Map<String, UserModel> userList = new HashMap<>();                                    //유저 리스트
    private ProgressDialog progressDialog = null;                                                 //진행 표시(Loding창)
    private Integer In_User_Count = 0;                                                            //현 채팅방의 들어와 있는 유저

    private FirebaseFirestore Firestore =null;
    private StorageReference storageReference;

    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");           //년/월일/ 변수
    private SimpleDateFormat dateFormatHour = new SimpleDateFormat("aa hh:mm");            //시/분/초 변수
    private static final int PICK_FROM_ALBUM = 1;                                                  //앨범 선택
    private static final int PICK_FROM_FILE = 2;                                                   //이미지 선택
    private static String rootPath = ChatUtil.getRootPath()+"/homemade_guardian_beta/";            //경로 설정


    public ChatFragment() {
    }

    public static final ChatFragment getInstance(String toUid, String roomID) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("toUid", toUid);
        bundle.putString("roomID", roomID);
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        Chat_RecyclerView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        Chat_RecyclerView.setLayoutManager(linearLayoutManager);

        Chat_Message_Input_EditText = view.findViewById(R.id.Chat_Message_Input_EditText);

        view.findViewById(R.id.Chat_Send_Button).setOnClickListener(Chat_Send_Button_ClickListener);
        view.findViewById(R.id.Chat_Image_Send_Button).setOnClickListener(Chat_Image_Send_Button_ClickListener);
        view.findViewById(R.id.Chat_File_Send_Button).setOnClickListener(Chat_File_Send_Button_ClickListener);
        view.findViewById(R.id.Chat_Message_Input_EditText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    ChatUtil.hideKeyboard(getActivity());
                }
            }
        });

        Chat_User_Check();                    //유저의 uid와 room의 uid 체크 함수

        Firestore = FirebaseFirestore.getInstance();
        storageReference  = FirebaseStorage.getInstance().getReference();
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        KroreaTime();                         //한국 시간 가져오기 함수
        Chat_RecyclerView_Arrangement();      // Chat_RecyclerView의 배열 정리에 관한 함수

        return view;
    }

    // Chat_RecyclerView의 배열 정리에 관한 함수
    public void Chat_RecyclerView_Arrangement(){
        Chat_RecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (Chat_RecyclerView_Adapter !=null & bottom < oldBottom) {
                    final int lastAdapterItem = Chat_RecyclerView_Adapter.getItemCount() - 1;
                    Chat_RecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            int recyclerViewPositionOffset = -1000000;
                            View bottomView = linearLayoutManager.findViewByPosition(lastAdapterItem);
                            if (bottomView != null) {
                                recyclerViewPositionOffset = 0 - bottomView.getHeight();
                            }
                            linearLayoutManager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
                        }
                    });
                }
            }
        });
    }

    //한국 시간 가져오기 함수수
   public void KroreaTime(){
        dateFormatDay.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        dateFormatHour.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    }

    //유저의 uid와 room의 uid 체크 함수
    public void Chat_User_Check(){

        if (getArguments() != null) {
            roomID = getArguments().getString("roomID");
            toUid = getArguments().getString("toUid");
        }
        if (!"".equals(toUid) && toUid!=null) {                     // find existing room for two user
            findChatRoom(toUid);
        } else
        if (!"".equals(roomID) && roomID!=null) {                   // existing room (multi user)
            setChatRoom(roomID);
        };

        if (roomID==null) {                                         // new room for two user
            getUserInfoFromServer(myUid);
            getUserInfoFromServer(toUid);
            In_User_Count = 2;
        };
    }


    // 프래그먼트 객체 자체는 사라지지 않고 메모리에 남아있는 함수
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Chat_RecyclerView_Adapter != null) {
            Chat_RecyclerView_Adapter.stopListening();
        }
    }

    // chat에 들어와있는 user의 uid로 정보를 가져와 userlist에 담는 함수
    void getUserInfoFromServer(String UserModel_Uid){
        Firestore.collection("USERS").document(UserModel_Uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                userList.put(userModel.getUserModel_Uid(), userModel);
                if (roomID != null & In_User_Count == userList.size()) {
                    Chat_RecyclerView_Adapter = new RecyclerViewAdapter();
                    Chat_RecyclerView.setAdapter(Chat_RecyclerView_Adapter);
                }
            }
        });
    }

    // 사용자 ID로 채팅방을 찾은 후 룸 ID를 반환하는 함수
    void findChatRoom(final String toUid){
        Firestore.collection("rooms").whereGreaterThanOrEqualTo("USERS."+myUid, 0).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {return;}

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Long> users = (Map<String, Long>) document.get("USERS");
                            if (users.size()==2 & users.get(toUid)!=null){
                                setChatRoom(document.getId());
                                break;
                            }
                        }
                    }
                });
    }

    // 채팅방에서 사용자 목록을 가져오는 함수
    void setChatRoom(String rid) {
        roomID = rid;
        Firestore.collection("rooms").document(roomID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {return;}
                DocumentSnapshot document = task.getResult();
                Map<String, Long> users = (Map<String, Long>) document.get("USERS");

                for( String key : users.keySet() ){
                    getUserInfoFromServer(key);
                }
                In_User_Count = users.size();
            }
        });
    }

    //읽은 채팅창인지 아닌지 확인하는 함수 -> 이거 조금 문제 있기에 나중에 수정해야함(이름도 좀 이상함)
    void setUnread2Read() {
        if (roomID==null) return;
        Firestore.collection("rooms").document(roomID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {return;}
                DocumentSnapshot document = task.getResult();
                Map<String, Long> users = (Map<String, Long>) document.get("USERS");

                users.put(myUid, (long) 0);
                document.getReference().update("USERS", users);
            }
        });
    }

    //채팅룸 만들기 함수
    public void CreateChattingRoom(final DocumentReference room) {
        Map<String, Integer> users = new HashMap<>();
        String title = "";
        for( String key : userList.keySet() ){
            users.put(key, 0);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("title", null);
        data.put("USERS", users);

        room.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Chat_RecyclerView_Adapter = new RecyclerViewAdapter();
                    Chat_RecyclerView.setAdapter(Chat_RecyclerView_Adapter);
                }
            }
        });
    }

    //유저의 리스트를 가져오는 함수
    public Map<String, UserModel> getUserList() {
        return userList;
    }

    //채팅 보내기 버튼 함수
    Button.OnClickListener Chat_Send_Button_ClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            String msg = Chat_Message_Input_EditText.getText().toString();
            sendMessage(msg, "0", null);
            Chat_Message_Input_EditText.setText("");
        }
    };

    // 이미지 보내기 버튼 함수
    Button.OnClickListener Chat_Image_Send_Button_ClickListener = new View.OnClickListener() {
        public void onClick(final View view) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_FROM_ALBUM);
        }
    };

    // 파일 보내기 버튼 함수
    Button.OnClickListener Chat_File_Send_Button_ClickListener = new View.OnClickListener() {
        public void onClick(final View view) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
        }
    };

    //메세지 보내기 함수
    private void sendMessage(final String msg, String msgtype, final ChatModel.FileInfo fileinfo) {
        Chat_Send_Button.setEnabled(false);

        if (roomID==null) {             // create chatting room for two user
            roomID = Firestore.collection("rooms").document().getId();
            CreateChattingRoom( Firestore.collection("rooms").document(roomID) );
        }

        final Map<String,Object> messages = new HashMap<>();
        messages.put("uid", myUid);
        messages.put("msg", msg);
        messages.put("msgtype", msgtype);
        messages.put("timestamp", FieldValue.serverTimestamp());
        if (fileinfo!=null){
            messages.put("filename", fileinfo.filename);
            messages.put("filesize", fileinfo.filesize);
        }

        final DocumentReference docRef = Firestore.collection("rooms").document(roomID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {return;}

                WriteBatch batch = Firestore.batch();

                // save last message
                batch.set(docRef, messages, SetOptions.merge());

                // save message
                List<String> readUsers = new ArrayList();
                readUsers.add(myUid);
                messages.put("readUsers", readUsers);//new String[]{myUid} );
                batch.set(docRef.collection("messages").document(), messages);

                // inc unread message count
                DocumentSnapshot document = task.getResult();
                Map<String, Long> users = (Map<String, Long>) document.get("USERS");

                for( String key : users.keySet() ){
                    if (!myUid.equals(key)) users.put(key, users.get(key)+1);
                }
                document.getReference().update("USERS", users);

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //sendGCM();
                            Chat_Send_Button.setEnabled(true);
                        }
                    }
                });
            }

        });
    };

    //사용안하는 듯
    void sendGCM(){
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.notification.title = userList.get(myUid).getUserModel_NickName();
        notificationModel.notification.body = Chat_Message_Input_EditText.getText().toString();
        notificationModel.data.title = userList.get(myUid).getUserModel_NickName();
        notificationModel.data.body = Chat_Message_Input_EditText.getText().toString();

        for ( Map.Entry<String, UserModel> elem : userList.entrySet() ){
            if (myUid.equals(elem.getValue().getUserModel_Uid())) continue;
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));
            Request request = new Request.Builder()
                    .header("Content-Type", "application/json")
                    .addHeader("Authorization", "key=")
                    .url("https://fcm.googleapis.com/fcm/send")
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {                }
            });
        }
    }

    // 이미지나 파일을 로딩하는 결과 함수
    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode!= RESULT_OK) { return;}
        Uri fileUri = data.getData();
        final String filename = ChatUtil.getUniqueValue();

        showProgressDialog("Uploading selected File.");
        final ChatModel.FileInfo fileinfo  = getFileDetailFromUri(getContext(), fileUri);

        storageReference.child("files/"+filename).putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                sendMessage(filename, Integer.toString(requestCode), fileinfo);
                hideProgressDialog();
            }
        });
        if (requestCode != PICK_FROM_ALBUM) { return;}

        // small image
        Glide.with(getContext())
                .asBitmap()
                .load(fileUri)
                .apply(new RequestOptions().override(150, 150))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        storageReference.child("filesmall/"+filename).putBytes(data);
                    }
                });
    }

    // Uri에서 파일 이름 및 크기 가져오기 함수
    public static ChatModel.FileInfo getFileDetailFromUri(final Context context, final Uri uri) {
        if (uri == null) { return null; }

        ChatModel.FileInfo fileDetail = new ChatModel.FileInfo();
        // File Scheme.
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            File file = new File(uri.getPath());
            fileDetail.filename = file.getName();
            fileDetail.filesize = ChatUtil.size2String(file.length());
        }
        // Content Scheme.
        else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor returnCursor =
                    context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null && returnCursor.moveToFirst()) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                fileDetail.filename = returnCursor.getString(nameIndex);
                fileDetail.filesize = ChatUtil.size2String(returnCursor.getLong(sizeIndex));
                returnCursor.close();
            }
        }

        return fileDetail;
    }

    //잠시 기다리라고 창띄우는 함수
    public void showProgressDialog(String title ) {
        if (progressDialog==null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(title);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //안쓰는듯
    public void setProgressDialog(int value) {
        progressDialog.setProgress(value);
    }
    //로딩창 지우는 함수
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }


    //채팅 RecyclerViewAdapter
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        final private RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));

        List<MessageModel> MessageModel_List= new ArrayList<MessageModel>(); //메세지 리스트
        String beforeDay = null; //이거 안쓰는듯
        MessageViewHolder beforeViewHolder; //이거 안쓰는듯


        //파일 경로 설정
        RecyclerViewAdapter() {
            File dir = new File(rootPath);
            if (!dir.exists()) {
                if (!ChatUtil.isPermissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return;
                }
                dir.mkdirs();
            }

            setUnread2Read();
            startListening();
        }

        //시작 세팅 함수
        public void startListening() {
            beforeDay = null;
            MessageModel_List.clear();

            CollectionReference roomRef = Firestore.collection("rooms").document(roomID).collection("messages");
            // my chatting room information
            listenerRegistration = roomRef.orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {return;}

                    MessageModel messageModel;
                    for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
                        switch (change.getType()) {
                            case ADDED:
                                messageModel = change.getDocument().toObject(MessageModel.class);
                                //if (message.msg !=null & message.timestamp == null) {continue;} // FieldValue.serverTimestamp is so late

                                if (messageModel.getReadUsers().indexOf(myUid) == -1) {
                                    messageModel.getReadUsers().add(myUid);
                                    change.getDocument().getReference().update("readUsers", messageModel.getReadUsers());
                                }
                                MessageModel_List.add(messageModel);
                                notifyItemInserted(change.getNewIndex());
                                break;
                            case MODIFIED:
                                messageModel = change.getDocument().toObject(MessageModel.class);
                                MessageModel_List.set(change.getOldIndex(), messageModel);
                                notifyItemChanged(change.getOldIndex());
                                break;
                            case REMOVED:
                                MessageModel_List.remove(change.getOldIndex());
                                notifyItemRemoved(change.getOldIndex());
                                break;
                        }
                    }
                    Chat_RecyclerView.scrollToPosition(MessageModel_List.size() - 1);
                }
            });
        }

        public void stopListening() {
            if (listenerRegistration != null) {
                listenerRegistration.remove();
                listenerRegistration = null;
            }

            MessageModel_List.clear();
            notifyDataSetChanged();
        }

        //케이스에 따른 이미지 띄우기 함수
        @Override
        public int getItemViewType(int position) {
            MessageModel messageModel = MessageModel_List.get(position);
            if (myUid.equals(messageModel.getUid()) ) {
                switch(messageModel.getMsgtype()){
                    case "1": return R.layout.item_chatimage_right;
                    case "2": return R.layout.item_chatfile_right;
                    default:  return R.layout.item_chatmsg_right;
                }
            } else {
                switch(messageModel.getMsgtype()){
                    case "1": return R.layout.item_chatimage_left;
                    case "2": return R.layout.item_chatfile_left;
                    default:  return R.layout.item_chatmsg_left;
                }
            }
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
            final MessageModel messageModel = MessageModel_List.get(position);
            setReadCounter(messageModel, messageViewHolder.Read_Check);

            if ("0".equals(messageModel.getMsgtype())) {                                      // text message
                messageViewHolder.Message_TextView.setText(messageModel.getMsg());
            } else
            if ("2".equals(messageModel.getMsgtype())) {                                      // file transfer
                messageViewHolder.Message_TextView.setText(messageModel.getFilename() + "\n" + messageModel.getFilesize());
                messageViewHolder.filename = messageModel.getFilename();
                messageViewHolder.realname = messageModel.getMsg();
                File file = new File(rootPath + messageModel.getFilename());
                if(file.exists()) {
                    messageViewHolder.button_item.setText("Open File");
                } else {
                    messageViewHolder.button_item.setText("Download");
                }
            } else {                                                                // image transfer
                messageViewHolder.realname = messageModel.getMsg();
                Glide.with(getContext())
                        .load(storageReference.child("filesmall/"+ messageModel.getMsg()))
                        .apply(new RequestOptions().override(1000, 1000))
                        .into(messageViewHolder.Message_Image_ImageView);
            }

            if (! myUid.equals(messageModel.getUid())) {
                UserModel userModel = userList.get(messageModel.getUid());
                messageViewHolder.Message_NickName.setText(userModel.getUserModel_NickName());


                //상대방 프로필사진으로 바꾸기
                if (userModel.getphotoUrl() != null) {
                    Glide.with(getContext()).load(userModel.getphotoUrl()).centerCrop().override(500).into(messageViewHolder.User_Profile_Imalge);
                } else{
                    Glide.with(getContext()).load(R.drawable.user).into(messageViewHolder.User_Profile_Imalge);
                }

            }
            messageViewHolder.Message_Divider.setVisibility(View.INVISIBLE);
            messageViewHolder.Message_Divider.getLayoutParams().height = 0;
            messageViewHolder.Message_DateOfManufacture.setText("");
            if (messageModel.getTimestamp()==null) {return;}

            String day = dateFormatDay.format( messageModel.getTimestamp());
            String timestamp = dateFormatHour.format( messageModel.getTimestamp());
            messageViewHolder.Message_DateOfManufacture.setText(timestamp);

            if (position==0) {
                messageViewHolder.Message_Divider_Date.setText(day);
                messageViewHolder.Message_Divider.setVisibility(View.VISIBLE);
                messageViewHolder.Message_Divider.getLayoutParams().height = 60;
            } else {
                MessageModel beforeMsg = MessageModel_List.get(position - 1);
                String beforeDay = dateFormatDay.format( beforeMsg.getTimestamp() );

                if (!day.equals(beforeDay) && beforeDay != null) {
                    messageViewHolder.Message_Divider_Date.setText(day);
                    messageViewHolder.Message_Divider.setVisibility(View.VISIBLE);
                    messageViewHolder.Message_Divider.getLayoutParams().height = 60;
                }
            }
        }

        void setReadCounter (MessageModel messageModel, final TextView textView) {
            int cnt = In_User_Count - messageModel.getReadUsers().size();
            if (cnt > 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(String.valueOf(cnt));
            } else {
                textView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return MessageModel_List.size();
        }


    }

    //메세지보낼때 프로필 사진, 뭐라고 보냈는지, 언제 보냈는지 등을 묶어주는 함수
    //파일도 없앨거임
    private class MessageViewHolder extends RecyclerView.ViewHolder {
        public ImageView User_Profile_Imalge;  //프로필 사진
        public TextView Message_TextView;//채팅 메세지 텍스트
        public ImageView Message_Image_ImageView;          //채팅 메세지 이미지
        public TextView Message_NickName; //채팅 메세지 이름
        public TextView Message_DateOfManufacture; //메세지 보낸 시간
        public TextView Read_Check; //읽었는지 확인
        public LinearLayout Message_Divider; //메세지 전체 틀
        public TextView Message_Divider_Date; //메세지 보낸 날짜
        public TextView button_item;            // only item_chatfile_<-이거 뭔지 정확히 체크
        public LinearLayout msgLine_item;       // only item_chatfile_ <-이거 뭔지 정확히 체크
        public String filename;
        public String realname;

        public MessageViewHolder(View view) {
            super(view);
            User_Profile_Imalge = view.findViewById(R.id.User_Profile_Imalge);
            Message_TextView = view.findViewById(R.id.Message_TextView);
            Message_Image_ImageView = view.findViewById(R.id.Message_Image_ImageView);
            Message_DateOfManufacture = view.findViewById(R.id.Message_DateOfManufacture);
            Message_NickName = view.findViewById(R.id.Message_NickName);
            Read_Check = view.findViewById(R.id.Read_Check);
            Message_Divider = view.findViewById(R.id.Message_Divider);
            Message_Divider_Date = view.findViewById(R.id.Message_Divider_Date);
            button_item = view.findViewById(R.id.button_item);
            msgLine_item = view.findViewById(R.id.msgLine_item);        // for file
            if (msgLine_item!=null) {
                msgLine_item.setOnClickListener(downloadClickListener);
            }
            if (Message_Image_ImageView !=null) {                                       // for image
                Message_Image_ImageView.setOnClickListener(imageClickListener);
            }
        }
        // 파일 다운로드및 열기 버튼 함수
        Button.OnClickListener downloadClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if ("Download".equals(button_item.getText())) {
                    download();
                } else {
                    openWith();
                }
            }
            public void download() {
                if (!ChatUtil.isPermissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return ;
                }
                showProgressDialog("Downloading File.");

                final File localFile = new File(rootPath, filename);

                storageReference.child("files/"+realname).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        button_item.setText("Open File");
                        hideProgressDialog();
                        Log.e("DirectTalk9 ","local file created " +localFile.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("DirectTalk9 ","local file not created  " +exception.toString());
                    }
                });
            }

            public void openWith() {
                File newFile = new File(rootPath + filename);
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext = newFile.getName().substring(newFile.getName().lastIndexOf(".") + 1);
                String type = mime.getMimeTypeFromExtension(ext);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".provider", newFile);

                    List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }else {
                    uri = Uri.fromFile(newFile);
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, type);//"application/vnd.android.package-archive");
                startActivity(Intent.createChooser(intent, "Your title"));
            }
        };
        // photo view
        Button.OnClickListener imageClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewPagerActivity.class);
                intent.putExtra("roomID", roomID);
                intent.putExtra("realname", realname); //<- 이게 뭔지 정확히 모르겠음
                startActivity(intent);
            }
        };
    }

    public void backPressed() {
    }
}
