package com.example.homemade_guardian_beta.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comment {
    private String comment;
    private String uid;
    private Date timestamp;
    private String name;
    private String commentID;
    private String postID;

    public Comment(String comment, String uid, Date timestamp, String name,String commentID,String postID){
        this.comment = comment;
        this.uid = uid;
        this.timestamp = timestamp;
        this.name = name;
        this.commentID = commentID;
        this.postID = postID;

    }




    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getComment() { return comment; }
    public void setComment(String msg) { this.comment = comment; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public String getName() { return name; }
    public void setName(String uid) { this.name = name; }
    public String getCommentID() { return commentID; }
    public void setCommentID(String commentID) { this.commentID = commentID; }
    public String getPostID() { return postID; }
    public void setPostID(String postID) { this.postID = postID; }

}
