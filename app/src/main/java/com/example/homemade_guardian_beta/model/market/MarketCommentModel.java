package com.example.homemade_guardian_beta.model.market;

import java.util.Date;

public class MarketCommentModel {
    private String MarketModel_Comment;    //댓글 내용
    private String MarketModel_Host_Image; //댓글 작성자 프로필
    private String MarketModel_Host_Uid;   //댓글 작성자 Uid
    private String MarketModel_Host_Name;  //댓글 작성자 이름
    private String MarketModel_Post_Uid;   //댓글이 쓰여진 게시물의 Uid
    private String MarketModel_Comment_Uid;//댓글의 Uid
    private Date MarketModel_DateOfManufacture;    //댓글 작성 시간

    public MarketCommentModel(String MarketModel_Host_Uid, String MarketModel_Comment, Date MarketModel_DateOfManufacture, String MarketModel_Host_Name, String MarketModel_Comment_Uid, String MarketModel_Post_Uid, String MarketModel_Host_Image){
        this.MarketModel_Host_Uid = MarketModel_Host_Uid;
        this.MarketModel_Comment = MarketModel_Comment;
        this.MarketModel_DateOfManufacture = MarketModel_DateOfManufacture;
        this.MarketModel_Host_Name = MarketModel_Host_Name;
        this.MarketModel_Comment_Uid = MarketModel_Comment_Uid;
        this.MarketModel_Post_Uid = MarketModel_Post_Uid;
        this.MarketModel_Host_Image = MarketModel_Host_Image;
    }

    public MarketCommentModel(){ }
    public String getMarketModel_Host_Uid() { return MarketModel_Host_Uid; }
    public void setMarketModel_Host_Uid(String CommentModel_Host_Uid) { this.MarketModel_Host_Uid = CommentModel_Host_Uid; }
    public String getMarketModel_Comment() { return MarketModel_Comment; }
    public void setMarketModel_Comment(String CommentModel_Comment) { this.MarketModel_Comment = CommentModel_Comment; }
    public Date getMarketModel_DateOfManufacture() { return MarketModel_DateOfManufacture; }
    public void setMarketModel_DateOfManufacture(Date CommentModel_DateOfManufacture) { this.MarketModel_DateOfManufacture = CommentModel_DateOfManufacture; }
    public String getMarketModel_Host_Name() { return MarketModel_Host_Name; }
    public void setMarketModel_Host_Name(String CommentModel_Host_Name) { this.MarketModel_Host_Name = CommentModel_Host_Name; }
    public String getMarketModel_Comment_Uid() { return MarketModel_Comment_Uid; }
    public void setMarketModel_Comment_Uid(String CommentModel_Comment_Uid) { this.MarketModel_Comment_Uid = CommentModel_Comment_Uid; }
    public String getMarketModel_Post_Uid() { return MarketModel_Post_Uid; }
    public void setMarketModel_Post_Uid(String CommentModel_Post_Uid) { this.MarketModel_Post_Uid = CommentModel_Post_Uid; }
    public String getMarketModel_Host_Image(){
        return this.MarketModel_Host_Image;
    }
    public void setMarketModel_Host_Image(String CommentModel_Host_Image){ this.MarketModel_Host_Image = CommentModel_Host_Image; }

}
