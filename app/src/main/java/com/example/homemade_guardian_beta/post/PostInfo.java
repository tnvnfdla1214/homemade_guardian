package com.example.homemade_guardian_beta.post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostInfo implements Serializable {                                                         // part10 : 게시물 정보 (21'30") // part17 : Serializable(29')
    private String title;
    private ArrayList<String> contents;
    private ArrayList<String> formats;
    private String uid;
    private Date createdAt;
    private String id;
    private String postID;

    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String uid, Date createdAt, String id){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.uid = uid;
        this.createdAt = createdAt;
        this.id = id;
    }
    ///
    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, Date createdAt, String uid, String postID){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.createdAt = createdAt;
        this.uid = uid;
        this.postID = postID;
    }
    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, Date createdAt, String uid){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.createdAt = createdAt;
        this.uid = uid;
    }

    public Map<String, Object> getPostInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("title",title);
        docData.put("contents",contents);
        docData.put("formats",formats);
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
    public ArrayList<String> getFormats(){
        return this.formats;
    }
    public void setFormats(ArrayList<String> formats){
        this.formats = formats;
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
}
