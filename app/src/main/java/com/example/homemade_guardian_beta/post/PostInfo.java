package com.example.homemade_guardian_beta.post;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//1. title -> PostModel_Title : (모델명)_ 기입
//2. uid -> PostModel_Host_Uid : 만든이 ->Host , 보는이 -> Guest
//3. createdAt -> PostModel_Date_Of_Manufacture : 모호한 영어는 번역기
//4. postID -> PostModel_Post_Uid : (모델명)_(이름)_Uid -> 해당객체의 Uid
//5. contents -> PostModel_ImageList : 포토를 이미지로 바꾸고 한장은 이미지, 여러장은 이미지리스트
//6. 해당 모델를 바꾼후 최상단에 변수명의 설명 적기
//7. getter,setter를 붙여놓기
//8. put 안쓰기
//9. CurrentUser -> 현재 앱 사용자 , FirebaseStore -> 등등은 앞에 Firebase 적기,docRefe2 -> docRefe_USERS_UserModel_User_Uid
//10. 파이어베이스 컬렉션 이름 : 대문자이름+S
//11. 선언 규칙 :
//              (1). 1.클래스, 2.변수및 배열, 3.Xml데이터(3-1버튼,3-2텍스트), 4.파이어베이스 관련 선언, 5.잡자한 변수들 *TAG지우기
//              (2). 위에 서열 바뀔때마다 한칸씩 띄기
//              (3). 선언된 변수; // 설명
//12. chttingroom -> (1) chat_Button -> (기능)_Button : 기능을 적고 case로 정리후 해당 함수에 주석으로 기능 설명
//                   (2) 마지막에는 Button,EditText로 끝내기
//13. 함수이름 잘정하고 주석으로 기능 설명
//14. 좆같은 태그 다 지우기
//15. 사용하지 않는 class 지우기
//16. 해당 클래스위에 어떠한 클래스인지 정리
//17. 공부용 주석은 남기고 혹여나 변경사항 있을시 자체적으로 새로 정리
//post-석규, chat - 민규 먼저끝낸사람이 photo하기, 나증에한사람은 짤짤이
public class PostInfo implements Serializable {                                                         // part10 : 게시물 정보 (21'30") // part17 : Serializable(29')
    private String title;
    private String uid;
    private Date createdAt;
    private String id;
    private String postID;
    private ArrayList<Uri> imageList = new ArrayList<Uri>();
    private String text;

    private ArrayList<String> contents;

    public PostInfo(String title, ArrayList<String> contents,  String uid, Date createdAt, String id){
        this.title = title;
        this.contents = contents;
        this.uid = uid;
        this.createdAt = createdAt;
        this.id = id;
    }

    /////////
    public PostInfo(String title, ArrayList<String> contents, Date createdAt, String uid, String postID){
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.uid = uid;
        this.postID = postID;
    }

    public PostInfo(String title, ArrayList<String> contents,  Date createdAt, String uid){
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.uid = uid;
    }

    public Map<String, Object> getPostInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("title",title);
        docData.put("contents",contents);
        docData.put("uid",uid);
        docData.put("createdAt",createdAt);
        docData.put("postID",postID);
        return  docData;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public ArrayList<String> getContents(){
        return this.contents;
    }
    public void setContents(ArrayList<String> contents){
        this.contents = contents;
    }
    public String getuid(){
        return this.uid;
    }
    public void setuid(String publisher){
        this.uid = publisher;
    }
    public Date getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }
    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getPostID(){
        return this.postID;
    }
    public void setPostID(String title){
        this.postID = postID;
    }

    public ArrayList<Uri> getImageList(){
        return this.imageList;
    }
    public void setImageList(ArrayList<Uri> imageList){
        this.imageList = imageList;
    }
    public String getText(){
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }

}
