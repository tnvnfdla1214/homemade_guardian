package com.example.homemade_guardian_beta.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserModel {
    private String UserModel_ID; //아이디
    private String UserModel_Uid; //uid
    private String UserModel_NickName; //지금은 tnvnfdla1214 -> 번듯한 오크 만약 이용자가 임력을 하지 않을 시 전에꺼로 한다.
    private String UserModel_BirthDay; //생일
    private int UserModel_University; //학교 0 : 홍대 세종 , 1 : 고대세종
    private String UserModel_ProfileImage; //프로필 사진
    private Date UserModel_DateOfManufacture; //생성일자
    private ArrayList<String> UserModel_UnReViewList;  //리뷰를 작성하지 않은 리스트

    public UserModel(String UserModel_NickName, String UserModel_BirthDay, Date UserModel_DateOfManufacture, int UserModel_University , String UserModel_ProfileImage, ArrayList<String> UserModel_UnReViewList){     // part5 : 생성자 초기화 (7')
        this.UserModel_NickName = UserModel_NickName;
        this.UserModel_BirthDay = UserModel_BirthDay;
        this.UserModel_University = UserModel_University;
        this.UserModel_ProfileImage = UserModel_ProfileImage;
        this.UserModel_DateOfManufacture = UserModel_DateOfManufacture;    // + : 사용자 리스트 수정 (날짜 정보 추가)
        this.UserModel_UnReViewList = UserModel_UnReViewList;

    }

    public UserModel(String UserModel_NickName,String UserModel_BirthDay, Date UserModel_DateOfManufacture, int UserModel_University, ArrayList<String> UserModel_UnReViewList){      // + : 사용자 리스트 수정(날짜 정보 추가)
        this.UserModel_NickName = UserModel_NickName;
        this.UserModel_BirthDay = UserModel_BirthDay;
        this.UserModel_DateOfManufacture = UserModel_DateOfManufacture;
        this.UserModel_University = UserModel_University;
        this.UserModel_UnReViewList = UserModel_UnReViewList;
    }

    public UserModel(){
    }

    //이걸로 후기 작성 모델 만들어도 될듯
    /*
    public Map<String, Object> getUserInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("UserModel_ID", UserModel_ID);
        docData.put("UserModel_Uid", UserModel_Uid);
        docData.put("UserModel_NickName", UserModel_NickName);
        docData.put("UserModel_BirthDay", UserModel_BirthDay);
        docData.put("UserModel_Address", UserModel_University);
        docData.put("UserModel_ProfileImage", UserModel_ProfileImage);
        docData.put("UserModel_DateOfManufacture", UserModel_DateOfManufacture);
        docData.put("UserModel_UnReViewList", UserModel_UnReViewList);
        return  docData;
    }

     */

    public String getUserModel_ID() {
        return UserModel_ID;
    }

    public void setUserModel_ID(String userModel_ID) {
        this.UserModel_ID = userModel_ID;
    }

    public String getUserModel_Uid() {
        return UserModel_Uid;
    }

    public void setUserModel_Uid(String userModel_Uid) {
        this.UserModel_Uid = userModel_Uid;
    }

    public String getUserModel_NickName() {
        return UserModel_NickName;
    }

    public void setUserModel_NickName(String userModel_NickName) {
        this.UserModel_NickName = userModel_NickName;
    }

    public String getUserModel_BirthDay(){
        return this.UserModel_BirthDay;
    }

    public void setUserModel_BirthDay(String userModel_BirthDay){
        this.UserModel_BirthDay = userModel_BirthDay;
    }

    public int getUserModel_University(){
        return this.UserModel_University;
    }

    public void setUserModel_University(int UserModel_University){
        this.UserModel_University = UserModel_University;
    }

    public String getUserModel_ProfileImage(){
        return this.UserModel_ProfileImage;
    }

    public void setUserModel_ProfileImage(String photoUrl){
        this.UserModel_ProfileImage = photoUrl;
    }
    
    public Date getUserModel_DateOfManufacture() { return UserModel_DateOfManufacture; }

    public void setUserModel_DateOfManufacture(Date userModel_DateOfManufacture) { this.UserModel_DateOfManufacture = userModel_DateOfManufacture; }

    public ArrayList<String> getUserModel_UnReViewList(){
        return this.UserModel_UnReViewList;
    }
    public void setUserModel_UnReViewList(ArrayList<String> UserModel_UnReViewList){ this.UserModel_UnReViewList = UserModel_UnReViewList; }
}
