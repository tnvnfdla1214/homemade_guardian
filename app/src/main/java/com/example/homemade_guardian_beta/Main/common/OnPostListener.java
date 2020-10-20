package com.example.homemade_guardian_beta.Main.common;

import com.example.homemade_guardian_beta.model.community.CommunityModel;
import com.example.homemade_guardian_beta.model.community.Community_CommentModel;
import com.example.homemade_guardian_beta.model.market.Market_CommentModel;
import com.example.homemade_guardian_beta.model.market.MarketModel;

//삭제를 할 때에 FirebaseHelper와 연결되어 사용되는 Interface이다.

public interface OnPostListener {
    void onDelete(MarketModel marketModel);
    void oncommentDelete(Market_CommentModel market_commentModel);
    void oncommunityDelete(CommunityModel communityModel);
    void oncommnitycommentDelete(Community_CommentModel community_commentModel);
    public void onModify();

}
