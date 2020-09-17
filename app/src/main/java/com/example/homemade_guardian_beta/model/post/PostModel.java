package com.example.homemade_guardian_beta.model.post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//1. title -> PostModel_Title : (모델명)_ 기입
//2. uid -> PostModel_Host_Uid : 만든이 ->Host , 보는이 -> Guest
//3. createdAt -> PostModel_DateOfManufacture : 모호한 영어는 번역기
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

public class PostModel implements Serializable {                                                         // part10 : 게시물 정보 (21'30") // part17 : Serializable(29')
    private String PostModel_Title;                 //게시물 제목
    private String PostModel_Host_Uid;              //게시물 작성자의 Uid
    private String PostModel_Post_Uid;              //게시물의 Uid
    private Date PostModel_DateOfManufacture;       //게시물의 작성 시간
    private ArrayList<String> PostModel_ImageList;  //게시물의 사진 리스트
    private String PostModel_Text;                //게시물의 글
    private String PostModel_Category;            //게시물의 카테고리
    private ArrayList<String> PostModel_LikeList;  //게시물의 좋아요 리스트
    private String PostModel_HotPost;

    public PostModel(String PostModel_Title, String PostModel_Text ,ArrayList<String> PostModel_ImageList, Date PostModel_DateOfManufacture, String PostModel_Host_Uid, String PostModel_Post_Uid, String PostModel_Category,ArrayList<String> PostModel_LikeList, String PostModel_HotPost){
        this.PostModel_Title = PostModel_Title;
        this.PostModel_Text = PostModel_Text;
        this.PostModel_ImageList = PostModel_ImageList;
        this.PostModel_DateOfManufacture = PostModel_DateOfManufacture;
        this.PostModel_Host_Uid = PostModel_Host_Uid;
        this.PostModel_Post_Uid = PostModel_Post_Uid;
        this.PostModel_Category = PostModel_Category;
        this.PostModel_LikeList = PostModel_LikeList;
        this.PostModel_HotPost = PostModel_HotPost;

    }
    public PostModel(String PostModel_Title, String PostModel_Text ,Date PostModel_DateOfManufacture, String PostModel_Host_Uid, String PostModel_Post_Uid, String PostModel_Category, ArrayList<String> PostModel_LikeList, String PostModel_HotPost){
        this.PostModel_Title = PostModel_Title;
        this.PostModel_Text = PostModel_Text;
        this.PostModel_DateOfManufacture = PostModel_DateOfManufacture;
        this.PostModel_Host_Uid = PostModel_Host_Uid;
        this.PostModel_Post_Uid = PostModel_Post_Uid;
        this.PostModel_Category = PostModel_Category;
        this.PostModel_LikeList = PostModel_LikeList;
        this.PostModel_HotPost = PostModel_HotPost;
    }

    public Map<String, Object> getPostInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("PostModel_Title", PostModel_Title);
        docData.put("PostModel_Text", PostModel_Text);
        docData.put("PostModel_ImageList", PostModel_ImageList);
        docData.put("PostModel_Host_Uid", PostModel_Host_Uid);
        docData.put("PostModel_DateOfManufacture", PostModel_DateOfManufacture);
        docData.put("PostModel_Post_Uid", PostModel_Post_Uid);
        docData.put("PostModel_Category", PostModel_Category);
        docData.put("PostModel_LikeList", PostModel_LikeList);
        docData.put("PostModel_HotPost", PostModel_HotPost);

        return  docData;
    }

    public PostModel(){ }
    public String getPostModel_Title(){
        return this.PostModel_Title;
    }
    public void setPostModel_Title(String PostModel_Title){ this.PostModel_Title = PostModel_Title; }
    public ArrayList<String> getPostModel_ImageList(){
        return this.PostModel_ImageList;
    }
    public void setPostModel_ImageList(ArrayList<String> PostModel_ImageList){ this.PostModel_ImageList = PostModel_ImageList; }
    public String getPostModel_Host_Uid(){
        return this.PostModel_Host_Uid;
    }
    public void setPostModel_Host_Uid(String PostModel_Host_Uid){ this.PostModel_Host_Uid = PostModel_Host_Uid; }
    public Date getPostModel_DateOfManufacture(){
        return this.PostModel_DateOfManufacture;
    }
    public void setPostModel_DateOfManufacture(Date PostModel_DateOfManufacture){ this.PostModel_DateOfManufacture = PostModel_DateOfManufacture; }
    public String getPostModel_Post_Uid(){
        return this.PostModel_Post_Uid;
    }
    public void setPostModel_Post_Uid(String PostModel_Post_Uid){ this.PostModel_Post_Uid = PostModel_Post_Uid; }
    public String getPostModel_Text(){return this.PostModel_Text;}
    public void setPostModel_Text(String postModel_Text){this.PostModel_Text = postModel_Text;}
    public String getPostModel_Category(){return this.PostModel_Category;}
    public void setPostModel_Category(String PostModel_Category){this.PostModel_Category = PostModel_Category;}
    public ArrayList<String> getPostModel_LikeList(){
        return this.PostModel_LikeList;
    }
    public void setPostModel_LikeList(ArrayList<String> PostModel_LikeList){ this.PostModel_LikeList = PostModel_LikeList; }
    public String getPostModel_HotPost(){
        return this.PostModel_HotPost;
    }
    public void setPostModel_HotPost(String PostModel_HotPost){ this.PostModel_HotPost = PostModel_HotPost; }
}
