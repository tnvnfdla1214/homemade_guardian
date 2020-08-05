package com.example.homemade_guardian_beta.chat.model;

//address : 지역(성남), birthDay : 생일 (950310), createdID : 가입 날짜 (2020년 8월 5일 오후 11시 54분 43초 UTC+9)
//name : 이름 (이석규), token : ? (null) , photoUrl : 스토리지 저장 경로 ("https://firebasestorage.googleapis.com/v0/b/homemade-guardian-beta.appspot.com/o/user%2FBMpq3vmEN6Pi4by5Sm283ZEIdNw1%2FprofileImage.jpg?alt=media&token=d8f58529-4580-44d3-9658-eca84cc4b424")
//uid : Auth고유 번호 ("BMpq3vmEN6Pi4by5Sm283ZEIdNw1"), userid : (tnvnfdla12@gmail.com), usermsg : 상태메세지 (안녕하세요. 좋은하루 보내세요.)
//usernm : 이름 -> 차후 닉네임(NickName)으로 변경해야함 (tnvnfdla12 -> 번듯한 오크, null시 tnvnfdla12로 설정)

import java.util.Date;

public class UserModel {
    private String userid;
    private String uid;
    private String usernm;
    private String token;
    private String userphoto; //<-변경 사항
    private String usermsg;
    //여기부터 UserInfo
    private String name;
    private String phoneNumber;
    private String birthDay;
    private String address;
    private String photoUrl;
    private Date createdID;

    public UserModel(String name, String phoneNumber, String birthDay, String address, Date createdID, String photoUrl){     // part5 : 생성자 초기화 (7')
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.createdID = createdID;                                                                         // + : 사용자 리스트 수정 (날짜 정보 추가)
        this.photoUrl = photoUrl;
    }

    public UserModel(String name, String phoneNumber, String birthDay, Date createdID, String address){      // + : 사용자 리스트 수정(날짜 정보 추가)
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.createdID = createdID;
    }

    public UserModel(){

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsernm() {
        return usernm;
    }

    public void setUsernm(String usernm) {
        this.usernm = usernm;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getUsermsg() {
        return usermsg;
    }

    public void setUsermsg(String usermsg) {
        this.usermsg = usermsg;
    }

    //추가
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDay(){
        return this.birthDay;
    }

    public void setBirthDay(String birthDay){
        this.birthDay = birthDay;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getphotoUrl(){
        return this.photoUrl;
    }

    public void setphotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
    
    public Date getCreatedID() { return createdID; }

    public void setCreatedID(Date createdID) { this.createdID = createdID; }
}
