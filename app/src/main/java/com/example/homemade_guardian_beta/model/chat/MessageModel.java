package com.example.homemade_guardian_beta.model.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageModel implements Serializable {
    private String MessageModel_UserUid; //메세지 보낸 유저의 uid
    private String MessageModel_Message; //메세지 내용
    private String Message_MessageType;          // 0: msg, 1: image, 2: file
    private Date MessageModel_DateOfManufacture; //메세지 보냔 낳짜
    private List<String> MessageModel_ReadUser = new ArrayList<>(); //메세지 읽음 체크 <- 근데 보낸유저의 uid는 무조건 0, 반댄 1인듯 ??
    private String MessageModel_FileName; //메세지 파일 이름 <- 뺴야할듯
    private String MessageModel_FileSize; //메세지 파일 사이즈 <- 빼야할듯
    private String MessageModel_ImageCount;

    public MessageModel(){}

    public MessageModel(String MessageModel_UserUid, String MessageModel_Message, String Message_MessageType, Date MessageModel_DateOfManufacture){
        this.MessageModel_UserUid = MessageModel_UserUid;
        this.MessageModel_Message = MessageModel_Message;
        this.Message_MessageType = Message_MessageType;
        this.MessageModel_DateOfManufacture = MessageModel_DateOfManufacture;
    }

    public MessageModel(String MessageModel_UserUid, String MessageModel_Message, String Message_MessageType, Date MessageModel_DateOfManufacture,String MessageModel_FileName, String MessageModel_FileSize){
        this.MessageModel_UserUid = MessageModel_UserUid;
        this.MessageModel_Message = MessageModel_Message;
        this.Message_MessageType = Message_MessageType;
        this.MessageModel_DateOfManufacture = MessageModel_DateOfManufacture;
        this.MessageModel_FileName = MessageModel_FileName;
        this.MessageModel_FileSize = MessageModel_FileSize;
    }

    public String getMessageModel_UserUid() {
        return MessageModel_UserUid;
    }

    public void setMessageModel_UserUid(String messageModel_UserUid) {
        this.MessageModel_UserUid = messageModel_UserUid;
    }

    public String getMessageModel_Message() {
        return MessageModel_Message;
    }

    public void setMessageModel_Message(String messageModel_Message) {
        this.MessageModel_Message = messageModel_Message;
    }

    public String getMessage_MessageType() {
        return Message_MessageType;
    }

    public void setMessage_MessageType(String message_MessageType) {
        this.Message_MessageType = message_MessageType;
    }

    public Date getMessageModel_DateOfManufacture() {
        return MessageModel_DateOfManufacture;
    }

    public void setMessageModel_DateOfManufacture(Date messageModel_DateOfManufacture) {
        this.MessageModel_DateOfManufacture = messageModel_DateOfManufacture;
    }

    public List<String> getMessageModel_ReadUser() {
        return MessageModel_ReadUser;
    }

    public void setMessageModel_ReadUser(List<String> messageModel_ReadUser) {
        this.MessageModel_ReadUser = messageModel_ReadUser;
    }

    public String getMessageModel_FileName() {
        return MessageModel_FileName;
    }

    public void setMessageModel_FileName(String messageModel_FileName) {
        this.MessageModel_FileName = messageModel_FileName;
    }

    public String getMessageModel_FileSize() {
        return MessageModel_FileSize;
    }

    public void setMessageModel_FileSize(String messageModel_FileSize) {
        this.MessageModel_FileSize = messageModel_FileSize;
    }
    public String getMessageModel_ImageCount() {
        return MessageModel_ImageCount;
    }

    public void setMessageModel_ImageCount(String MessageModel_ImageCount) {
        this.MessageModel_ImageCount = MessageModel_ImageCount;
    }
}
