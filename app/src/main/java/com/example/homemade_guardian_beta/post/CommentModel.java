package com.example.homemade_guardian_beta.post;

import java.util.Date;

public class CommentModel {
    private String CommentModel_Comment;    //댓글 내용
    private String CommentModel_Host_Image; //댓글 작성자 프로필
    private String CommentModel_Host_Uid;   //댓글 작성자 Uid
    private String CommentModel_Host_Name;  //댓글 작성자 이름
    private String CommentModel_Post_Uid;   //댓글이 쓰여진 게시물의 Uid
    private String CommentModel_Comment_Uid;//댓글의 Uid
    private Date CommentModel_DateOfManufacture;    //댓글 작성 시간

    public CommentModel(String CommentModel_Host_Uid, String CommentModel_Comment, Date CommentModel_DateOfManufacture, String CommentModel_Host_Name, String CommentModel_Comment_Uid, String CommentModel_Post_Uid, String CommentModel_Host_Image){
        this.CommentModel_Host_Uid = CommentModel_Host_Uid;
        this.CommentModel_Comment = CommentModel_Comment;
        this.CommentModel_DateOfManufacture = CommentModel_DateOfManufacture;
        this.CommentModel_Host_Name = CommentModel_Host_Name;
        this.CommentModel_Comment_Uid = CommentModel_Comment_Uid;
        this.CommentModel_Post_Uid = CommentModel_Post_Uid;
        this.CommentModel_Host_Image = CommentModel_Host_Image;
    }

    public CommentModel(){ }
    public String getCommentModel_Host_Uid() { return CommentModel_Host_Uid; }
    public void setCommentModel_Host_Uid(String CommentModel_Host_Uid) { this.CommentModel_Host_Uid = CommentModel_Host_Uid; }
    public String getCommentModel_Comment() { return CommentModel_Comment; }
    public void setCommentModel_Comment(String CommentModel_Comment) { this.CommentModel_Comment = CommentModel_Comment; }
    public Date getCommentModel_DateOfManufacture() { return CommentModel_DateOfManufacture; }
    public void setCommentModel_DateOfManufacture(Date CommentModel_DateOfManufacture) { this.CommentModel_DateOfManufacture = CommentModel_DateOfManufacture; }
    public String getCommentModel_Host_Name() { return CommentModel_Host_Name; }
    public void setCommentModel_Host_Name(String CommentModel_Host_Name) { this.CommentModel_Host_Name = CommentModel_Host_Name; }
    public String getCommentModel_Comment_Uid() { return CommentModel_Comment_Uid; }
    public void setCommentModel_Comment_Uid(String CommentModel_Comment_Uid) { this.CommentModel_Comment_Uid = CommentModel_Comment_Uid; }
    public String getCommentModel_Post_Uid() { return CommentModel_Post_Uid; }
    public void setCommentModel_Post_Uid(String CommentModel_Post_Uid) { this.CommentModel_Post_Uid = CommentModel_Post_Uid; }
    public String getCommentModel_Host_Image(){
        return this.CommentModel_Host_Image;
    }
    public void setCommentModel_Host_Image(String CommentModel_Host_Image){ this.CommentModel_Host_Image = CommentModel_Host_Image; }

}
