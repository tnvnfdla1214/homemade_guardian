package com.example.homemade_guardian_beta.model.market;

import java.util.Date;

public class Market_CommentModel {
    private String Market_CommentModel_Comment;    //댓글 내용
    private String Market_CommentModel_Host_Image; //댓글 작성자 프로필
    private String Market_CommentModel_Host_Uid;   //댓글 작성자 Uid
    private String Market_CommentModel_Host_Name;  //댓글 작성자 이름
    private String Market_CommentModel_Post_Uid;   //댓글이 쓰여진 게시물의 Uid
    private String Market_CommentModel_Comment_Uid;//댓글의 Uid
    private Date Market_CommentModel_DateOfManufacture;    //댓글 작성 시간

    public Market_CommentModel(String Market_CommentModel_Host_Uid, String Market_CommentModel_Comment, Date Market_CommentModel_DateOfManufacture, String Market_CommentModel_Host_Name, String Market_CommentModel_Comment_Uid, String Market_CommentModel_Post_Uid, String Market_CommentModel_Host_Image){
        this.Market_CommentModel_Host_Uid = Market_CommentModel_Host_Uid;
        this.Market_CommentModel_Comment = Market_CommentModel_Comment;
        this.Market_CommentModel_DateOfManufacture = Market_CommentModel_DateOfManufacture;
        this.Market_CommentModel_Host_Name = Market_CommentModel_Host_Name;
        this.Market_CommentModel_Comment_Uid = Market_CommentModel_Comment_Uid;
        this.Market_CommentModel_Post_Uid = Market_CommentModel_Post_Uid;
        this.Market_CommentModel_Host_Image = Market_CommentModel_Host_Image;
    }

    public Market_CommentModel(){ }
    public String getMarket_CommentModel_Host_Uid() { return Market_CommentModel_Host_Uid; }
    public void setMarket_CommentModel_Host_Uid(String CommentModel_Host_Uid) { this.Market_CommentModel_Host_Uid = CommentModel_Host_Uid; }
    public String getMarket_CommentModel_Comment() { return Market_CommentModel_Comment; }
    public void setMarket_CommentModel_Comment(String CommentModel_Comment) { this.Market_CommentModel_Comment = CommentModel_Comment; }
    public Date getMarket_CommentModel_DateOfManufacture() { return Market_CommentModel_DateOfManufacture; }
    public void setMarket_CommentModel_DateOfManufacture(Date CommentModel_DateOfManufacture) { this.Market_CommentModel_DateOfManufacture = CommentModel_DateOfManufacture; }
    public String getMarket_CommentModel_Host_Name() { return Market_CommentModel_Host_Name; }
    public void setMarket_CommentModel_Host_Name(String CommentModel_Host_Name) { this.Market_CommentModel_Host_Name = CommentModel_Host_Name; }
    public String getMarket_CommentModel_Comment_Uid() { return Market_CommentModel_Comment_Uid; }
    public void setMarket_CommentModel_Comment_Uid(String CommentModel_Comment_Uid) { this.Market_CommentModel_Comment_Uid = CommentModel_Comment_Uid; }
    public String getMarket_CommentModel_Post_Uid() { return Market_CommentModel_Post_Uid; }
    public void setMarket_CommentModel_Post_Uid(String CommentModel_Post_Uid) { this.Market_CommentModel_Post_Uid = CommentModel_Post_Uid; }
    public String getMarket_CommentModel_Host_Image(){
        return this.Market_CommentModel_Host_Image;
    }
    public void setMarket_CommentModel_Host_Image(String CommentModel_Host_Image){ this.Market_CommentModel_Host_Image = CommentModel_Host_Image; }

}
