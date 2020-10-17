package com.example.homemade_guardian_beta.market.common.listener;

import com.example.homemade_guardian_beta.model.market.Market_CommentModel;
import com.example.homemade_guardian_beta.model.market.MarketModel;

//삭제를 할 때에 FirebaseHelper와 연결되어 사용되는 Interface이다.

public interface OnPostListener {
    void onDelete(MarketModel postmodel);
    void oncommentDelete(Market_CommentModel commentmodel);
    public void onModify();

}
