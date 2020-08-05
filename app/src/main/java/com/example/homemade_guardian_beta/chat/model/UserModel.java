package com.example.homemade_guardian_beta.chat.model;

// token : null , uid : "0prEeLtTIlfD5as6NyUYzHLfA3p2" , userid : "tnvnfdla12@gmail.com"
// usermsg : "..." , usernm : "tnvnfdla12" , userphoto : null

import java.util.Date;

public class UserModel {
    private String userid;
    private String uid;
    private String usernm;
    private String token;
    private String userphoto;
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
