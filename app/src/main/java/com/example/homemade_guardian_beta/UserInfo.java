package com.example.homemade_guardian_beta;
import java.util.Date;

public class UserInfo {
    private String name;
    private String phoneNumber;
    private String birthDay;
    private String address;
    private String photoUrl;
    private Date createdID;                                                                                 // + : 사용자 리스트 수정 (날짜 정보 추가)

    public UserInfo(String name, String phoneNumber, String birthDay, String address, Date createdID, String photoUrl){     // part5 : 생성자 초기화 (7')
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.createdID = createdID;                                                                         // + : 사용자 리스트 수정 (날짜 정보 추가)
        this.photoUrl = photoUrl;
    }

    public UserInfo(String name, String phoneNumber, String birthDay, Date createdID, String address){      // + : 사용자 리스트 수정(날짜 정보 추가)
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.createdID = createdID;
    }

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
    public String getPhotoUrl(){
        return this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }

    public Date getCreatedID() { return createdID; }
    public void setCreatedID(Date createdID) { this.createdID = createdID; }
}
