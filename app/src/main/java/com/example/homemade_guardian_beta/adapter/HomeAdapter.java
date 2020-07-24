package com.example.homemade_guardian_beta.adapter;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.homemade_guardian_beta.FirebaseHelper;
import com.example.homemade_guardian_beta.PostInfo;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.activity.PostActivity;
import com.example.homemade_guardian_beta.activity.WritePostActivity;
import com.example.homemade_guardian_beta.listener.OnPostListener;
import com.example.homemade_guardian_beta.view.ReadContentsView;

//import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MainViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private Activity activity;
    private FirebaseHelper firebaseHelper;
    //private ArrayList<ArrayList<SimpleExoPlayer>> playerArrayListArrayList = new ArrayList<>();
    private final int MORE_INDEX = 2;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public HomeAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
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
    public HomeAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {             // part : 게시물을 눌렀을 떄
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                                   // part18 : 게시물 클릭시 게시물페이지로 이동 (36'10")
                Intent intent = new Intent(activity, PostActivity.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });

        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           // part15: 점3개 메뉴(수정, 삭제) (20'30")
                showPopup(v, mainViewHolder.getAdapterPosition());
            }
        });

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
                        myStartActivity(WritePostActivity.class, mDataset.get(position));
                        return true;
                    case R.id.delete:
                        firebaseHelper.storageDelete(mDataset.get(position));
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
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