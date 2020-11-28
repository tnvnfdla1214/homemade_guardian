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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.homemade_guardian_beta.chat.ChatUtil;
import com.example.homemade_guardian_beta.chat.common.SendNotification;
import com.example.homemade_guardian_beta.chat.common.photoview.ViewPagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
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
import com.example.homemade_guardian_beta.model.chat.ChatModel;
import com.example.homemade_guardian_beta.model.chat.MessageModel;
import com.example.homemade_guardian_beta.model.chat.NotificationModel;
import com.example.homemade_guardian_beta.model.user.UserModel;


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

    private String MarketModel_Market_Uid;
    private String ChatRoomListModel_RoomUid;                                                     //룸의 uid (확실 x)
    private String currentUser_Uid;                                                                         //나의 uid
    private String To_User_Uid;                                                                         //상대방의 uid
    private Map<String, UserModel> UserModel_UserList = new HashMap<>();                                    //유저 리스트
    private ProgressDialog progressDialog = null;                                                 //진행 표시(Loding창)
    private Integer NumberOfUser = 0;                                                            //현 채팅방의 들어와 있는 유저


    private FirebaseFirestore Firestore =null;
    private StorageReference StorageReference;

    private SimpleDateFormat DateFormatDay = new SimpleDateFormat("yyyy-MM-dd");           //년/월일/ 변수
    private SimpleDateFormat DateFormatHour = new SimpleDateFormat("aa hh:mm");            //시/분/초 변수
    private static final int PICK_FROM_ALBUM = 1;                                                  //앨범 선택
    private static final int PICK_FROM_FILE = 2;                                                   //이미지 선택
    private static String rootPath = ChatUtil.getRootPath()+"/homemade_guardian_beta/";            //경로 설정

    private MessageModel MessageModel;                                                             //UserModel 참조 선언
    int Int_MessageModel_ImageCount;                                                               //string형을 int로 형변환
    String String_MessageModel_ImageCount;                                                         //int를 string으로 변환

    private RelativeLayout loaderLayout;

    private RoomUidSetListener roomUidSetListener;


    public ChatFragment() {
    }

    public static final ChatFragment getInstance(String To_User_Uid, String RoomUiD,String MarketModel_Market_Uid,String currentUser_Uid) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("To_User_Uid", To_User_Uid);
        bundle.putString("RoomUid", RoomUiD);
        bundle.putString("MarketModel_Market_Uid", MarketModel_Market_Uid);
        bundle.putString("currentUser_Uid", currentUser_Uid);
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
        Chat_Send_Button = view.findViewById(R.id.Chat_Send_Button);
        Chat_Message_Input_EditText = view.findViewById(R.id.Chat_Message_Input_EditText);
        loaderLayout = (RelativeLayout)view.findViewById(R.id.Loader_Lyaout);

        view.findViewById(R.id.Chat_Send_Button).setOnClickListener(Chat_Send_Button_ClickListener);
        view.findViewById(R.id.Chat_Image_Send_Button).setOnClickListener(Chat_Image_Send_Button_ClickListener);
        view.findViewById(R.id.Chat_Message_Input_EditText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    ChatUtil.hideKeyboard(getActivity());
                }
            }
        });
        Chat_Message_Input_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().getBytes().length >= 150) {
                    Log.d("민규","150자 이상입니다.");
                }
            }
        });


        Firestore = FirebaseFirestore.getInstance();
        StorageReference = FirebaseStorage.getInstance().getReference();
        currentUser_Uid = getArguments().getString("currentUser_Uid");

        Chat_User_Check();                    //유저의 uid와 room의 uid 체크 함수
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

    //한국 시간 가져오기 함수
    public void KroreaTime(){
        DateFormatDay.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        DateFormatHour.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    }

    //유저의 uid와 room를 해당 변수에 넣는 함수,또한 현재 roomuid와 touid가 맞는지 확인 하는 함수
    public void Chat_User_Check(){

        if (getArguments() != null) {
            ChatRoomListModel_RoomUid = getArguments().getString("RoomUid");
            To_User_Uid = getArguments().getString("To_User_Uid");
            MarketModel_Market_Uid = getArguments().getString("MarketModel_Market_Uid");
        }

        if (!"".equals(To_User_Uid) && To_User_Uid !=null && !"".equals(MarketModel_Market_Uid) && MarketModel_Market_Uid !=null) {
            findChatRoom(To_User_Uid,MarketModel_Market_Uid);
        } else
        if (!"".equals(ChatRoomListModel_RoomUid) && ChatRoomListModel_RoomUid !=null) {                   // existing room (multi user)
            setChatRoom(ChatRoomListModel_RoomUid);
        }

        if (ChatRoomListModel_RoomUid ==null) {                                         // new room for two user
            currentUser_Uid = getArguments().getString("currentUser_Uid");
            To_User_Uid = getArguments().getString("To_User_Uid");
            getUserInfoFromServer(currentUser_Uid);
            getUserInfoFromServer(To_User_Uid);
            NumberOfUser = 2;
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
        Firestore = FirebaseFirestore.getInstance();
        Firestore.collection("USERS").document(UserModel_Uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                UserModel_UserList.put(userModel.getUserModel_Uid(), userModel);
                if (ChatRoomListModel_RoomUid != null & NumberOfUser == UserModel_UserList.size()) {
                    Chat_RecyclerView_Adapter = new RecyclerViewAdapter();
                    Chat_RecyclerView.setAdapter(Chat_RecyclerView_Adapter);
                }
            }
        });
    }

    // Room에서 MessageModel_PostUid로 찾는다.
    void findChatRoom(final String toUid,final String MarketModel_Market_Uid){
        Firestore = FirebaseFirestore.getInstance();
        Firestore.collection("ROOMS").whereEqualTo("MessageModel_PostUid",MarketModel_Market_Uid).get()
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

    // 채팅방에서 사용자 목록을 가져오는 함수 -> 사실 필요없을 듯
    void setChatRoom(String ChatRoomListModel_RoomUid) {
        Firestore = FirebaseFirestore.getInstance();
        Firestore.collection("ROOMS").document(ChatRoomListModel_RoomUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {return;}
                DocumentSnapshot document = task.getResult();
                Map<String, Long> users = (Map<String, Long>) document.get("USERS");

                for( String key : users.keySet() ){
                    getUserInfoFromServer(key);
                }
                NumberOfUser = users.size();
            }
        });
    }

    //읽은 채팅창인지 아닌지 확인하는 함수 -> 이거 조금 문제 있기에 나중에 수정해야함(이름도 좀 이상함) , 이거 사용을 안함
    void setUnread2Read() {
        if (ChatRoomListModel_RoomUid ==null) return;
        Firestore = FirebaseFirestore.getInstance();
        Firestore.collection("ROOMS").document(ChatRoomListModel_RoomUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {return;}
                DocumentSnapshot document = task.getResult();
                Map<String, Long> users = (Map<String, Long>) document.get("USERS");

                users.put(currentUser_Uid, (long) 0);
                document.getReference().update("USERS", users);
            }
        });
    }

    //채팅(글) 보내기 버튼 함수
    Button.OnClickListener Chat_Send_Button_ClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            String msg = Chat_Message_Input_EditText.getText().toString();
            sendMessage(msg, "0", null, MarketModel_Market_Uid,ChatRoomListModel_RoomUid);
            Chat_Message_Input_EditText.setText("");
        }
    };

    // 이미지 보내기 버튼 함수
    Button.OnClickListener Chat_Image_Send_Button_ClickListener = new View.OnClickListener() {
        public void onClick(final View view) {
            if(ChatRoomListModel_RoomUid!=null){
                //여기서 처음이면 0 다음부터는 +1씩 증가시킨다. 그후 이름을 해당 에 맞추어 이름을 지어 배열로 가진다.
                DocumentReference docRefe_ROOMS_CurrentUid = FirebaseFirestore.getInstance().collection("ROOMS").document(ChatRoomListModel_RoomUid);
                docRefe_ROOMS_CurrentUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MessageModel = documentSnapshot.toObject(MessageModel.class);
                        Int_MessageModel_ImageCount = Integer.parseInt(MessageModel.getMessageModel_ImageCount());
                        Log.d("민규","Int_MessageModel_ImageCount1 : " + Int_MessageModel_ImageCount);
                    }
                });
            }
            else{
                //이렇게 되면 첫채팅에서 사진 누르고 다시 취소하면 아무 채팅없이 방이 하나 파질듯
                ChatRoomListModel_RoomUid = Firestore.collection("ROOMS").document().getId();
                CreateChattingRoom( Firestore.collection("ROOMS").document(ChatRoomListModel_RoomUid) );

                //여기서 처음이면 0 다음부터는 +1씩 증가시킨다. 그후 이름을 해당 에 맞추어 이름을 지어 배열로 가진다.
                DocumentReference docRefe_ROOMS_CurrentUid = FirebaseFirestore.getInstance().collection("ROOMS").document(ChatRoomListModel_RoomUid);
                docRefe_ROOMS_CurrentUid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MessageModel = documentSnapshot.toObject(MessageModel.class);
                        Int_MessageModel_ImageCount = Integer.parseInt(MessageModel.getMessageModel_ImageCount());
                        Log.d("민규","Int_MessageModel_ImageCount2 : " + Int_MessageModel_ImageCount);
                    }
                });
            }
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_FROM_ALBUM);
        }
    };


    //최초 채팅룸 만들기,1을 0으로 변경하는 함수
    public void CreateChattingRoom(final DocumentReference room) {
        //유저의 uid와 읽었는지 않읽었는지 확인 하는 정보
        Map<String, Integer> USERS = new HashMap<>();
        for( String key : UserModel_UserList.keySet() ){
            USERS.put(key, 0);
        }
        //유저의 uid와 유저가 나갔는지 안나갓는지 확인하는 정보 <- 1이면 안나간거 0이면 나간거
        Map<String, Integer> USERS_OUT = new HashMap<>();
        for( String key : UserModel_UserList.keySet() ){
            USERS_OUT.put(key, 1);
        }

        //나머지 ROOMS 정보
        final Map<String,Object> MessageModel = new HashMap<>();
        MessageModel.put("MessageModel_Title", null);
        MessageModel.put("USERS", USERS);
        MessageModel.put("USERS_OUT", USERS_OUT);

        //최초 MessageModel_ImageCount 는 0으로 시작
        MessageModel.put("MessageModel_ImageCount","0");

        //해당 룸은 어떤 포스트의 경로인지 확인하는 Postuid를 만들어준다.
        MessageModel.put("MessageModel_PostUid", MarketModel_Market_Uid);

        room.set(MessageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Chat_RecyclerView_Adapter = new RecyclerViewAdapter();
                    Chat_RecyclerView.setAdapter(Chat_RecyclerView_Adapter);
                }
            }
        });
    }


    //메세지 보내기 함수
    private void sendMessage(final String MessageModel_Message, String Message_MessageType, final ChatModel.FileInfo fileinfo, final String MarketModel_Market_Uid, String RoomUid) {
        Chat_Send_Button.setEnabled(false);

        //최초 룸 만들기기
        if(ChatRoomListModel_RoomUid ==null) {             // create chatting room for two user
            ChatRoomListModel_RoomUid = Firestore.collection("ROOMS").document().getId();
            CreateChattingRoom( Firestore.collection("ROOMS").document(ChatRoomListModel_RoomUid) );

            //ChatActivity에 Room_uid 넘겨주기
            roomUidSetListener.RoomUidSet(ChatRoomListModel_RoomUid, To_User_Uid);

        }
        //이거 어떻게 바꿈?
        //메세지 모델에 들어가는거
        final Map<String,Object> MessageModel = new HashMap<>();
        MessageModel.put("MessageModel_UserUid", currentUser_Uid);
        MessageModel.put("MessageModel_Message", MessageModel_Message);
        MessageModel.put("Message_MessageType", Message_MessageType);
        MessageModel.put("MessageModel_DateOfManufacture", FieldValue.serverTimestamp());
        if (fileinfo!=null){
            MessageModel.put("MessageModel_FileName", fileinfo.FileName);
            MessageModel.put("MessageModel_FileSize", fileinfo.FileSize);
        }


        //처음 채팅을 시작할때 -> 텍스트만 들어옴
        if(RoomUid ==null){
            final DocumentReference docRef = Firestore.collection("ROOMS").document(ChatRoomListModel_RoomUid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    //MESSAGE의 MessageModel_ReadUser에 uid를 넣는다.
                    //ROOMS의 USERS의 해당 uid의 1->0으로 바꾼다.
                    if (!task.isSuccessful()) {return;}
                    WriteBatch batch = Firestore.batch();
                    // save last message
                    batch.set(docRef, MessageModel, SetOptions.merge());
                    // save message
                    List<String> ReadUsers = new ArrayList();
                    ReadUsers.add(currentUser_Uid);
                    MessageModel.put("MessageModel_ReadUser", ReadUsers);
                    batch.set(docRef.collection("MESSAGE").document(), MessageModel);
                    // inc unread message count
                    DocumentSnapshot document = task.getResult();
                    Map<String, Long> users = (Map<String, Long>) document.get("USERS");

                    for( String key : users.keySet() ){
                        if (!currentUser_Uid.equals(key)) users.put(key, users.get(key)+1);
                    }
                    document.getReference().update("USERS", users);
                    document.getReference().update("MessageModel_PostUid", MarketModel_Market_Uid);

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //sendGCM();
                                sendGson();
                                Chat_Send_Button.setEnabled(true);
                            }
                        }
                    });
                }

            });
        }
        else{
            //룸이 있다면`
            final DocumentReference docRef = Firestore.collection("ROOMS").document(RoomUid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (!task.isSuccessful()) {return;}
                    WriteBatch batch = Firestore.batch();
                    // save last message
                    batch.set(docRef, MessageModel, SetOptions.merge());
                    // save message
                    List<String> ReadUsers = new ArrayList();
                    ReadUsers.add(currentUser_Uid);
                    MessageModel.put("MessageModel_ReadUser", ReadUsers);
                    batch.set(docRef.collection("MESSAGE").document(), MessageModel);
                    // inc unread message count
                    DocumentSnapshot document = task.getResult();
                    Map<String, Long> users = (Map<String, Long>) document.get("USERS");

                    for( String key : users.keySet() ){
                        if (!currentUser_Uid.equals(key)) users.put(key, users.get(key)+1);
                    }
                    document.getReference().update("USERS", users);
                    document.getReference().update("MessageModel_PostUid", MarketModel_Market_Uid);

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
        }

    }

    // 이미지나 파일을 로딩하는 결과 함수 (대략 10초가 넘게 걸림 , 스토리지에 넣는것 9초 사진 띄우기 1초)
    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode!= RESULT_OK) { return;}
        Uri fileUri = data.getData(); //해당 사진
        final ChatModel.FileInfo fileinfo  = getFileDetailFromUri(getContext(), fileUri); //chatmodel.fileinfo에 넣기
        Int_MessageModel_ImageCount = Int_MessageModel_ImageCount +1;
        String_MessageModel_ImageCount =String.valueOf(Int_MessageModel_ImageCount);
        StorageReference.child("ROOMS/"+ChatRoomListModel_RoomUid + "/" + String_MessageModel_ImageCount).putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //추가 ()
                DocumentReference docRefe_ROOMS_CurrentUid = FirebaseFirestore.getInstance().collection("ROOMS").document(ChatRoomListModel_RoomUid);
                docRefe_ROOMS_CurrentUid.update("MessageModel_ImageCount", String_MessageModel_ImageCount).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sendMessage(String_MessageModel_ImageCount, Integer.toString(requestCode), fileinfo, MarketModel_Market_Uid,ChatRoomListModel_RoomUid);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
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
                        StorageReference.child("ROOMS/"+ChatRoomListModel_RoomUid + "/" + String_MessageModel_ImageCount).putBytes(data);
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
            fileDetail.FileName = file.getName();
            fileDetail.FileSize = ChatUtil.size2String(file.length());
        }
        // Content Scheme.
        else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor returnCursor =
                    context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null && returnCursor.moveToFirst()) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                fileDetail.FileName = returnCursor.getString(nameIndex);
                fileDetail.FileSize = ChatUtil.size2String(returnCursor.getLong(sizeIndex));
                returnCursor.close();
            }
        }

        return fileDetail;
    }

    /*
    //사용안하는 듯-> 이거 근데 사용해야함 이거 안드로이드 폰에 메세지 뜨게 하는 용도 인듯
    void sendGCM(){
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.notification.title = UserModel_UserList.get(currentUser_Uid).getUserModel_NickName();
        notificationModel.notification.body = Chat_Message_Input_EditText.getText().toString();
        notificationModel.data.title = UserModel_UserList.get(currentUser_Uid).getUserModel_NickName();
        notificationModel.data.body = Chat_Message_Input_EditText.getText().toString();

        for ( Map.Entry<String, UserModel> elem : UserModel_UserList.entrySet() ){
            if (currentUser_Uid.equals(elem.getValue().getUserModel_Uid())) continue;
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

     */

    private void sendGson() {

        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {  //데이터의 존재여부
                            UserModel userModel = document.toObject(UserModel.class);
                            SendNotification.sendNotification(userModel.getUserModel_Token(), userModel.getUserModel_NickName(), Chat_Message_Input_EditText.getText().toString());
                        }
                    }
                }
            }
        });

    }

    //로딩창 창띄우는 함수
    public void showProgressDialog(String title ) {
        if (progressDialog==null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(title);
        progressDialog.setMessage("사진을 보내는 중입니다.");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //로딩창 지우는 함수
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }


    //채팅 RecyclerViewAdapter
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        final private RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        List<MessageModel> MessageModel_List= new ArrayList<MessageModel>(); //메세지 리스트


        //파일 경로 설정
        RecyclerViewAdapter() {
            File dir = new File(rootPath);
            if (!dir.exists()) {
                if (!ChatUtil.isPermissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return;
                }
                dir.mkdirs();
            }

            //setUnread2Read();
            startListening();
            //setUnread2Read();
        }

        //시작 세팅 함수
        public void startListening() {
            MessageModel_List.clear();
            Firestore = FirebaseFirestore.getInstance();

            CollectionReference roomRef = Firestore.collection("ROOMS").document(ChatRoomListModel_RoomUid).collection("MESSAGE");
            // my chatting room information
            listenerRegistration = roomRef.orderBy("MessageModel_DateOfManufacture").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {return;}

                    MessageModel messageModel;
                    for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
                        switch (change.getType()) {
                            case ADDED:
                                messageModel = change.getDocument().toObject(MessageModel.class);
                                //if (message.msg !=null & message.timestamp == null) {continue;} // FieldValue.serverTimestamp is so late

                                if (messageModel.getMessageModel_ReadUser().indexOf(currentUser_Uid) == -1) {
                                    messageModel.getMessageModel_ReadUser().add(currentUser_Uid);
                                    change.getDocument().getReference().update("MessageModel_ReadUser", messageModel.getMessageModel_ReadUser());
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
            if (currentUser_Uid.equals(messageModel.getMessageModel_UserUid()) ) {
                switch(messageModel.getMessage_MessageType()){
                    case "1": return R.layout.item_chatimage_right;
                    case "2": return R.layout.item_chatfile_right;
                    default:  return R.layout.item_chatmsg_right;
                }
            } else {
                switch(messageModel.getMessage_MessageType()){
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

            //변경
            if ("0".equals(messageModel.getMessage_MessageType())) {                                      // text message
                messageViewHolder.Message_TextView.setText(messageModel.getMessageModel_Message());
            }
            //사진 보이기
            else if ("1".equals(messageModel.getMessage_MessageType())) {                                      // file transfer
                messageViewHolder.realname = messageModel.getMessageModel_Message();
                Glide.with(getContext())
                        .load(StorageReference.child("ROOMS/"+ChatRoomListModel_RoomUid + "/" + messageModel.getMessageModel_Message()))
                        .apply(new RequestOptions().override(1000, 1000))
                        .into(messageViewHolder.Message_Image_ImageView);
            }
            ////

            //상대방 이름
            if (! currentUser_Uid.equals(messageModel.getMessageModel_UserUid())) {
                UserModel userModel = UserModel_UserList.get(messageModel.getMessageModel_UserUid());
                messageViewHolder.Message_NickName.setText(userModel.getUserModel_NickName());
                //상대방 프로필사진으로 바꾸기
                if (userModel.getUserModel_ProfileImage() != null) {
                    Glide.with(getContext()).load(userModel.getUserModel_ProfileImage()).centerCrop().override(500).into(messageViewHolder.User_Profile_Imalge);
                } else{
                    Glide.with(getContext()).load(R.drawable.user).into(messageViewHolder.User_Profile_Imalge);
                }
            }

            messageViewHolder.Message_Divider.setVisibility(View.INVISIBLE);
            messageViewHolder.Message_Divider.getLayoutParams().height = 0;
            messageViewHolder.Message_DateOfManufacture.setText("");
            if (messageModel.getMessageModel_DateOfManufacture()==null) {return;}

            String day = DateFormatDay.format( messageModel.getMessageModel_DateOfManufacture());
            String timestamp = DateFormatHour.format( messageModel.getMessageModel_DateOfManufacture());
            messageViewHolder.Message_DateOfManufacture.setText(timestamp);

            if (position==0) {
                messageViewHolder.Message_Divider_Date.setText(day);
                messageViewHolder.Message_Divider.setVisibility(View.VISIBLE);
                messageViewHolder.Message_Divider.getLayoutParams().height = 60;
            } else {
                MessageModel beforeMsg = MessageModel_List.get(position - 1);
                String beforeDay = DateFormatDay.format( beforeMsg.getMessageModel_DateOfManufacture() );

                if (!day.equals(beforeDay) && beforeDay != null) {
                    messageViewHolder.Message_Divider_Date.setText(day);
                    messageViewHolder.Message_Divider.setVisibility(View.VISIBLE);
                    messageViewHolder.Message_Divider.getLayoutParams().height = 60;
                }
            }
        }

        //상대방 유저가 읽었는지 확인한다. -> 채팅방 안의 1이랑 사라지는거는 MESSAGE의 MessageModel_ReadUser의 수로 파악
        void setReadCounter (MessageModel messageModel, final TextView textView) {
            int cnt = NumberOfUser - messageModel.getMessageModel_ReadUser().size();
            if (cnt > 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(String.valueOf(cnt));
            } else {
                textView.setVisibility(View.INVISIBLE);

                //현재 상대방 유저가 들어와있다면 상대방 룸 리스트의 현재 채팅방을 읽음 표시로 바꾼다. - 추가
                Firestore = FirebaseFirestore.getInstance();
                Firestore.collection("ROOMS").document(ChatRoomListModel_RoomUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {return;}
                        DocumentSnapshot document = task.getResult();
                        Map<String, Long> users = (Map<String, Long>) document.get("USERS");

                        users.put(currentUser_Uid, (long) 0);
                        document.getReference().update("USERS", users);
                    }
                });
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
        public String realname;                 //이거 뭔지 모르겠음

        public MessageViewHolder(View view) {
            super(view);
            User_Profile_Imalge = view.findViewById(R.id.User_Profile_Imalge);
            Message_TextView = view.findViewById(R.id.Message_TextView);
            Message_Image_ImageView = view.findViewById(R.id.Message_Image_ImageView);
            Message_DateOfManufacture = view.findViewById(R.id.Message_DateOfManufacture);
            Message_NickName = (TextView) view.findViewById(R.id.Message_NickName);
            Read_Check = view.findViewById(R.id.Read_Check);
            Message_Divider = view.findViewById(R.id.Message_Divider);
            Message_Divider_Date = view.findViewById(R.id.Message_Divider_Date);
            button_item = view.findViewById(R.id.button_item);
            msgLine_item = view.findViewById(R.id.msgLine_item);        // for file
            if (msgLine_item!=null) {
                msgLine_item.setOnClickListener(downloadClickListener);
            }
            /*
            if (Message_Image_ImageView !=null) {                                       // for image
                Message_Image_ImageView.setOnClickListener(imageClickListener);
            }
            */
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

                StorageReference.child("files/"+realname).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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
                intent.putExtra("RoomUid", ChatRoomListModel_RoomUid);
                intent.putExtra("realname", realname); //<- 이게 뭔지 정확히 모르겠음
                startActivity(intent);
            }
        };
    }

    public void backPressed() {
    }


    //Room_Uid를 넘겨주는 interface
    public interface RoomUidSetListener{
        void RoomUidSet(String RoomUid,String ToUid);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof RoomUidSetListener){
            roomUidSetListener = (RoomUidSetListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement RoomUidSetListener");
        }
    }
    @Override
    public void onDetach(){
        super.onDetach();
        roomUidSetListener = null;
    }

    //유저 나갈때 나갔습니다. 텍스트 띄우기 함수
    public void User_GoOut(String current_Uid, final String Market_Uid,final String Room_uid){
        Firestore.collection("USERS").document(current_Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                DocumentSnapshot document = task.getResult();
                String NickName = (String)document.get("UserModel_NickName");
                String msg = "("+ NickName + ")" + "  님이 나갔습니다.";
                sendMessage(msg, "0", null, Market_Uid,Room_uid);
            }
        });

    }

}