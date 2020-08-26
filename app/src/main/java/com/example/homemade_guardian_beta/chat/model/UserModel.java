package com.example.homemade_guardian_beta.chat.model;

//address : 지역(성남), birthDay : 생일 (950310), createdID : 가입 날짜 (2020년 8월 5일 오후 11시 54분 43초 UTC+9)
//name : 이름 (이석규), token : ? (null) , photoUrl : 스토리지 저장 경로 ("https://firebasestorage.googleapis.com/v0/b/homemade-guardian-beta.appspot.com/o/user%2FBMpq3vmEN6Pi4by5Sm283ZEIdNw1%2FprofileImage.jpg?alt=media&token=d8f58529-4580-44d3-9658-eca84cc4b424")
//uid : Auth고유 번호 ("BMpq3vmEN6Pi4by5Sm283ZEIdNw1"), userid : (tnvnfdla12@gmail.com), usermsg : 상태메세지 (안녕하세요. 좋은하루 보내세요.)
//usernm : 이름 -> 차후 닉네임(NickName)으로 변경해야함 (tnvnfdla12 -> 번듯한 오크, null시 tnvnfdla12로 설정)

import java.util.Date;

public class UserModel {
    private String UserModel_ID; //아이디
    private String UserModel_Uid; //uid
    private String userModel_NickName; //지금은 tnvnfdla1214 -> 번듯한 오크 바뀌어야함
    private String UserModel_StateMassage; //상태메세지
    private String UserModel_Name; //이름
    private String UserModel_PhoneNumber; //전화번호
    private String UserModel_BirthDay; //생일
    private String UserModel_Address; //주소
    private String UserModel_ProfileImage; //프로필 사진
    private Date UserModel_DateOfManufacture; //생성일자

    public UserModel(String UserModel_Name, String UserModel_PhoneNumber, String UserModel_BirthDay, String UserModel_Address, Date UserModel_DateOfManufacture, String UserModel_ProfileImage){     // part5 : 생성자 초기화 (7')
        this.UserModel_Name = UserModel_Name;
        this.UserModel_PhoneNumber = UserModel_PhoneNumber;
        this.UserModel_BirthDay = UserModel_BirthDay;
        this.UserModel_Address = UserModel_Address;
        this.UserModel_DateOfManufacture = UserModel_DateOfManufacture;                                                                         // + : 사용자 리스트 수정 (날짜 정보 추가)
        this.UserModel_ProfileImage = UserModel_ProfileImage;
    }

    public UserModel(String UserModel_Name, String UserModel_PhoneNumber, String UserModel_BirthDay, Date UserModel_DateOfManufacture, String UserModel_Address){      // + : 사용자 리스트 수정(날짜 정보 추가)
        this.UserModel_Name = UserModel_Name;
        this.UserModel_PhoneNumber = UserModel_PhoneNumber;
        this.UserModel_BirthDay = UserModel_BirthDay;
        this.UserModel_Address = UserModel_Address;
        this.UserModel_DateOfManufacture = UserModel_DateOfManufacture;
    }

    public UserModel(){

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
        return userModel_NickName;
    }

    public void setUserModel_NickName(String userModel_NickName) {
        this.userModel_NickName = userModel_NickName;
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

    public String getphotoUrl(){
        return this.UserModel_ProfileImage;
    }

    public void setphotoUrl(String photoUrl){
        this.UserModel_ProfileImage = photoUrl;
    }
    
    public Date getUserModel_DateOfManufacture() { return UserModel_DateOfManufacture; }

    public void setUserModel_DateOfManufacture(Date userModel_DateOfManufacture) { this.UserModel_DateOfManufacture = userModel_DateOfManufacture; }
}
