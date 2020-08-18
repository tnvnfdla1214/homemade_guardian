package com.example.homemade_guardian_beta.post.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.FirebaseHelper;
import com.example.homemade_guardian_beta.post.PostInfo;
import com.example.homemade_guardian_beta.post.activity.ModifyPostActivity;
import com.example.homemade_guardian_beta.post.activity.PostActivity;
import com.example.homemade_guardian_beta.post.listener.OnPostListener;
import com.example.homemade_guardian_beta.post.view.ReadContentsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MainViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private Activity activity;
    private FirebaseHelper firebaseHelper;
    //private ArrayList<ArrayList<SimpleExoPlayer>> playerArrayListArrayList = new ArrayList<>();
    private final int MORE_INDEX = 2;
    private FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public SearchResultAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;

        firebaseHelper = new FirebaseHelper(activity);
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public SearchResultAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {             // part : 게시물을 눌렀을 떄
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                                   // part18 : 게시물 클릭시 게시물페이지로 이동 (36'10")
                Intent intent = new Intent(activity, PostActivity.class);
                //postInfo 안에 uid있음
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
                Log.d("로그","포스트 44444");
            }
        });


        /*
        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           // part15: 점3개 메뉴(수정, 삭제) (20'30")
                showPopup(v, mainViewHolder.getAdapterPosition());
            }
        });
         */

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {                      // part : 게시물을 나열
        Log.d("로그","로딩 11111");
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        Log.d("로그","로딩 22222");
        PostInfo postInfo = mDataset.get(position);                                                         //HomeFragment에서 PostInfo(mDaset)에 넣은 데이터 get
        titleTextView.setText(postInfo.getTitle());

        ReadContentsView readContentsView = cardView.findViewById(R.id.readContentsView);                   //contentsLayout에다가 날짜포함
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(postInfo)) {                 // part16 : 게시물 개수에 변화가 있을 때만 실행..? (26'40")
            contentsLayout.setTag(postInfo);
            contentsLayout.removeAllViews();                                                                // part14: 다 지웠다가 다시 생성

            readContentsView.setMoreIndex(MORE_INDEX);                                                      // part19 : 위에서 두개 까지만 표시
            readContentsView.setPostInfo(postInfo);

//            ArrayList<SimpleExoPlayer> playerArrayList = readContentsVIew.getPlayerArrayList();             // part21 : 폰이 꺼지거나 하면 동영상 멈추게 하기 (9')
//            if(playerArrayList != null){
//                playerArrayListArrayList.add(playerArrayList);
//            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void showPopup(View v, final int position) {                                                // part15 : 오른쪽 상단의 점3개 (수정 삭제) (23'25")
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        myStartActivity(ModifyPostActivity.class, mDataset.get(position));
                        return true;
                    case R.id.delete:
                        Log.d("로그","삭제 11111");
                        firebaseHelper.storageDelete(mDataset.get(position));
                        Log.d("로그","삭제 22222");
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post_host, popup.getMenu());
        popup.show();
    }

    private void myStartActivity(Class c, PostInfo postInfo) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent intent = new Intent(activity, c);
        intent.putExtra("postInfo", postInfo);
        activity.startActivity(intent);
    }

//    public void playerStop(){                                                                           // part21 :  폰이 꺼질시 종졍상 정지 (11'40")
//        for(int i = 0; i < playerArrayListArrayList.size(); i++){
//            ArrayList<SimpleExoPlayer> playerArrayList = playerArrayListArrayList.get(i);
//            for(int ii = 0; ii < playerArrayList.size(); ii++){
//                SimpleExoPlayer player = playerArrayList.get(ii);
//                if(player.getPlayWhenReady()){
//                    player.setPlayWhenReady(false);
//                }
//            }
//        }
//    }
}
