package com.example.homemade_guardian_beta.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserModel {
    private String UserModel_ID; //아이디
    private String UserModel_Uid; //uid
    private String UserModel_NickName; //지금은 tnvnfdla1214 -> 번듯한 오크 바뀌어야함
    private String UserModel_StateMassage; //상태메세지
    private String UserModel_Name; //이름
    private String UserModel_PhoneNumber; //전화번호
    private String UserModel_BirthDay; //생일
    private String UserModel_Address; //주소
    private String UserModel_ProfileImage; //프로필 사진
    private Date UserModel_DateOfManufacture; //생성일자
    private ArrayList<String> UserModel_UnReViewList;  //리뷰를 작성하지 않은 리스트

    public UserModel(String UserModel_Name, String UserModel_PhoneNumber, String UserModel_BirthDay, String UserModel_Address, Date UserModel_DateOfManufacture, String UserModel_ProfileImage, ArrayList<String> UserModel_UnReViewList){     // part5 : 생성자 초기화 (7')
        this.UserModel_Name = UserModel_Name;
        this.UserModel_PhoneNumber = UserModel_PhoneNumber;
        this.UserModel_BirthDay = UserModel_BirthDay;
        this.UserModel_Address = UserModel_Address;
        this.UserModel_DateOfManufacture = UserModel_DateOfManufacture;                                                                         // + : 사용자 리스트 수정 (날짜 정보 추가)
        this.UserModel_ProfileImage = UserModel_ProfileImage;
        this.UserModel_UnReViewList = UserModel_UnReViewList;

    }

    public UserModel(String UserModel_Name, String UserModel_PhoneNumber, String UserModel_BirthDay, Date UserModel_DateOfManufacture, String UserModel_Address, ArrayList<String> UserModel_UnReViewList){      // + : 사용자 리스트 수정(날짜 정보 추가)
        this.UserModel_Name = UserModel_Name;
        this.UserModel_PhoneNumber = UserModel_PhoneNumber;
        this.UserModel_BirthDay = UserModel_BirthDay;
        this.UserModel_Address = UserModel_Address;
        this.UserModel_DateOfManufacture = UserModel_DateOfManufacture;
        this.UserModel_UnReViewList = UserModel_UnReViewList;
    }

    public UserModel(){
    }

    public Map<String, Object> getUserInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("UserModel_ID", UserModel_ID);
        docData.put("UserModel_Uid", UserModel_Uid);
        docData.put("UserModel_NickName", UserModel_NickName);
        docData.put("UserModel_StateMassage", UserModel_StateMassage);
        docData.put("UserModel_Name", UserModel_Name);
        docData.put("UserModel_PhoneNumber", UserModel_PhoneNumber);
        docData.put("UserModel_BirthDay", UserModel_BirthDay);
        docData.put("UserModel_Address", UserModel_Address);
        docData.put("UserModel_ProfileImage", UserModel_ProfileImage);
        docData.put("UserModel_DateOfManufacture", UserModel_DateOfManufacture);
        docData.put("UserModel_UnReViewList", UserModel_UnReViewList);
        return  docData;
    }

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

    public String getUserModel_StateMassage() {
        return UserModel_StateMassage;
    }

    public void setUserModel_StateMassage(String userModel_StateMassage) {
        this.UserModel_StateMassage = userModel_StateMassage;
    }

    //추가
    public String getUserModel_Name(){
        return this.UserModel_Name;
    }

    public void setUserModel_Name(String userModel_Name){
        this.UserModel_Name = userModel_Name;
    }

    public String getUserModel_PhoneNumber(){
        return this.UserModel_PhoneNumber;
    }

    public void setUserModel_PhoneNumber(String userModel_PhoneNumber){
        this.UserModel_PhoneNumber = userModel_PhoneNumber;
    }

    public String getUserModel_BirthDay(){
        return this.UserModel_BirthDay;
    }

    public void setUserModel_BirthDay(String userModel_BirthDay){
        this.UserModel_BirthDay = userModel_BirthDay;
    }

    public String getUserModel_Address(){
        return this.UserModel_Address;
    }

    public void setUserModel_Address(String userModel_Address){
        this.UserModel_Address = userModel_Address;
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
