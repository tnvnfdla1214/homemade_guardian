package com.example.homemade_guardian_beta.model.market;

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

public class MarketModel implements Serializable {                                                         // part10 : 게시물 정보 (21'30") // part17 : Serializable(29')
    private String MarketModel_Title;                 //게시물 제목
    private String MarketModel_Host_Uid;              //게시물 작성자의 Uid
    private String MarketModel_Post_Uid;              //게시물의 Uid
    private Date MarketModel_DateOfManufacture;       //게시물의 작성 시간
    private ArrayList<String> MarketModel_ImageList;  //게시물의 사진 리스트
    private String MarketModel_Text;                //게시물의 글
    private String MarketModel_Category;            //게시물의 카테고리
    private ArrayList<String> MarketModel_LikeList;  //게시물의 좋아요 리스트
    private String MarketModel_HotPost;
    private String MarketModel_reservation;             //게시자가 예약을 할때 안했을때는 0 했다면 1
    private String MarketModel_deal;                    //게시물이 거래 안했을때 0, 했으면 1

    public MarketModel(String MarketModel_Title, String MarketModel_Text, ArrayList<String> MarketModel_ImageList, Date MarketModel_DateOfManufacture, String MarketModel_Host_Uid, String MarketModel_Post_Uid, String MarketModel_Category, ArrayList<String> MarketModel_LikeList, String MarketModel_HotPost, String MarketModel_reservation, String MarketModel_deal){
        this.MarketModel_Title = MarketModel_Title;
        this.MarketModel_Text = MarketModel_Text;
        this.MarketModel_ImageList = MarketModel_ImageList;
        this.MarketModel_DateOfManufacture = MarketModel_DateOfManufacture;
        this.MarketModel_Host_Uid = MarketModel_Host_Uid;
        this.MarketModel_Post_Uid = MarketModel_Post_Uid;
        this.MarketModel_Category = MarketModel_Category;
        this.MarketModel_LikeList = MarketModel_LikeList;
        this.MarketModel_HotPost = MarketModel_HotPost;
        this.MarketModel_reservation = MarketModel_reservation;
        this.MarketModel_deal = MarketModel_deal;
    }
    public MarketModel(String MarketModel_Title, String MarketModel_Text, Date MarketModel_DateOfManufacture, String MarketModel_Host_Uid, String MarketModel_Post_Uid, String MarketModel_Category, ArrayList<String> MarketModel_LikeList, String MarketModel_HotPost, String MarketModel_reservation, String MarketModel_deal){
        this.MarketModel_Title = MarketModel_Title;
        this.MarketModel_Text = MarketModel_Text;
        this.MarketModel_DateOfManufacture = MarketModel_DateOfManufacture;
        this.MarketModel_Host_Uid = MarketModel_Host_Uid;
        this.MarketModel_Post_Uid = MarketModel_Post_Uid;
        this.MarketModel_Category = MarketModel_Category;
        this.MarketModel_LikeList = MarketModel_LikeList;
        this.MarketModel_HotPost = MarketModel_HotPost;
        this.MarketModel_reservation = MarketModel_reservation;
        this.MarketModel_deal = MarketModel_deal;
    }

    public Map<String, Object> getPostInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("MarketModel_Title", MarketModel_Title);
        docData.put("MarketModel_Text", MarketModel_Text);
        docData.put("MarketModel_ImageList", MarketModel_ImageList);
        docData.put("MarketModel_Host_Uid", MarketModel_Host_Uid);
        docData.put("MarketModel_DateOfManufacture", MarketModel_DateOfManufacture);
        docData.put("MarketModel_Post_Uid", MarketModel_Post_Uid);
        docData.put("MarketModel_Category", MarketModel_Category);
        docData.put("MarketModel_LikeList", MarketModel_LikeList);
        docData.put("MarketModel_HotPost", MarketModel_HotPost);
        docData.put("MarketModel_reservation", MarketModel_reservation);
        docData.put("MarketModel_deal", MarketModel_deal);

        return  docData;
    }

    public MarketModel(){ }
    public String getMarketModel_Title(){
        return this.MarketModel_Title;
    }
    public void setMarketModel_Title(String MarketModel_Title){ this.MarketModel_Title = MarketModel_Title; }
    public ArrayList<String> getMarketModel_ImageList(){
        return this.MarketModel_ImageList;
    }
    public void setMarketModel_ImageList(ArrayList<String> MarketModel_ImageList){ this.MarketModel_ImageList = MarketModel_ImageList; }
    public String getMarketModel_Host_Uid(){
        return this.MarketModel_Host_Uid;
    }
    public void setMarketModel_Host_Uid(String MarketModel_Host_Uid){ this.MarketModel_Host_Uid = MarketModel_Host_Uid; }
    public Date getMarketModel_DateOfManufacture(){
        return this.MarketModel_DateOfManufacture;
    }
    public void setMarketModel_DateOfManufacture(Date MarketModel_DateOfManufacture){ this.MarketModel_DateOfManufacture = MarketModel_DateOfManufacture; }
    public String getMarketModel_Post_Uid(){
        return this.MarketModel_Post_Uid;
    }
    public void setMarketModel_Post_Uid(String MarketModel_Post_Uid){ this.MarketModel_Post_Uid = MarketModel_Post_Uid; }
    public String getMarketModel_Text(){return this.MarketModel_Text;}
    public void setMarketModel_Text(String MarketModel_Text){this.MarketModel_Text = MarketModel_Text;}
    public String getMarketModel_Category(){return this.MarketModel_Category;}
    public void setMarketModel_Category(String MarketModel_Category){this.MarketModel_Category = MarketModel_Category;}
    public ArrayList<String> getMarketModel_LikeList(){
        return this.MarketModel_LikeList;
    }
    public void setMarketModel_LikeList(ArrayList<String> MarketModel_LikeList){ this.MarketModel_LikeList = MarketModel_LikeList; }
    public String getMarketModel_HotPost(){
        return this.MarketModel_HotPost;
    }
    public void setMarketModel_HotPost(String MarketModel_HotPost){ this.MarketModel_HotPost = MarketModel_HotPost; }
    public String getMarketModel_reservation(){
        return this.MarketModel_reservation;
    }
    public void setMarketModel_reservation(String MarketModel_reservation){ this.MarketModel_reservation = MarketModel_reservation; }
    public String getMarketModel_deal(){
        return this.MarketModel_deal;
    }
    public void setMarketModel_deal(String MarketModel_deal){ this.MarketModel_deal = MarketModel_deal; }
}
