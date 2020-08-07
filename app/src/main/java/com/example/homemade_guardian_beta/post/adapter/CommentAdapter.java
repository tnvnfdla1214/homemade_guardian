package com.example.homemade_guardian_beta.post.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.homemade_guardian_beta.MainActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.chat.ChatActivity;
import com.example.homemade_guardian_beta.post.Comment;
import com.example.homemade_guardian_beta.post.FirebaseHelper;
import com.example.homemade_guardian_beta.post.activity.PostActivity;
import com.example.homemade_guardian_beta.post.view.CommentItemView;
import com.example.homemade_guardian_beta.post.view.ReadContentsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<Comment> mDataset;
    private Activity activity;
    private FirebaseHelper firebaseHelper;

    private FirebaseUser currentUser;
    private Context context;

    // cardview에 하나 씩 넣은 댓글들

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CommentViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
    ///
    public CommentAdapter(Activity activity, ArrayList<Comment> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;

        //삭제 할 때 쓸거임
        firebaseHelper = new FirebaseHelper(activity);
    }
    ///
    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        final CommentViewHolder commentViewHolder = new CommentViewHolder(cardView);
        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           // part15: 점3개 메뉴(수정, 삭제) (20'30")
                showPopup(v, commentViewHolder.getAdapterPosition());
            }
        });
        return commentViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
        final RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        Comment comment = mDataset.get(position);                                                         //HomeFragment에서 PostInfo(mDaset)에 넣은 데이터 get
        titleTextView.setText(comment.getName());


        ImageView imageView = cardView.findViewById(R.id.user_image);
        Glide.with(activity).load(R.drawable.user).apply(requestOptions).into(imageView);
        CommentItemView commentItemView = cardView.findViewById(R.id.commentitemView);
        //commentItemView.setComment(comment);
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(comment)) {                 // part16 : 게시물 개수에 변화가 있을 때만 실행..? (26'40")
            contentsLayout.setTag(comment);
            contentsLayout.removeAllViews();                                                                // part14: 다 지웠다가 다시 생성

            commentItemView.setComment(comment);

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
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mDataset.get(position);

        if(currentUser.getUid().equals(comment.getUid())){
            popup.getMenuInflater().inflate(R.menu.post_host, popup.getMenu());
            popup.show();
        }
        else{
            popup.getMenuInflater().inflate(R.menu.post_guest, popup.getMenu());
            popup.show();
        }

        // 본인일 때만 수정 삭제 표현 ( 수정이랑 삭제가 남음)
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        //myStartActivity(ModifyPostActivity.class, mDataset.get(position));
                        return true;
                    case R.id.delete:
                        Log.d("로그w","삭제 1번");
                        firebaseHelper.commentstoreDelete(comment);
                        Log.d("로그w","삭제 2번");
                        Intent intentpage = new Intent(context,PostActivity.class);
                        //intentpage.putExtra("postInfo", comment);
                        Log.d("로그w","삭제 3번");
                        context.startActivity(intentpage);
                        Log.d("로그w","삭제 4번");
                        Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                        Log.d("로그w","삭제 5번");
                        return true;
                    case R.id.report:
                        Toast.makeText(context, "신고 되었습니다.", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.chat:
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("toUid", comment.getUid());
                        context.startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }


}
