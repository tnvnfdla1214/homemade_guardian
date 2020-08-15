package com.example.homemade_guardian_beta.post;

import java.util.Date;

public class CommentModel {
    private String comment;
    private String uid;
    private Date timestamp;
    private String name;
    private String commentID;
    private String postID;
    private String commentphotoUrl;


    public CommentModel(String comment, String uid, Date timestamp, String name, String commentID, String postID){
        this.comment = comment;
        this.uid = uid;
        this.timestamp = timestamp;
        this.name = name;
        this.commentID = commentID;
        this.postID = postID;

    }

    public CommentModel(){

    }




    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCommentID() { return commentID; }
    public void setCommentID(String commentID) { this.commentID = commentID; }
    public String getPostID() { return postID; }
    public void setPostID(String postID) { this.postID = postID; }
    public String getcommentphotoUrl(){
        return this.commentphotoUrl;
    }
    public void setcommentphotoUrl(String commentphotoUrl){
        this.commentphotoUrl = commentphotoUrl;
    }

}
